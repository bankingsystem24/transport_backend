package transport_backend.dto;

import java.math.BigDecimal;

public class BusinessStatusResponse {

    private Long ownerId;
    private String ownerName;

    private Long companyId;
    private String companyName;

    private BigDecimal billedAmount;
    private BigDecimal tripBalance;
    private BigDecimal paymentAmount;
    private BigDecimal balancePayment;

    public BusinessStatusResponse() {
    }

    public BusinessStatusResponse(
            Long ownerId,
            String ownerName,
            Long companyId,
            String companyName,
            BigDecimal billedAmount,
            BigDecimal tripBalance,
            BigDecimal paymentAmount,
            BigDecimal balancePayment) {

        this.ownerId = ownerId;
        this.ownerName = ownerName;
        this.companyId = companyId;
        this.companyName = companyName;
        this.billedAmount = billedAmount;
        this.tripBalance = tripBalance;
        this.paymentAmount = paymentAmount;
        this.balancePayment = balancePayment;
    }

    public Long getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(Long ownerId) {
        this.ownerId = ownerId;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public BigDecimal getBilledAmount() {
        return billedAmount;
    }

    public void setBilledAmount(BigDecimal billedAmount) {
        this.billedAmount = billedAmount;
    }

    public BigDecimal getTripBalance() {
        return tripBalance;
    }

    public void setTripBalance(BigDecimal tripBalance) {
        this.tripBalance = tripBalance;
    }

    public BigDecimal getPaymentAmount() {
        return paymentAmount;
    }

    public void setPaymentAmount(BigDecimal paymentAmount) {
        this.paymentAmount = paymentAmount;
    }

    public BigDecimal getBalancePayment() {
        return balancePayment;
    }

    public void setBalancePayment(BigDecimal balancePayment) {
        this.balancePayment = balancePayment;
    }

       public Long getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Long companyId) {
        this.companyId = companyId;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

}