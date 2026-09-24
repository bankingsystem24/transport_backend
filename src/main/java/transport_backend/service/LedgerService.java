package transport_backend.service;

import org.springframework.stereotype.Service;

import transport_backend.dto.BusinessStatusResponse;
import transport_backend.dto.LedgerBilledVehicleResponse;
import transport_backend.dto.LedgerPaymentResponse;
import transport_backend.entity.CompanyMaster;
import transport_backend.entity.PaymentTransaction;
import transport_backend.entity.TransportTransaction;
import transport_backend.repository.CompanyMasterRepository;
import transport_backend.repository.PaymentTransactionRepository;
import transport_backend.repository.TransportTransactionRepository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
public class LedgerService {

        private final TransportTransactionRepository transportTransactionRepository;
        private final PaymentTransactionRepository paymentTransactionRepository;
        private final CompanyMasterRepository companyMasterRepository;

        public LedgerService(
                        TransportTransactionRepository transportTransactionRepository,
                        PaymentTransactionRepository paymentTransactionRepository,
                        CompanyMasterRepository companyMasterRepository) {

                this.transportTransactionRepository = transportTransactionRepository;
                this.paymentTransactionRepository = paymentTransactionRepository;
                this.companyMasterRepository = companyMasterRepository;
        }

        public List<LedgerBilledVehicleResponse> getLedgerBilled(
                        Long ownerId,
                        Long companyId,
                        LocalDate fromDate,
                        LocalDate toDate) {

                if (ownerId == null) {
                        throw new RuntimeException("Owner ID is required");
                }

                if (fromDate == null) {
                        throw new RuntimeException("From date is required");
                }

                if (toDate == null) {
                        throw new RuntimeException("To date is required");
                }

                if (fromDate.isAfter(toDate)) {
                        throw new RuntimeException(
                                        "From date cannot be greater than to date");
                }

                List<TransportTransaction> transactions = transportTransactionRepository
                                .findLedgerBilledTransactions(
                                                ownerId,
                                                companyId,
                                                fromDate,
                                                toDate);

                Map<Long, LedgerBilledVehicleResponse> vehicleMap = new LinkedHashMap<>();

                for (TransportTransaction transaction : transactions) {

                        Long vehicleId = transaction.getVehicle() != null
                                        ? transaction.getVehicle().getId()
                                        : null;

                        String vehicleName = transaction.getVehicleName();
                         String companyName = transaction.getCompany() != null
                                ? transaction.getCompany().getCompanyName()
                                : null;

                        /*
                         * If vehicle ID is null, use a separate key.
                         */
                        Long mapKey = vehicleId != null
                                        ? vehicleId
                                        : -transaction.getId();

                        LedgerBilledVehicleResponse response = vehicleMap.get(mapKey);

                        if (response == null) {

                                response = new LedgerBilledVehicleResponse(
                                                vehicleId,
                                                vehicleName,
                                                BigDecimal.ZERO,
                                                BigDecimal.ZERO,
                                                BigDecimal.ZERO,
                                                BigDecimal.ZERO,
                                                companyId,
                                                companyName);

                                vehicleMap.put(mapKey, response);
                        }

                        BigDecimal billedAmount = transaction.getTotalAmount() != null
                                        ? transaction.getTotalAmount()
                                        : BigDecimal.ZERO;

                        BigDecimal dieselAmount = transaction.getDieselAmount() != null
                                        ? transaction.getDieselAmount()
                                        : BigDecimal.ZERO;

                        BigDecimal advance = transaction.getAdvance() != null
                                        ? transaction.getAdvance()
                                        : BigDecimal.ZERO;

                        BigDecimal balanceAmount = billedAmount
                                        .subtract(dieselAmount)
                                        .subtract(advance);

                        response.setBalanceAmount(
                                        response.getBalanceAmount().add(balanceAmount));

                        response.setDieselAmount(
                                        response.getDieselAmount().add(dieselAmount));

                        response.setAdvance(
                                        response.getAdvance().add(advance));

                        response.setBilledAmount(
                                        response.getBilledAmount().add(billedAmount));
                        response.setCompanyId(response.getCompanyId());
                        response.setCompanyName(response.getCompanyName());

                }

                return new ArrayList<>(vehicleMap.values());
        }

