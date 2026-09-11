package transport_backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import transport_backend.entity.CompanyDestinationRate;

import java.time.LocalDate;
import java.util.List;

public interface CompanyDestinationRateRepository
        extends JpaRepository<CompanyDestinationRate, Long> {

    /*
     * Check whether the exact destination/product/date range
     * already exists.
     */
    boolean existsByDestination_IdAndProduct_IdAndFromDateAndToDate(
            Long destinationId,
            Long productId,
            LocalDate fromDate,
            LocalDate toDate
    );

    /*
     * Find overlapping date ranges for the same
     * destination and product.
     *
     * Existing:
     * 01-09-2026 to 30-09-2026
     *
     * New:
     * 15-09-2026 to 15-10-2026
     *
     * This is an overlap.
     */
    @Query("""
        SELECT c
        FROM CompanyDestinationRate c
        WHERE c.destination.id = :destinationId
          AND c.product.id = :productId
          AND c.fromDate <= :toDate
          AND c.toDate >= :fromDate
    """)
    List<CompanyDestinationRate> findOverlappingRates(
            @Param("destinationId") Long destinationId,
            @Param("productId") Long productId,
            @Param("fromDate") LocalDate fromDate,
            @Param("toDate") LocalDate toDate
    );

    /*
     * Same overlap check, but excludes the current record.
     * Used during UPDATE.
     */
    @Query("""
        SELECT c
        FROM CompanyDestinationRate c
        WHERE c.destination.id = :destinationId
          AND c.product.id = :productId
          AND c.id <> :id
          AND c.fromDate <= :toDate
          AND c.toDate >= :fromDate
    """)
    List<CompanyDestinationRate> findOverlappingRatesForUpdate(
            @Param("id") Long id,
            @Param("destinationId") Long destinationId,
            @Param("productId") Long productId,
            @Param("fromDate") LocalDate fromDate,
            @Param("toDate") LocalDate toDate
    );
}