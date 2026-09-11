package transport_backend.dto;

import java.time.LocalDateTime;

public class ProductMasterResponse {

    private Long id;
    private String productName;
    private Long createdBy;
    private String createdByName;
    private LocalDateTime createdDate;

    public ProductMasterResponse() {
    }

    public ProductMasterResponse(
            Long id,
            String productName,
            Long createdBy,
            String createdByName,
            LocalDateTime createdDate) {

        this.id = id;
        this.productName = productName;
        this.createdBy = createdBy;
        this.createdByName = createdByName;
        this.createdDate = createdDate;
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(LocalDateTime createdDate) {
        this.createdDate = createdDate;
    }
    public String getCreatedByName() {
        return createdByName;
    }

    public void setCreatedByName(String createdByName) {
        this.createdByName = createdByName;
    }
}