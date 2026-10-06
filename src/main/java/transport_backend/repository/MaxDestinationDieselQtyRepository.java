package transport_backend.repository;

import transport_backend.entity.MaxDestinationDieselQty;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MaxDestinationDieselQtyRepository
        extends JpaRepository<MaxDestinationDieselQty, Long> {

    boolean existsByCompany_IdAndDestination_Id(
            Long companyId,
            Long destinationId
    );

    Optional<MaxDestinationDieselQty>
    findByCompany_IdAndDestination_Id(
            Long companyId,
            Long destinationId
    );
}
