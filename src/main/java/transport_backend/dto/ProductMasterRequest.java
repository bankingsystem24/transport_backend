package transport_backend.dto;

import jakarta.validation.constraints.NotBlank;

public class ProductMasterRequest {

    @NotBlank(message = "Product name is required")
    private String productName;

    private Long createdBy;

    public ProductMasterRequest() {
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public Long getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(Long createdBy) {
        this.createdBy = createdBy;
    }
}