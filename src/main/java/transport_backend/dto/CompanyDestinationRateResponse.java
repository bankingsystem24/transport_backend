package transport_backend.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public class CompanyDestinationRateResponse {

    private Long id;

    private Long destinationId;
    private String destinationName;

    private Long productId;
    private String productName;

    private LocalDate fromDate;
    private LocalDate toDate;

    private BigDecimal companyRate;

    public CompanyDestinationRateResponse() {
    }

    public CompanyDestinationRateResponse(
            Long id,
            Long destinationId,
            String destinationName,
            Long productId,
            String productName,
            LocalDate fromDate,
            LocalDate toDate,
            BigDecimal companyRate) {

        this.id = id;
        this.destinationId = destinationId;
        this.destinationName = destinationName;
        this.productId = productId;
        this.productName = productName;
        this.fromDate = fromDate;
        this.toDate = toDate;
        this.companyRate = companyRate;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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
}