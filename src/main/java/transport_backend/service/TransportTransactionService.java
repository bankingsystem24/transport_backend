package transport_backend.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import transport_backend.dto.TransportTransactionReportResponse;
import transport_backend.dto.TransportTransactionRequest;
import transport_backend.dto.TransportTransactionResponse;
import transport_backend.entity.CompanyMaster;
import transport_backend.entity.DestinationMaster;
import transport_backend.entity.OwnerMaster;
import transport_backend.entity.PartyMaster;
import transport_backend.entity.ProductMaster;
import transport_backend.entity.TransportTransaction;
import transport_backend.entity.TransportTransactionLog;
import transport_backend.entity.User;
import transport_backend.entity.VehicleMaster;
import transport_backend.repository.CompanyMasterRepository;
import transport_backend.repository.DestinationMasterRepository;
import transport_backend.repository.OwnerMasterRepository;
import transport_backend.repository.PartyMasterRepository;
import transport_backend.repository.ProductMasterRepository;
import transport_backend.repository.TransportTransactionLogRepository;
import transport_backend.repository.TransportTransactionRepository;
import transport_backend.repository.UserRepository;
import transport_backend.repository.VehicleMasterRepository;

@Service
@Transactional
public class TransportTransactionService {

        private final TransportTransactionRepository transactionRepository;
        private final TransportTransactionLogRepository logRepository;

        private final ProductMasterRepository productRepository;
        private final CompanyMasterRepository companyRepository;
        private final OwnerMasterRepository ownerRepository;
        private final VehicleMasterRepository vehicleRepository;
        private final PartyMasterRepository partyRepository;
        private final DestinationMasterRepository destinationRepository;
        private final UserRepository userRepository;

        public TransportTransactionService(
                        TransportTransactionRepository transactionRepository,
                        TransportTransactionLogRepository logRepository,
                        ProductMasterRepository productRepository,
                        CompanyMasterRepository companyRepository,
                        OwnerMasterRepository ownerRepository,
                        VehicleMasterRepository vehicleRepository,
                        PartyMasterRepository partyRepository,
                        DestinationMasterRepository destinationRepository,
                        UserRepository userRepository) {

                this.transactionRepository = transactionRepository;
                this.logRepository = logRepository;

                this.productRepository = productRepository;
                this.companyRepository = companyRepository;
                this.ownerRepository = ownerRepository;
                this.vehicleRepository = vehicleRepository;
                this.partyRepository = partyRepository;
                this.destinationRepository = destinationRepository;
                this.userRepository = userRepository;
        }

        @Transactional
        public TransportTransactionResponse create(
                TransportTransactionRequest request) {

        String diNo = request.getDiNo() != null
                ? request.getDiNo().trim()
                : null;

        String lrNo = request.getLrNo() != null
                ? request.getLrNo().trim()
                : null;

        String invoiceNo = request.getInvoiceNo() != null
                ? request.getInvoiceNo().trim()
                : null;

        if (transactionRepository.existsByCompany_IdAndDiNoAndLrNoAndInvoiceNo(
                request.getCompanyId(),
                diNo,
                lrNo,
                invoiceNo)) {

        throw new RuntimeException(
                "Transaction already exists for "
                        + "DI No: " + diNo
                        + ", LR No: " + lrNo
                        + ", Invoice No: " + invoiceNo);
        }

        TransportTransaction transaction =
                new TransportTransaction();

        mapRequestToEntity(transaction, request);

        transaction.setCreatedDate(LocalDateTime.now());

        TransportTransaction saved =
                transactionRepository.save(transaction);

        saveLog(
                saved,
                "CREATE",
                null,
                createSnapshot(saved),
                getUser(request.getCreatedById()));

        return mapToResponse(saved);
        }

        @Transactional(readOnly = true)
        public TransportTransactionResponse getById(Long id) {

                TransportTransaction transaction = transactionRepository.findById(id)
                                .orElseThrow(() -> new RuntimeException(
                                                "Transport transaction not found with id: "
                                                                + id));

                return mapToResponse(transaction);
        }

        @Transactional(readOnly = true)
        public List<TransportTransactionResponse> getAll(Long companyId) {

        return transactionRepository
                .findByCompany_Id(companyId)
                .stream()
                .map(this::mapToResponse)
                .toList();
        }

