package transport_backend.dto;

import java.math.BigDecimal;

public class OwnerRateResponse {

    private BigDecimal ownerRate;
    private BigDecimal companyRate;
    private BigDecimal benefit;

    public OwnerRateResponse() {
    }

    public OwnerRateResponse(BigDecimal ownerRate, BigDecimal companyRate, BigDecimal benefit) {
        this.ownerRate = ownerRate;
        this.companyRate = companyRate;
        this.benefit = benefit;
    }

    public BigDecimal getOwnerRate() {
        return ownerRate;
    }

    public void setOwnerRate(BigDecimal ownerRate) {
        this.ownerRate = ownerRate;
    }

    public BigDecimal getCompanyRate() {
        return companyRate;
    }

    public void setCompanyRate(BigDecimal companyRate) {
        this.companyRate = companyRate;
    }

    public BigDecimal getBenefit() {
        return benefit;
    }

    public void setBenefit(BigDecimal benefit) {
        this.benefit = benefit;
    }
}