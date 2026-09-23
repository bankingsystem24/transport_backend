package transport_backend.repository;

import transport_backend.entity.PaymentTransaction;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PaymentTransactionRepository
        extends JpaRepository<PaymentTransaction, Long> {

            List<PaymentTransaction> findByOwner_IdAndPaymentMonthBetween(
            Long ownerId,
            LocalDate fromDate,
            LocalDate toDate
    );

    @Query("""
        SELECT p
        FROM PaymentTransaction p
        LEFT JOIN FETCH p.owner
        WHERE p.paymentMonth BETWEEN :fromDate AND :toDate
        ORDER BY p.owner.id
    """)
    List<PaymentTransaction> findBusinessStatusPayments(
            @Param("fromDate") LocalDate fromDate,
            @Param("toDate") LocalDate toDate
    );
}