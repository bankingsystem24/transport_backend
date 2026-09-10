package transport_backend.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import transport_backend.dto.VehicleMasterRequest;
import transport_backend.dto.VehicleMasterResponse;
import transport_backend.service.VehicleMasterService;

@RestController
@RequestMapping("/api/vehicle-master")
public class VehicleMasterController {

    private final VehicleMasterService vehicleMasterService;

    public VehicleMasterController(
            VehicleMasterService vehicleMasterService) {

        this.vehicleMasterService = vehicleMasterService;
    }

    // CREATE
    @PostMapping
    public ResponseEntity<VehicleMasterResponse> create(
            @RequestBody VehicleMasterRequest request) {

        return ResponseEntity.ok(
                vehicleMasterService.create(request));
    }

    // GET ALL
    @GetMapping
    public ResponseEntity<List<VehicleMasterResponse>> getAll() {

        return ResponseEntity.ok(
                vehicleMasterService.getAll());
    }

    // GET BY ID
    @GetMapping("/{id}")
    public ResponseEntity<VehicleMasterResponse> getById(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                vehicleMasterService.getById(id));
    }

    // GET BY OWNER
    @GetMapping("/owner/{ownerId}")
    public ResponseEntity<List<VehicleMasterResponse>> getByOwnerId(
            @PathVariable Long ownerId) {

        return ResponseEntity.ok(
                vehicleMasterService.getByOwnerId(ownerId));
    }

    // UPDATE
    @PutMapping("/{id}")
    public ResponseEntity<VehicleMasterResponse> update(
            @PathVariable Long id,
            @RequestBody VehicleMasterRequest request) {

        return ResponseEntity.ok(
                vehicleMasterService.update(id, request));
    }

    // DELETE
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @PathVariable Long id) {

        vehicleMasterService.delete(id);

        return ResponseEntity.noContent().build();
    }
}