        public TransportTransactionResponse update(
                        Long id,
                        TransportTransactionRequest request) {

                TransportTransaction transaction = transactionRepository.findById(id)
                                .orElseThrow(() -> new RuntimeException(
                                                "Transport transaction not found with id: "
                                                                + id));

                String oldData = createSnapshot(transaction);

                mapRequestToEntity(transaction, request);

                TransportTransaction updated = transactionRepository.save(transaction);

                String newData = createSnapshot(updated);

                saveLog(
                                updated,
                                "UPDATE",
                                oldData,
                                newData,
                                getUser(request.getCreatedById()));

                return mapToResponse(updated);
        }

        public void delete(Long id) {

                TransportTransaction transaction = transactionRepository.findById(id)
                                .orElseThrow(() -> new RuntimeException(
                                                "Transport transaction not found with id: "
                                                                + id));

                transactionRepository.delete(transaction);
        }

        private void mapRequestToEntity(
                        TransportTransaction transaction,
                        TransportTransactionRequest request) {

                ProductMaster product = productRepository.findById(request.getProductId())
                                .orElseThrow(() -> new RuntimeException(
                                                "Product not found with id: "
                                                                + request.getProductId()));

                transaction.setProduct(product);

                CompanyMaster company = companyRepository.findById(request.getCompanyId())
                                .orElseThrow(() -> new RuntimeException(
                                                "Company not found with id: "
                                                                + request.getCompanyId()));

                transaction.setCompany(company);

                if (request.getOwnerId() != null) {

                        OwnerMaster owner = ownerRepository.findById(request.getOwnerId())
                                        .orElseThrow(() -> new RuntimeException(
                                                        "Owner not found with id: "
                                                                        + request.getOwnerId()));

                        transaction.setOwner(owner);

                } else {

                        transaction.setOwner(null);
                }

                if (request.getVehicleId() != null) {

                        VehicleMaster vehicle = vehicleRepository.findById(request.getVehicleId())
                                        .orElseThrow(() -> new RuntimeException(
                                                        "Vehicle not found with id: "
                                                                        + request.getVehicleId()));

                        transaction.setVehicle(vehicle);

                } else {

                        transaction.setVehicle(null);
                }

                if (request.getPartyId() != null) {

                        PartyMaster party = partyRepository.findById(request.getPartyId())
                                        .orElseThrow(() -> new RuntimeException(
                                                        "Party not found with id: "
                                                                        + request.getPartyId()));

                        transaction.setParty(party);

                } else {

                        transaction.setParty(null);
                }

                if (request.getDestinationId() != null) {

                        DestinationMaster destination = destinationRepository.findById(
                                        request.getDestinationId())
                                        .orElseThrow(() -> new RuntimeException(
                                                        "Destination not found with id: "
                                                                        + request.getDestinationId()));

                        transaction.setDestination(destination);

                } else {

                        transaction.setDestination(null);
                }

                transaction.setVehicleName(
                                request.getVehicleName());

                transaction.setPartyName(
                                request.getPartyName());

                transaction.setDestinationName(
                                request.getDestinationName());

                transaction.setDiDate(request.getDiDate());
                transaction.setDiNo(request.getDiNo());
                transaction.setLrNo(request.getLrNo());

                transaction.setInvoiceNo(request.getInvoiceNo());
                transaction.setInvoiceNo1(request.getInvoiceNo1());
                transaction.setInvoiceNo2(request.getInvoiceNo2());

                transaction.setLoadingWt(request.getLoadingWt());
                transaction.setUnloadingWt(request.getUnloadingWt());

                transaction.setLoadingDate(
                                request.getLoadingDate());

                transaction.setUnloadingDate(
                                request.getUnloadingDate());

                transaction.setShortage(
                                request.getShortage());

                transaction.setOwnerRate(
                                request.getOwnerRate());

                transaction.setTotalAmount(
                                request.getTotalAmount());

                transaction.setJumboRate(
                                request.getJumboRate());

                transaction.setTonMt(
                                request.getTonMt());

                transaction.setOtherPay(
                                request.getOtherPay());

                transaction.setDieselRqNo(
                                request.getDieselRqNo());

                transaction.setDieselRqDate(
                                request.getDieselRqDate());

                transaction.setDieselRate(
                                request.getDieselRate());

                transaction.setDieselQty(
                                request.getDieselQty());

                transaction.setDieselAmount(
                                request.getDieselAmount());

                transaction.setAdvance(
                                request.getAdvance());

                transaction.setParkingCharges(
                                request.getParkingCharges());

                transaction.setPaymentDate(
                                request.getPaymentDate());

                transaction.setTripBalance(
                                request.getTripBalance());

                if (transaction.getId() == null
                                && request.getCreatedById() != null) {

                        User user = getUser(request.getCreatedById());

                        transaction.setCreatedBy(user);
                }
        }

