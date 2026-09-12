package transport_backend.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    // =========================================================
    // CREATE
    // =========================================================

    public TransportTransactionResponse create(
            TransportTransactionRequest request) {

        TransportTransaction transaction =
                new TransportTransaction();

        mapRequestToEntity(transaction, request);

        transaction.setCreatedDate(LocalDateTime.now());

        TransportTransaction saved =
                transactionRepository.save(transaction);

        // CREATE LOG
        saveLog(
                saved,
                "CREATE",
                null,
                createSnapshot(saved),
                getUser(request.getCreatedById())
        );

        return mapToResponse(saved);
    }

    // =========================================================
    // GET BY ID
    // =========================================================

    @Transactional(readOnly = true)
    public TransportTransactionResponse getById(Long id) {

        TransportTransaction transaction =
                transactionRepository.findById(id)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Transport transaction not found with id: "
                                                + id
                                )
                        );

        return mapToResponse(transaction);
    }

    // =========================================================
    // GET ALL
    // =========================================================

    @Transactional(readOnly = true)
    public List<TransportTransactionResponse> getAll() {

        return transactionRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    // =========================================================
    // UPDATE
    // =========================================================

    public TransportTransactionResponse update(
            Long id,
            TransportTransactionRequest request) {

        TransportTransaction transaction =
                transactionRepository.findById(id)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Transport transaction not found with id: "
                                                + id
                                )
                        );

        // Snapshot BEFORE update
        String oldData = createSnapshot(transaction);

        // Update entity
        mapRequestToEntity(transaction, request);

        TransportTransaction updated =
                transactionRepository.save(transaction);

        // Snapshot AFTER update
        String newData = createSnapshot(updated);

        // UPDATE LOG
        saveLog(
                updated,
                "UPDATE",
                oldData,
                newData,
                getUser(request.getCreatedById())
        );

        return mapToResponse(updated);
    }

    // =========================================================
    // DELETE
    // =========================================================

    public void delete(Long id) {

        TransportTransaction transaction =
                transactionRepository.findById(id)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Transport transaction not found with id: "
                                                + id
                                )
                        );

        transactionRepository.delete(transaction);
    }

    // =========================================================
    // MAP REQUEST TO ENTITY
    // =========================================================

    private void mapRequestToEntity(
            TransportTransaction transaction,
            TransportTransactionRequest request) {

        // -----------------------------------------------------
        // PRODUCT
        // -----------------------------------------------------

        ProductMaster product =
                productRepository.findById(request.getProductId())
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Product not found with id: "
                                                + request.getProductId()
                                )
                        );

        transaction.setProduct(product);

        // -----------------------------------------------------
        // COMPANY
        // -----------------------------------------------------

        CompanyMaster company =
                companyRepository.findById(request.getCompanyId())
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Company not found with id: "
                                                + request.getCompanyId()
                                )
                        );

        transaction.setCompany(company);

        // -----------------------------------------------------
        // OWNER
        // -----------------------------------------------------

        if (request.getOwnerId() != null) {

            OwnerMaster owner =
                    ownerRepository.findById(request.getOwnerId())
                            .orElseThrow(() ->
                                    new RuntimeException(
                                            "Owner not found with id: "
                                                    + request.getOwnerId()
                                    )
                            );

            transaction.setOwner(owner);

        } else {

            transaction.setOwner(null);
        }

        // -----------------------------------------------------
        // VEHICLE
        // -----------------------------------------------------

        if (request.getVehicleId() != null) {

            VehicleMaster vehicle =
                    vehicleRepository.findById(request.getVehicleId())
                            .orElseThrow(() ->
                                    new RuntimeException(
                                            "Vehicle not found with id: "
                                                    + request.getVehicleId()
                                    )
                            );

            transaction.setVehicle(vehicle);

        } else {

            transaction.setVehicle(null);
        }

        // -----------------------------------------------------
        // PARTY
        // -----------------------------------------------------

        if (request.getPartyId() != null) {

            PartyMaster party =
                    partyRepository.findById(request.getPartyId())
                            .orElseThrow(() ->
                                    new RuntimeException(
                                            "Party not found with id: "
                                                    + request.getPartyId()
                                    )
                            );

            transaction.setParty(party);

        } else {

            transaction.setParty(null);
        }

        // -----------------------------------------------------
        // DESTINATION
        // -----------------------------------------------------

        if (request.getDestinationId() != null) {

            DestinationMaster destination =
                    destinationRepository.findById(
                            request.getDestinationId()
                    )
                    .orElseThrow(() ->
                            new RuntimeException(
                                    "Destination not found with id: "
                                            + request.getDestinationId()
                            )
                    );

            transaction.setDestination(destination);

        } else {

            transaction.setDestination(null);
        }

        // -----------------------------------------------------
        // NAMES
        // -----------------------------------------------------

        transaction.setVehicleName(
                request.getVehicleName()
        );

        transaction.setPartyName(
                request.getPartyName()
        );

        transaction.setDestinationName(
                request.getDestinationName()
        );

        // -----------------------------------------------------
        // DI / LR
        // -----------------------------------------------------

        transaction.setDiDate(request.getDiDate());
        transaction.setDiNo(request.getDiNo());
        transaction.setLrNo(request.getLrNo());

        // -----------------------------------------------------
        // INVOICE
        // -----------------------------------------------------

        transaction.setInvoiceNo(request.getInvoiceNo());
        transaction.setInvoiceNo1(request.getInvoiceNo1());
        transaction.setInvoiceNo2(request.getInvoiceNo2());

        // -----------------------------------------------------
        // WEIGHT
        // -----------------------------------------------------

        transaction.setLoadingWt(request.getLoadingWt());
        transaction.setUnloadingWt(request.getUnloadingWt());

        transaction.setLoadingDate(
                request.getLoadingDate()
        );

        transaction.setUnloadingDate(
                request.getUnloadingDate()
        );

        transaction.setShortage(
                request.getShortage()
        );

        // -----------------------------------------------------
        // RATES
        // -----------------------------------------------------

        transaction.setOwnerRate(
                request.getOwnerRate()
        );

        transaction.setTotalAmount(
                request.getTotalAmount()
        );

        transaction.setJumboRate(
                request.getJumboRate()
        );

        transaction.setTonMt(
                request.getTonMt()
        );

        transaction.setOtherPay(
                request.getOtherPay()
        );

        // -----------------------------------------------------
        // DIESEL
        // -----------------------------------------------------

        transaction.setDieselRqNo(
                request.getDieselRqNo()
        );

        transaction.setDieselRqDate(
                request.getDieselRqDate()
        );

        transaction.setDieselRate(
                request.getDieselRate()
        );

        transaction.setDieselQty(
                request.getDieselQty()
        );

        transaction.setDieselAmount(
                request.getDieselAmount()
        );

        // -----------------------------------------------------
        // PAYMENT
        // -----------------------------------------------------

        transaction.setAdvance(
                request.getAdvance()
        );

        transaction.setParkingCharges(
                request.getParkingCharges()
        );

        transaction.setPaymentDate(
                request.getPaymentDate()
        );

        transaction.setTripBalance(
                request.getTripBalance()
        );

        // -----------------------------------------------------
        // CREATED BY
        // Only set during CREATE
        // -----------------------------------------------------

        if (transaction.getId() == null
                && request.getCreatedById() != null) {

            User user =
                    getUser(request.getCreatedById());

            transaction.setCreatedBy(user);
        }
    }

    // =========================================================
    // GET USER
    // =========================================================

    private User getUser(Long userId) {

        if (userId == null) {
            return null;
        }

        return userRepository.findById(userId)
                .orElseThrow(() ->
                        new RuntimeException(
                                "User not found with id: " + userId
                        )
                );
    }

    // =========================================================
    // SAVE LOG
    // =========================================================

    private void saveLog(
            TransportTransaction transaction,
            String action,
            String oldData,
            String newData,
            User changedBy) {

        TransportTransactionLog log =
                new TransportTransactionLog();

        log.setTransaction(transaction);
        log.setAction(action);
        log.setChangedBy(changedBy);
        log.setChangedDate(LocalDateTime.now());

        log.setOldData(oldData);
        log.setNewData(newData);

        logRepository.save(log);
    }

    // =========================================================
    // CREATE SNAPSHOT
    // =========================================================

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

    // =========================================================
    // JSON NUMBER / VALUE
    // =========================================================

    private String value(Object value) {

        if (value == null) {
            return "null";
        }

        return value.toString();
    }

    // =========================================================
    // JSON STRING / TEXT
    // =========================================================

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

    // =========================================================
    // MAP ENTITY TO RESPONSE
    // =========================================================

    private TransportTransactionResponse mapToResponse(
            TransportTransaction transaction) {

        TransportTransactionResponse response =
                new TransportTransactionResponse();

        response.setId(transaction.getId());

        // -----------------------------------------------------
        // PRODUCT
        // -----------------------------------------------------

        if (transaction.getProduct() != null) {

            response.setProductId(
                    transaction.getProduct().getId()
            );

            response.setProductName(
                    transaction.getProduct().getProductName()
            );
        }

        // -----------------------------------------------------
        // COMPANY
        // -----------------------------------------------------

        if (transaction.getCompany() != null) {

            response.setCompanyId(
                    transaction.getCompany().getId()
            );

            response.setCompanyName(
                    transaction.getCompany().getCompanyName()
            );
        }

        // -----------------------------------------------------
        // OWNER
        // -----------------------------------------------------

        if (transaction.getOwner() != null) {

            response.setOwnerId(
                    transaction.getOwner().getId()
            );

            response.setOwnerName(
                    transaction.getOwner().getOwnerName()
            );
        }

        // -----------------------------------------------------
        // VEHICLE
        // -----------------------------------------------------

        if (transaction.getVehicle() != null) {

            response.setVehicleId(
                    transaction.getVehicle().getId()
            );
        }

        response.setVehicleName(
                transaction.getVehicleName()
        );

        // -----------------------------------------------------
        // PARTY
        // -----------------------------------------------------

        if (transaction.getParty() != null) {

            response.setPartyId(
                    transaction.getParty().getId()
            );

            response.setPartyName(
                    transaction.getParty().getPartyName()
            );

        } else {

            response.setPartyName(
                    transaction.getPartyName()
            );
        }

        // -----------------------------------------------------
        // DESTINATION
        // -----------------------------------------------------

        if (transaction.getDestination() != null) {

            response.setDestinationId(
                    transaction.getDestination().getId()
            );

            response.setDestinationName(
                    transaction.getDestination().getDestination()
            );

        } else {

            response.setDestinationName(
                    transaction.getDestinationName()
            );
        }

        // -----------------------------------------------------
        // DI / LR
        // -----------------------------------------------------

        response.setDiDate(
                transaction.getDiDate()
        );

        response.setDiNo(
                transaction.getDiNo()
        );

        response.setLrNo(
                transaction.getLrNo()
        );

        // -----------------------------------------------------
        // INVOICE
        // -----------------------------------------------------

        response.setInvoiceNo(
                transaction.getInvoiceNo()
        );

        response.setInvoiceNo1(
                transaction.getInvoiceNo1()
        );

        response.setInvoiceNo2(
                transaction.getInvoiceNo2()
        );

        // -----------------------------------------------------
        // WEIGHT
        // -----------------------------------------------------

        response.setLoadingWt(
                transaction.getLoadingWt()
        );

        response.setUnloadingWt(
                transaction.getUnloadingWt()
        );

        response.setLoadingDate(
                transaction.getLoadingDate()
        );

        response.setUnloadingDate(
                transaction.getUnloadingDate()
        );

        response.setShortage(
                transaction.getShortage()
        );

        // -----------------------------------------------------
        // RATES
        // -----------------------------------------------------

        response.setOwnerRate(
                transaction.getOwnerRate()
        );

        response.setTotalAmount(
                transaction.getTotalAmount()
        );

        response.setJumboRate(
                transaction.getJumboRate()
        );

        response.setTonMt(
                transaction.getTonMt()
        );

        response.setOtherPay(
                transaction.getOtherPay()
        );

        // -----------------------------------------------------
        // DIESEL
        // -----------------------------------------------------

        response.setDieselRqNo(
                transaction.getDieselRqNo()
        );

        response.setDieselRqDate(
                transaction.getDieselRqDate()
        );

        response.setDieselRate(
                transaction.getDieselRate()
        );

        response.setDieselQty(
                transaction.getDieselQty()
        );

        response.setDieselAmount(
                transaction.getDieselAmount()
        );

        // -----------------------------------------------------
        // PAYMENT
        // -----------------------------------------------------

        response.setAdvance(
                transaction.getAdvance()
        );

        response.setParkingCharges(
                transaction.getParkingCharges()
        );

        response.setPaymentDate(
                transaction.getPaymentDate()
        );

        response.setTripBalance(
                transaction.getTripBalance()
        );

        // -----------------------------------------------------
        // CREATED BY
        // -----------------------------------------------------

        if (transaction.getCreatedBy() != null) {

            response.setCreatedById(
                    transaction.getCreatedBy().getId()
            );
        }

        response.setCreatedDate(
                transaction.getCreatedDate()
        );

        return response;
    }
}