package transport_backend.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(
    name = "max_destination_diesel_qty",
    uniqueConstraints = {
        @UniqueConstraint(
            name = "uk_company_destination_diesel",
            columnNames = {"company_id", "destination_id"}
        )
    }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MaxDestinationDieselQty {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Company is required")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
        name = "company_id",
        nullable = false
    )
    private CompanyMaster company;

    @NotNull(message = "Destination is required")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
        name = "destination_id",
        nullable = false
    )
    private DestinationMaster destination;

    @NotNull(message = "Maximum diesel quantity is required")
    @DecimalMin(
        value = "0.01",
        message = "Maximum diesel quantity must be greater than 0"
    )
    @Column(
        name = "max_diesel_qty",
        precision = 10,
        scale = 2,
        nullable = false
    )
    private BigDecimal maxDieselQty;

    @Column(name = "created_by")
    private Long createdBy;

    @Column(
        name = "created_date",
        nullable = false,
        updatable = false
    )
    private LocalDateTime createdDate;

    @Column(name = "updated_date")
    private LocalDateTime updatedDate;
}
