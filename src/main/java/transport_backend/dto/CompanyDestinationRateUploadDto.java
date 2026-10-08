package transport_backend.dto;

import java.math.BigDecimal;

public class CompanyDestinationRateUploadDto {

    private Long destinationId;
    private String destination;
    private Long productId;
    private String product;
    private BigDecimal companyRate;
    private BigDecimal revisedRate;

    public CompanyDestinationRateUploadDto() {
    }

    public Long getDestinationId() {
        return destinationId;
    }

    public void setDestinationId(Long destinationId) {
        this.destinationId = destinationId;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public String getProduct() {
        return product;
    }

    public void setProduct(String product) {
        this.product = product;
    }

    public BigDecimal getCompanyRate() {
        return companyRate;
    }

    public void setCompanyRate(BigDecimal companyRate) {
        this.companyRate = companyRate;
    }

    public BigDecimal getRevisedRate() {
        return revisedRate;
    }

    public void setRevisedRate(BigDecimal revisedRate) {
        this.revisedRate = revisedRate;
    }
}