package transport_backend.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class OwnerDestinationRateRevisionItem {

    private Long ownerId;

    private Long productId;

    private Long destinationId;

    private BigDecimal companyRate;

    private BigDecimal ownerRate;
}
