package transport_backend.dto;

import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class OwnerDestinationRateRevisionRequest {

    private LocalDate effectiveDate;

    private List<OwnerDestinationRateRevisionItem> rates;
}
