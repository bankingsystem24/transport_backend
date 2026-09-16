package transport_backend.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import transport_backend.entity.OwnerMaster;

public interface OwnerMasterRepository extends JpaRepository<OwnerMaster, Long> {

    Optional<OwnerMaster> findByOwnerNameIgnoreCase(String ownerName);

    boolean existsByOwnerNameIgnoreCase(String ownerName);

}