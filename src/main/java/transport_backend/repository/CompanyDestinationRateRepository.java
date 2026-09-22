package transport_backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import transport_backend.entity.CompanyDestinationRate;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

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


    Optional<CompanyDestinationRate> findByProduct_IdAndDestination_IdAndFromDateLessThanEqualAndToDateGreaterThanEqual(
        Long productId,
        Long destinationId,
        LocalDate fromDate,
        LocalDate fromDate2);
}