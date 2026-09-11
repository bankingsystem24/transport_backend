package transport_backend.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import transport_backend.dto.DestinationMasterRequest;
import transport_backend.dto.DestinationMasterResponse;
import transport_backend.service.DestinationMasterService;

import java.util.List;

@RestController
@RequestMapping("/api/destinations")
@CrossOrigin(origins = "*")
public class DestinationMasterController {

    private final DestinationMasterService destinationService;

    public DestinationMasterController(
            DestinationMasterService destinationService) {

        this.destinationService = destinationService;
    }

    // CREATE
    @PostMapping
    public ResponseEntity<DestinationMasterResponse> create(
            @RequestBody DestinationMasterRequest request) {

        DestinationMasterResponse response =
                destinationService.create(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    // GET ALL
    @GetMapping
    public ResponseEntity<List<DestinationMasterResponse>> getAll() {

        return ResponseEntity.ok(
                destinationService.getAll()
        );
    }

    // GET BY ID
    @GetMapping("/{id}")
    public ResponseEntity<DestinationMasterResponse> getById(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                destinationService.getById(id)
        );
    }

    // UPDATE
    @PutMapping("/{id}")
    public ResponseEntity<DestinationMasterResponse> update(
            @PathVariable Long id,
            @RequestBody DestinationMasterRequest request) {

        return ResponseEntity.ok(
                destinationService.update(id, request)
        );
    }

    // DELETE
    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(
            @PathVariable Long id) {

        destinationService.delete(id);

        return ResponseEntity.ok(
                "Destination deleted successfully"
        );
    }
}