        private User getUser(Long userId) {

                if (userId == null) {
                        return null;
                }

                return userRepository.findById(userId)
                                .orElseThrow(() -> new RuntimeException(
                                                "User not found with id: " + userId));
        }

        private void saveLog(
                        TransportTransaction transaction,
                        String action,
                        String oldData,
                        String newData,
                        User changedBy) {

                TransportTransactionLog log = new TransportTransactionLog();

                log.setTransaction(transaction);
                log.setAction(action);
                log.setChangedBy(changedBy);
                log.setChangedDate(LocalDateTime.now());

                log.setOldData(oldData);
                log.setNewData(newData);

                logRepository.save(log);
        }

        private String createSnapshot(
                        TransportTransaction transaction) {

                return "{"
                                + "\"id\":" + value(transaction.getId())

                                + ",\"productId\":" + value(
                                                transaction.getProduct() != null
                                                                ? transaction.getProduct().getId()
                                                                : null)

                                + ",\"productName\":" + text(
                                                transaction.getProduct() != null
                                                                ? transaction.getProduct().getProductName()
                                                                : null)

                                + ",\"companyId\":" + value(
                                                transaction.getCompany() != null
                                                                ? transaction.getCompany().getId()
                                                                : null)

                                + ",\"companyName\":" + text(
                                                transaction.getCompany() != null
                                                                ? transaction.getCompany().getCompanyName()
                                                                : null)

                                + ",\"ownerId\":" + value(
                                                transaction.getOwner() != null
                                                                ? transaction.getOwner().getId()
                                                                : null)

                                + ",\"ownerName\":" + text(
                                                transaction.getOwner() != null
                                                                ? transaction.getOwner().getOwnerName()
                                                                : null)

                                + ",\"vehicleId\":" + value(
                                                transaction.getVehicle() != null
                                                                ? transaction.getVehicle().getId()
                                                                : null)

                                + ",\"vehicleName\":" + text(
                                                transaction.getVehicleName())

                                + ",\"partyId\":" + value(
                                                transaction.getParty() != null
                                                                ? transaction.getParty().getId()
                                                                : null)

                                + ",\"partyName\":" + text(
                                                transaction.getPartyName())

                                + ",\"destinationId\":" + value(
                                                transaction.getDestination() != null
                                                                ? transaction.getDestination().getId()
                                                                : null)

                                + ",\"destinationName\":" + text(
                                                transaction.getDestinationName())

                                + ",\"diDate\":" + text(
                                                transaction.getDiDate())

                                + ",\"diNo\":" + text(
                                                transaction.getDiNo())

                                + ",\"lrNo\":" + text(
                                                transaction.getLrNo())

                                + ",\"invoiceNo\":" + text(
                                                transaction.getInvoiceNo())

                                + ",\"invoiceNo1\":" + text(
                                                transaction.getInvoiceNo1())

                                + ",\"invoiceNo2\":" + text(
                                                transaction.getInvoiceNo2())

                                + ",\"loadingWt\":" + value(
                                                transaction.getLoadingWt())

                                + ",\"unloadingWt\":" + value(
                                                transaction.getUnloadingWt())

                                + ",\"loadingDate\":" + text(
                                                transaction.getLoadingDate())

                                + ",\"unloadingDate\":" + text(
                                                transaction.getUnloadingDate())

                                + ",\"shortage\":" + value(
                                                transaction.getShortage())

                                + ",\"ownerRate\":" + value(
                                                transaction.getOwnerRate())

                                + ",\"totalAmount\":" + value(
                                                transaction.getTotalAmount())

                                + ",\"jumboRate\":" + value(
                                                transaction.getJumboRate())

                                + ",\"tonMt\":" + value(
                                                transaction.getTonMt())

                                + ",\"otherPay\":" + value(
                                                transaction.getOtherPay())

                                + ",\"dieselRqNo\":" + text(
                                                transaction.getDieselRqNo())

                                + ",\"dieselRqDate\":" + text(
                                                transaction.getDieselRqDate())

                                + ",\"dieselRate\":" + value(
                                                transaction.getDieselRate())

                                + ",\"dieselQty\":" + value(
                                                transaction.getDieselQty())

                                + ",\"dieselAmount\":" + value(
                                                transaction.getDieselAmount())

                                + ",\"advance\":" + value(
                                                transaction.getAdvance())

                                + ",\"parkingCharges\":" + value(
                                                transaction.getParkingCharges())

                                + ",\"paymentDate\":" + text(
                                                transaction.getPaymentDate())

                                + ",\"tripBalance\":" + value(
                                                transaction.getTripBalance())

                                + ",\"createdById\":" + value(
                                                transaction.getCreatedBy() != null
                                                                ? transaction.getCreatedBy().getId()
                                                                : null)

                                + ",\"createdDate\":" + text(
                                                transaction.getCreatedDate())

                                + "}";
        }
        private String value(Object value) {

                if (value == null) {
                        return "null";
                }

                return value.toString();
        }

