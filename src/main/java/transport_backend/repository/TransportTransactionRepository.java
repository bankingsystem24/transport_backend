package transport_backend.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import transport_backend.entity.TransportTransaction;

public interface TransportTransactionRepository
        extends JpaRepository<TransportTransaction, Long> {

    // Date only
    List<TransportTransaction> findByDiDateBetween(
            LocalDate fromDate,
            LocalDate toDate);

    // Owner only
    List<TransportTransaction> findByDiDateBetweenAndOwner_Id(
            LocalDate fromDate,
            LocalDate toDate,
            Long ownerId);

    // Vehicle only
    List<TransportTransaction> findByDiDateBetweenAndVehicle_Id(
            LocalDate fromDate,
            LocalDate toDate,
            Long vehicleId);
}