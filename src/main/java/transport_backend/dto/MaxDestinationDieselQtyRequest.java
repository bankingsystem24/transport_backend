package transport_backend.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class MaxDestinationDieselQtyRequest {

    @NotNull(message = "Destination is required")
    private Long destinationId;

    @NotNull(message = "Maximum diesel quantity is required")
    @DecimalMin(
        value = "0.01",
        message = "Maximum diesel quantity must be greater than 0"
    )
    private BigDecimal maxDieselQty;
}
