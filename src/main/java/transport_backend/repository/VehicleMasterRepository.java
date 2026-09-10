package transport_backend.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import transport_backend.entity.VehicleMaster;

public interface VehicleMasterRepository
        extends JpaRepository<VehicleMaster, Long> {

    boolean existsByVehicleNumber(String vehicleNumber);

    Optional<VehicleMaster> findByVehicleNumber(String vehicleNumber);

    List<VehicleMaster> findByOwnerId(Long ownerId);
}