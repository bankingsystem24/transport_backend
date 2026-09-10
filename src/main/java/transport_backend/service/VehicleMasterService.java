package transport_backend.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import transport_backend.dto.VehicleMasterRequest;
import transport_backend.dto.VehicleMasterResponse;
import transport_backend.entity.OwnerMaster;
import transport_backend.entity.User;
import transport_backend.entity.VehicleMaster;
import transport_backend.repository.OwnerMasterRepository;
import transport_backend.repository.UserRepository;
import transport_backend.repository.VehicleMasterRepository;

@Service
@Transactional
public class VehicleMasterService {

    private final VehicleMasterRepository vehicleMasterRepository;
    private final OwnerMasterRepository ownerMasterRepository;
    private final UserRepository userRepository;

    public VehicleMasterService(
            VehicleMasterRepository vehicleMasterRepository,
            OwnerMasterRepository ownerMasterRepository,
            UserRepository userRepository) {

        this.vehicleMasterRepository = vehicleMasterRepository;
        this.ownerMasterRepository = ownerMasterRepository;
        this.userRepository = userRepository;
    }

    // CREATE
    public VehicleMasterResponse create(VehicleMasterRequest request) {

        if (vehicleMasterRepository
                .existsByVehicleNumber(request.getVehicleNumber())) {

            throw new RuntimeException("Vehicle number already exists");
        }

        OwnerMaster owner = ownerMasterRepository
                .findById(request.getOwnerId())
                .orElseThrow(() ->
                        new RuntimeException("Owner not found"));

        User createdBy = userRepository
                .findById(request.getCreatedBy())
                .orElseThrow(() ->
                        new RuntimeException("User not found"));

        VehicleMaster vehicle = new VehicleMaster();

        vehicle.setVehicleNumber(request.getVehicleNumber());
        vehicle.setOwner(owner);
        vehicle.setCreatedBy(createdBy);
        vehicle.setCreatedDate(LocalDateTime.now());

        VehicleMaster saved =
                vehicleMasterRepository.save(vehicle);

        return toResponse(saved);
    }

    // GET ALL
    @Transactional(readOnly = true)
    public List<VehicleMasterResponse> getAll() {

        return vehicleMasterRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    // GET BY ID
    @Transactional(readOnly = true)
    public VehicleMasterResponse getById(Long id) {

        VehicleMaster vehicle = vehicleMasterRepository
                .findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Vehicle not found"));

        return toResponse(vehicle);
    }

    // GET BY OWNER
    @Transactional(readOnly = true)
    public List<VehicleMasterResponse> getByOwnerId(Long ownerId) {

        return vehicleMasterRepository
                .findByOwnerId(ownerId)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    // UPDATE
    public VehicleMasterResponse update(
            Long id,
            VehicleMasterRequest request) {

        VehicleMaster vehicle = vehicleMasterRepository
                .findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Vehicle not found"));

        if (!vehicle.getVehicleNumber()
                .equals(request.getVehicleNumber())
                && vehicleMasterRepository
                        .existsByVehicleNumber(
                                request.getVehicleNumber())) {

            throw new RuntimeException(
                    "Vehicle number already exists");
        }

        OwnerMaster owner = ownerMasterRepository
                .findById(request.getOwnerId())
                .orElseThrow(() ->
                        new RuntimeException("Owner not found"));

        vehicle.setVehicleNumber(
                request.getVehicleNumber());

        vehicle.setOwner(owner);

        // createdBy and createdDate are not changed

        return toResponse(
                vehicleMasterRepository.save(vehicle));
    }

    // DELETE
    public void delete(Long id) {

        VehicleMaster vehicle = vehicleMasterRepository
                .findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Vehicle not found"));

        vehicleMasterRepository.delete(vehicle);
    }

    // ENTITY TO RESPONSE
    private VehicleMasterResponse toResponse(
            VehicleMaster vehicle) {

        VehicleMasterResponse response =
                new VehicleMasterResponse();

        response.setId(vehicle.getId());
        response.setVehicleNumber(
                vehicle.getVehicleNumber());

        if (vehicle.getOwner() != null) {
            response.setOwnerId(
                    vehicle.getOwner().getId());

            response.setOwnerName(
                    vehicle.getOwner().getOwnerName());
        }

        if (vehicle.getCreatedBy() != null) {
            response.setCreatedBy(
                    vehicle.getCreatedBy().getId());

            response.setCreatedByName(
                    vehicle.getCreatedBy().getName());
        }

        response.setCreatedDate(
                vehicle.getCreatedDate());

        return response;
    }
}