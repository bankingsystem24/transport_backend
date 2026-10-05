package transport_backend.dto;

import java.time.LocalDate;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public class CompanyDestinationRateRevisionRequest {

    @NotNull(message = "Effective date is required")
    @JsonFormat(pattern = "dd-MM-yyyy")
    private LocalDate effectiveDate;

    @NotEmpty(message = "Rates are required")
    private List<CompanyDestinationRateRevisionItem> rates;

    public LocalDate getEffectiveDate() {
        return effectiveDate;
    }

    public void setEffectiveDate(LocalDate effectiveDate) {
        this.effectiveDate = effectiveDate;
    }

    public List<CompanyDestinationRateRevisionItem> getRates() {
        return rates;
    }

    public void setRates(List<CompanyDestinationRateRevisionItem> rates) {
        this.rates = rates;
    }
}
    