        private String text(Object value) {

                if (value == null) {
                        return "null";
                }

                String text = value.toString();

                text = text.replace("\\", "\\\\");
                text = text.replace("\"", "\\\"");
                text = text.replace("\n", "\\n");
                text = text.replace("\r", "\\r");

                return "\"" + text + "\"";
        }

        private TransportTransactionResponse mapToResponse(
                        TransportTransaction transaction) {

                TransportTransactionResponse response = new TransportTransactionResponse();

                response.setId(transaction.getId());

                if (transaction.getProduct() != null) {

                        response.setProductId(
                                        transaction.getProduct().getId());

                        response.setProductName(
                                        transaction.getProduct().getProductName());
                }

                if (transaction.getCompany() != null) {

                        response.setCompanyId(
                                        transaction.getCompany().getId());

                        response.setCompanyName(
                                        transaction.getCompany().getCompanyName());
                }

                if (transaction.getOwner() != null) {

                        response.setOwnerId(
                                        transaction.getOwner().getId());

                        response.setOwnerName(
                                        transaction.getOwner().getOwnerName());
                }

                if (transaction.getVehicle() != null) {

                        response.setVehicleId(
                                        transaction.getVehicle().getId());
                }

                response.setVehicleName(
                                transaction.getVehicleName());

                if (transaction.getParty() != null) {

                        response.setPartyId(
                                        transaction.getParty().getId());

                        response.setPartyName(
                                        transaction.getParty().getPartyName());

                } else {

                        response.setPartyName(
                                        transaction.getPartyName());
                }

                if (transaction.getDestination() != null) {

                        response.setDestinationId(
                                        transaction.getDestination().getId());

                        response.setDestinationName(
                                        transaction.getDestination().getDestination());

                } else {

                        response.setDestinationName(
                                        transaction.getDestinationName());
                }

                response.setDiDate(
                                transaction.getDiDate());

                response.setDiNo(
                                transaction.getDiNo());

                response.setLrNo(
                                transaction.getLrNo());

                response.setInvoiceNo(
                                transaction.getInvoiceNo());

                response.setInvoiceNo1(
                                transaction.getInvoiceNo1());

                response.setInvoiceNo2(
                                transaction.getInvoiceNo2());

                response.setLoadingWt(
                                transaction.getLoadingWt());

                response.setUnloadingWt(
                                transaction.getUnloadingWt());

                response.setLoadingDate(
                                transaction.getLoadingDate());

                response.setUnloadingDate(
                                transaction.getUnloadingDate());

                response.setShortage(
                                transaction.getShortage());
                response.setOwnerRate(
                                transaction.getOwnerRate());

                response.setTotalAmount(
                                transaction.getTotalAmount());

                response.setJumboRate(
                                transaction.getJumboRate());

                response.setTonMt(
                                transaction.getTonMt());

                response.setOtherPay(
                                transaction.getOtherPay());

                response.setDieselRqNo(
                                transaction.getDieselRqNo());

                response.setDieselRqDate(
                                transaction.getDieselRqDate());

                response.setDieselRate(
                                transaction.getDieselRate());

                response.setDieselQty(
                                transaction.getDieselQty());

                response.setDieselAmount(
                                transaction.getDieselAmount());

                response.setAdvance(
                                transaction.getAdvance());

                response.setParkingCharges(
                                transaction.getParkingCharges());

                response.setPaymentDate(
                                transaction.getPaymentDate());

                response.setTripBalance(
                                transaction.getTripBalance());

                if (transaction.getCreatedBy() != null) {

                        response.setCreatedById(
                                        transaction.getCreatedBy().getId());
                }

                response.setCreatedDate(
                                transaction.getCreatedDate());

                return response;
        }

