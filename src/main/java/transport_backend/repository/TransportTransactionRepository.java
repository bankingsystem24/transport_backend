package transport_backend.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

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

        @Query("""
                            SELECT t
                            FROM TransportTransaction t
                            LEFT JOIN FETCH t.vehicle
                            WHERE t.owner.id = :ownerId
                              AND t.diDate BETWEEN :fromDate AND :toDate AND t.company.id = :companyId
                            ORDER BY t.vehicleName
                        """)
        List<TransportTransaction> findLedgerBilledTransactions(
                        @Param("ownerId") Long ownerId,
                        @Param("companyId") Long companyId,
                        @Param("fromDate") LocalDate fromDate,
                        @Param("toDate") LocalDate toDate);

        @Query("""
                        SELECT t
                        FROM TransportTransaction t
                        LEFT JOIN FETCH t.owner
                        WHERE t.diDate BETWEEN :fromDate AND :toDate
                        AND t.owner IS NOT NULL AND t.company.id = :companyId
                        ORDER BY t.owner.id
                        """)
        List<TransportTransaction> findBusinessStatusTransactions(
                        @Param("companyId") Long companyId,
                        @Param("fromDate") LocalDate fromDate,
                        @Param("toDate") LocalDate toDate);

        List<TransportTransaction> findByCompany_Id(Long companyId);
}