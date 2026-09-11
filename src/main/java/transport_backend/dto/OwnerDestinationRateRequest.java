package transport_backend.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDate;

public class OwnerDestinationRateRequest {

    @NotNull(message = "Owner is required")
    private Long ownerId;

    @NotNull(message = "Product is required")
    private Long productId;

    @NotNull(message = "Destination is required")
    private Long destinationId;

    @NotNull(message = "From date is required")
    private LocalDate fromDate;

    @NotNull(message = "To date is required")
    private LocalDate toDate;

    @NotNull(message = "Company rate is required")
    @DecimalMin(
            value = "0.00",
            inclusive = true,
            message = "Company rate cannot be negative"
    )
    private BigDecimal companyRate;

    @NotNull(message = "Owner rate is required")
    @DecimalMin(
            value = "0.00",
            inclusive = true,
            message = "Owner rate cannot be negative"
    )
    private BigDecimal ownerRate;

    public OwnerDestinationRateRequest() {
    }

    public Long getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(Long ownerId) {
        this.ownerId = ownerId;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public Long getDestinationId() {
        return destinationId;
    }

    public void setDestinationId(Long destinationId) {
        this.destinationId = destinationId;
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
}