        public List<TransportTransactionReportResponse> findReport(
                LocalDate fromDate,
                LocalDate toDate,
                Boolean owner,
                Long ownerId,
                Boolean vehicle,
                Long vehicleId) {

        if (fromDate == null || toDate == null) {
                throw new IllegalArgumentException(
                        "From date and To date are required");
        }

        if (fromDate.isAfter(toDate)) {
                throw new IllegalArgumentException(
                        "From date cannot be after To date");
        }

        List<TransportTransaction> transactions;

        if (Boolean.TRUE.equals(owner) && ownerId != null) {

                transactions = transactionRepository
                        .findByDiDateBetweenAndOwner_Id(
                                fromDate,
                                toDate,
                                ownerId);

        } else if (Boolean.TRUE.equals(vehicle) && vehicleId != null) {

                transactions = transactionRepository
                        .findByDiDateBetweenAndVehicle_Id(
                                fromDate,
                                toDate,
                                vehicleId);

        } else {

                transactions = transactionRepository
                        .findByDiDateBetween(
                                fromDate,
                                toDate);
        }

        return transactions.stream()
                .map(this::convertToReportResponse)
                .toList();
        }


        private TransportTransactionReportResponse convertToReportResponse(
                TransportTransaction transaction) {

        TransportTransactionReportResponse response =
                new TransportTransactionReportResponse();

        response.setId(transaction.getId());

        if (transaction.getProduct() != null) {
                response.setProductId(transaction.getProduct().getId());
                response.setProductName(transaction.getProduct().getProductName());
        }

        if (transaction.getCompany() != null) {
                response.setCompanyId(transaction.getCompany().getId());
                response.setCompanyName(transaction.getCompany().getCompanyName());
        }

        if (transaction.getOwner() != null) {
                response.setOwnerId(transaction.getOwner().getId());
                response.setOwnerName(transaction.getOwner().getOwnerName());
        }

        if (transaction.getVehicle() != null) {
                response.setVehicleId(transaction.getVehicle().getId());
                response.setVehicleName(transaction.getVehicleName());
        }

        if (transaction.getParty() != null) {
                response.setPartyId(transaction.getParty().getId());
                response.setPartyName(transaction.getPartyName());
        }

        if (transaction.getDestination() != null) {
                response.setDestinationId(transaction.getDestination().getId());
                response.setDestinationName(
                        transaction.getDestinationName());
        }

        response.setDiDate(transaction.getDiDate());
        response.setDiNo(transaction.getDiNo());

        response.setLrNo(transaction.getLrNo());

        response.setInvoiceNo(transaction.getInvoiceNo());
        response.setInvoiceNo1(transaction.getInvoiceNo1());
        response.setInvoiceNo2(transaction.getInvoiceNo2());

        response.setLoadingWt(transaction.getLoadingWt());
        response.setUnloadingWt(transaction.getUnloadingWt());

        response.setLoadingDate(transaction.getLoadingDate());
        response.setUnloadingDate(transaction.getUnloadingDate());

        response.setShortage(transaction.getShortage());

        response.setOwnerRate(transaction.getOwnerRate());
        response.setTotalAmount(transaction.getTotalAmount());
        response.setJumboRate(transaction.getJumboRate());
        response.setTonMt(transaction.getTonMt());
        response.setOtherPay(transaction.getOtherPay());

        response.setDieselRqNo(transaction.getDieselRqNo());
        response.setDieselRqDate(transaction.getDieselRqDate());
        response.setDieselRate(transaction.getDieselRate());
        response.setDieselQty(transaction.getDieselQty());
        response.setDieselAmount(transaction.getDieselAmount());

        response.setAdvance(transaction.getAdvance());
        response.setParkingCharges(transaction.getParkingCharges());

        response.setPaymentDate(transaction.getPaymentDate());
        response.setTripBalance(transaction.getTripBalance());
        response.setProfitRate(transaction.getProfitRate());
        response.setProfitAmount(transaction.getProfitAmount());

        if (transaction.getCreatedBy() != null) {
                response.setCreatedBy(transaction.getCreatedBy().getId());

                response.setCreatedByName(
                        transaction.getCreatedBy().getUsername());
        }

        response.setCreatedDate(transaction.getCreatedDate());

        return response;
        }

}