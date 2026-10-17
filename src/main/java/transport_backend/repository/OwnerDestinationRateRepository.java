package transport_backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import transport_backend.entity.CompanyDestinationRate;
import transport_backend.entity.OwnerDestinationRate;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface OwnerDestinationRateRepository
        extends JpaRepository<OwnerDestinationRate, Long> {

        Optional<OwnerDestinationRate> findByIdAndCompany_Id(
        Long id,
        Long companyId
);

    @Query("""
        SELECT r
        FROM OwnerDestinationRate r
        WHERE r.owner.id = :ownerId
          AND r.product.id = :productId
          AND r.destination.id = :destinationId
          AND r.fromDate <= :toDate
          AND r.toDate >= :fromDate
          AND r.company.id = :companyId
    """)
    List<OwnerDestinationRate> findOverlappingRates(
            @Param("ownerId") Long ownerId,
            @Param("productId") Long productId,
            @Param("destinationId") Long destinationId,
            @Param("fromDate") LocalDate fromDate,
            @Param("toDate") LocalDate toDate,
            @Param("companyId") Long companyId
    );

    @Query("""
        SELECT r
        FROM OwnerDestinationRate r
        WHERE r.id <> :id
          AND r.owner.id = :ownerId
          AND r.product.id = :productId
          AND r.destination.id = :destinationId
          AND r.fromDate <= :toDate
          AND r.toDate >= :fromDate
          AND r.company.id = :companyId
    """)
    List<OwnerDestinationRate> findOverlappingRatesForUpdate(
            @Param("id") Long id,
            @Param("ownerId") Long ownerId,
            @Param("productId") Long productId,
            @Param("destinationId") Long destinationId,
            @Param("fromDate") LocalDate fromDate,
            @Param("toDate") LocalDate toDate,
            @Param("companyId") Long companyId
    );

    boolean existsByOwner_IdAndProduct_IdAndDestination_IdAndFromDate(
                Long ownerId,
                Long productId,
                Long destinationId,
                LocalDate fromDate
        );

        @Query("""
                SELECT r
                FROM OwnerDestinationRate r
                WHERE r.owner.id = :ownerId
                AND r.product.id = :productId
                AND r.destination.id = :destinationId
                AND r.fromDate <= :effectiveDate
                AND r.toDate >= :effectiveDate
                """)
                Optional<OwnerDestinationRate> findApplicableRate(
                        @Param("ownerId") Long ownerId,
                        @Param("productId") Long productId,
                        @Param("destinationId") Long destinationId,
                        @Param("effectiveDate") LocalDate effectiveDate
                );
        
        @Query("""
                SELECT r
                FROM CompanyDestinationRate r
                WHERE r.product.id = :productId
                AND r.destination.id = :destinationId
                AND r.fromDate <= :effectiveDate
                AND r.toDate >= :effectiveDate
                """)
                Optional<CompanyDestinationRate> findApplicableRate(
                        @Param("productId") Long productId,
                        @Param("destinationId") Long destinationId,
                        @Param("effectiveDate") LocalDate effectiveDate
        );

                @Query("""
                SELECT r
                FROM OwnerDestinationRate r
                WHERE r.company.id = :companyId
                AND :date BETWEEN r.fromDate AND r.toDate
                """)
                List<OwnerDestinationRate> findRatesForDateAndCompany(
                        @Param("date") LocalDate date,
                        @Param("companyId") Long companyId);

            Optional<CompanyDestinationRate>
                findFirstByProduct_IdAndDestination_IdAndFromDateLessThanEqualAndToDateGreaterThanEqual(
            Long productId,
            Long destinationId,
            LocalDate fromDate,
            LocalDate toDate
    );
}