        public List<LedgerPaymentResponse> getLedgerPayments(
                        Long ownerId,
                        Long companyId,
                        LocalDate fromDate,
                        LocalDate toDate) {

                if (ownerId == null) {
                        throw new RuntimeException("Owner ID is required");
                }

                if (fromDate == null) {
                        throw new RuntimeException("From date is required");
                }

                if (toDate == null) {
                        throw new RuntimeException("To date is required");
                }

                if (fromDate.isAfter(toDate)) {
                        throw new RuntimeException(
                                        "From date cannot be greater than to date");
                }

                List<PaymentTransaction> payments = paymentTransactionRepository
                                .findByOwner_IdAndCompany_IdAndPaymentMonthBetween(
                                                ownerId,
                                                companyId,
                                                fromDate,
                                                toDate);

                return payments.stream()
                                .map(payment -> new LedgerPaymentResponse(
                                                payment.getId(),
                                                payment.getPaymentDate(),
                                                payment.getPaymentMonth(),
                                                payment.getBankName(),
                                                payment.getAccountNo(),
                                                payment.getChequeNo(),
                                                payment.getAmount(),
                                                payment.getCompany().getId(),
                                                payment.getRemarks()))
                                .toList();
        }

        public List<BusinessStatusResponse> getBusinessStatus(
                        Long companyId,
                        LocalDate fromDate,
                        LocalDate toDate) {

                if (fromDate == null) {
                        throw new RuntimeException("From date is required");
                }

                if (toDate == null) {
                        throw new RuntimeException("To date is required");
                }

                if (fromDate.isAfter(toDate)) {
                        throw new RuntimeException(
                                        "From date cannot be greater than to date");
                }

                CompanyMaster company = companyMasterRepository.findById(companyId)
                        .orElseThrow(() ->
                                new RuntimeException("Company not found with id: " + companyId));

                String companyName = company.getCompanyName();

                List<TransportTransaction> transactions = transportTransactionRepository
                                .findBusinessStatusTransactions(
                                                companyId,
                                                fromDate,
                                                toDate);

                List<PaymentTransaction> payments = paymentTransactionRepository
                                .findBusinessStatusPayments(
                                                companyId,
                                                fromDate,
                                                toDate);

                Map<Long, BusinessStatusResponse> ownerMap = new LinkedHashMap<>();

                for (TransportTransaction transaction : transactions) {

                        if (transaction.getOwner() == null) {
                                continue;
                        }

                        Long ownerId = transaction.getOwner().getId();

                        String ownerName = transaction.getOwner().getOwnerName();

                        BusinessStatusResponse response = ownerMap.get(ownerId);

                        if (response == null) {

                                response = new BusinessStatusResponse(
                                                ownerId,
                                                ownerName,
                                                companyId,
                                                companyName,
                                                BigDecimal.ZERO,
                                                BigDecimal.ZERO,
                                                BigDecimal.ZERO,
                                                BigDecimal.ZERO);

                                ownerMap.put(ownerId, response);
                        }

                        BigDecimal totalAmount = transaction.getTotalAmount() != null
                                        ? transaction.getTotalAmount()
                                        : BigDecimal.ZERO;

                        BigDecimal dieselAmount = transaction.getDieselAmount() != null
                                        ? transaction.getDieselAmount()
                                        : BigDecimal.ZERO;

                        BigDecimal advance = transaction.getAdvance() != null
                                        ? transaction.getAdvance()
                                        : BigDecimal.ZERO;

                        BigDecimal tripBalance = transaction.getTripBalance() != null
                                        ? transaction.getTripBalance()
                                        : BigDecimal.ZERO;

                        BigDecimal billedAmount = totalAmount
                                        .subtract(dieselAmount)
                                        .subtract(advance);

                        response.setBilledAmount(
                                        response.getBilledAmount()
                                                        .add(billedAmount));

                        response.setTripBalance(
                                        response.getTripBalance()
                                                        .add(tripBalance));
                }

                for (PaymentTransaction payment : payments) {

                        if (payment.getOwner() == null) {
                                continue;
                        }

                        Long ownerId = payment.getOwner().getId();

                        String ownerName = payment.getOwner().getOwnerName();

                        BusinessStatusResponse response = ownerMap.get(ownerId);

                        if (response == null) {

                                response = new BusinessStatusResponse(
                                                ownerId,
                                                ownerName,
                                                companyId,
                                                companyName,
                                                BigDecimal.ZERO,
                                                BigDecimal.ZERO,
                                                BigDecimal.ZERO,
                                                BigDecimal.ZERO);

                                ownerMap.put(ownerId, response);
                        }

                        BigDecimal paymentAmount = payment.getAmount() != null
                                        ? payment.getAmount()
                                        : BigDecimal.ZERO;

                        response.setPaymentAmount(
                                        response.getPaymentAmount()
                                                        .add(paymentAmount));

                }

                for (BusinessStatusResponse response : ownerMap.values()) {

                        BigDecimal balancePayment = response.getTripBalance()
                                        .subtract(response.getPaymentAmount());

                        response.setBalancePayment(balancePayment);
                }

                return new ArrayList<>(ownerMap.values());
        }

}