package transport_backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import transport_backend.entity.DestinationMaster;

import java.util.Optional;

public interface DestinationMasterRepository
        extends JpaRepository<DestinationMaster, Long> {

    Optional<DestinationMaster> findByDestinationIgnoreCase(String destination);

    boolean existsByDestinationIgnoreCase(String destination);
}