package transport_backend.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table( name = "payment_transactions", indexes = { @Index( name = "idx_owner_payment_month", columnList = "owner_id,payment_month" ) } )
@Getter
@Setter
public class PaymentTransaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Payment date is required")
    @Column(nullable = false)
    private LocalDate paymentDate;


    @Column(name = "payment_month", nullable = false)
    private LocalDate paymentMonth;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id", nullable = false)
    @NotNull(message = "Owner is required")
    private OwnerMaster owner;

    @Column(length = 100)
    private String bankName;

    @Column(length = 50)
    private String accountNo;

    @Column(length = 50)
    private String chequeNo;

    @NotNull(message = "Amount is required")
    @DecimalMin(value = "0.01", message = "Amount must be greater than zero")
    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal amount;

    @Column(length = 500)
    private String remarks;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by", nullable = false)
    private User createdBy;

    @Column(nullable = false, updatable = false)
    private LocalDate createdDate;

    @PrePersist
    protected void onCreate() {
        createdDate = LocalDate.now();
    }
}