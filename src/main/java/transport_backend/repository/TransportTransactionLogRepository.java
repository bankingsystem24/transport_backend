package transport_backend.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import transport_backend.entity.TransportTransactionLog;

public interface TransportTransactionLogRepository
        extends JpaRepository<TransportTransactionLog, Long> {

    List<TransportTransactionLog> findByTransactionIdOrderByChangedDateDesc(Long transactionId);
}