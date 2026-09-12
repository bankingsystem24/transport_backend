package transport_backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import transport_backend.entity.TransportTransaction;

public interface TransportTransactionRepository
        extends JpaRepository<TransportTransaction, Long> {
}