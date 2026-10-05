package transport_backend.dto;

import java.math.BigDecimal;
import jakarta.validation.constraints.NotNull;

public class CompanyDestinationRateRevisionItem {

    @NotNull(message = "Product is required")
    private Long productId;

    @NotNull(message = "Destination is required")
    private Long destinationId;

    @NotNull(message = "Company rate is required")
    private BigDecimal companyRate;

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

    public BigDecimal getCompanyRate() {
        return companyRate;
    }

    public void setCompanyRate(BigDecimal companyRate) {
        this.companyRate = companyRate;
    }
}
