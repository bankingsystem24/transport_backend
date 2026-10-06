package transport_backend.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class MaxDestinationDieselQtyResponse {

    private Long id;

    private Long companyId;
    private String companyName;

    private Long destinationId;
    private String destinationName;

    private BigDecimal maxDieselQty;

    private Long createdBy;

    private LocalDateTime createdDate;
    private LocalDateTime updatedDate;
}
