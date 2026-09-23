package transport_backend.repository;

import transport_backend.entity.PaymentTransaction;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentTransactionRepository
        extends JpaRepository<PaymentTransaction, Long> {

            List<PaymentTransaction> findByOwner_IdAndPaymentMonthBetween(
            Long ownerId,
            LocalDate fromDate,
            LocalDate toDate
    );
}