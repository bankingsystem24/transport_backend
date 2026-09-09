package transport_backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import transport_backend.entity.OwnerMaster;

public interface OwnerMasterRepository extends JpaRepository<OwnerMaster, Long> {

}