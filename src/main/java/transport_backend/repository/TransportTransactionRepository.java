package transport_backend.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import transport_backend.dto.DashboardTransactionResponse;
import transport_backend.entity.TransportTransaction;

public interface TransportTransactionRepository
                extends JpaRepository<TransportTransaction, Long> {

        // Date only
        List<TransportTransaction> findByDiDateBetweenAndCompany_Id(
                        LocalDate fromDate,
                        LocalDate toDate,
                        Long companyId);

        // Owner only
        List<TransportTransaction> findByDiDateBetweenAndOwner_IdAndCompany_Id(
                        LocalDate fromDate,
                        LocalDate toDate,
                        Long ownerId,
                        Long companyId);

        // Vehicle only
        List<TransportTransaction> findByDiDateBetweenAndVehicle_IdAndCompany_Id(
                        LocalDate fromDate,
                        LocalDate toDate,
                        Long vehicleId,
                        Long companyId);

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

        boolean existsByCompany_IdAndDiNoAndLrNoAndInvoiceNo(
                Long companyId,
                String diNo,
                String lrNo,
                String invoiceNo
        );

        


    @Query("""
        SELECT new transport_backend.dto.DashboardTransactionResponse(
            c.id,
            c.companyName,
            p.id,
            p.productName,
            o.id,
            o.ownerName,
            t.partyName,
            d.id,
            d.destination,
            t.diDate,
            t.totalAmount,
            t.dieselAmount,
            t.parkingCharges,
            t.advance,
            t.otherPay,
            t.tripBalance,
            t.profitAmount,
            t.ton_mt
        )
        FROM TransportTransaction t
        JOIN t.company c
        JOIN t.product p
        LEFT JOIN t.owner o
        LEFT JOIN t.destination d
        WHERE c.id = :companyId
          AND t.diDate >= :fromDate
          AND t.diDate <= :toDate
        ORDER BY t.diDate DESC
        """)
    List<DashboardTransactionResponse> findDashboardTransactions(
            @Param("companyId") Long companyId,
            @Param("fromDate") LocalDate fromDate,
            @Param("toDate") LocalDate toDate);


        @Query("""
        SELECT new transport_backend.dto.DashboardTransactionResponse(
                c.id,
                c.companyName,
                p.id,
                p.productName,
                o.id,
                o.ownerName,
                t.partyName,
                d.id,
                d.destination,
                t.diDate,
                t.totalAmount,
                t.dieselAmount,
                t.parkingCharges,
                t.advance,
                t.otherPay,
                t.tripBalance,
                t.profitAmount
        )
        FROM TransportTransaction t
        JOIN t.company c
        JOIN t.product p
        LEFT JOIN t.owner o
        LEFT JOIN t.destination d
        WHERE c.id = :companyId
        ORDER BY t.diDate DESC
        """)
        List<DashboardTransactionResponse> findDashboardTransactionsByCompanyId(
                @Param("companyId") Long companyId);
                
}

