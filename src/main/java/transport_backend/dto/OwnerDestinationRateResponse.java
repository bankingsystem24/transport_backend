package transport_backend.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public class OwnerDestinationRateResponse {

    private Long id;

    private Long ownerId;
    private String ownerName;

    private Long productId;
    private String productName;

    private Long destinationId;
    private String destinationName;

    private LocalDate fromDate;
    private LocalDate toDate;

    private BigDecimal companyRate;
    private BigDecimal ownerRate;
    private BigDecimal benefit;

    public OwnerDestinationRateResponse() {
    }

    public OwnerDestinationRateResponse(
            Long id,
            Long ownerId,
            String ownerName,
            Long productId,
            String productName,
            Long destinationId,
            String destinationName,
            LocalDate fromDate,
            LocalDate toDate,
            BigDecimal companyRate,
            BigDecimal ownerRate,
            BigDecimal benefit) {

        this.id = id;
        this.ownerId = ownerId;
        this.ownerName = ownerName;
        this.productId = productId;
        this.productName = productName;
        this.destinationId = destinationId;
        this.destinationName = destinationName;
        this.fromDate = fromDate;
        this.toDate = toDate;
        this.companyRate = companyRate;
        this.ownerRate = ownerRate;
        this.benefit = benefit;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public Long getDestinationId() {
        return destinationId;
    }

    public void setDestinationId(Long destinationId) {
        this.destinationId = destinationId;
    }

    public String getDestinationName() {
        return destinationName;
    }

    public void setDestinationName(String destinationName) {
        this.destinationName = destinationName;
    }

    public LocalDate getFromDate() {
        return fromDate;
    }

    public void setFromDate(LocalDate fromDate) {
        this.fromDate = fromDate;
    }

    public LocalDate getToDate() {
        return toDate;
    }

    public void setToDate(LocalDate toDate) {
        this.toDate = toDate;
    }

    public BigDecimal getCompanyRate() {
        return companyRate;
    }

    public void setCompanyRate(BigDecimal companyRate) {
        this.companyRate = companyRate;
    }

    public BigDecimal getOwnerRate() {
        return ownerRate;
    }

    public void setOwnerRate(BigDecimal ownerRate) {
        this.ownerRate = ownerRate;
    }

    public BigDecimal getBenefit() {
        return benefit;
    }

    public void setBenefit(BigDecimal benefit) {
        this.benefit = benefit;
    }
}