package transport_backend.controller;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import transport_backend.dto.OwnerDestinationRateRequest;
import transport_backend.dto.OwnerDestinationRateResponse;
import transport_backend.service.OwnerDestinationRateService;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/owner-destination-rates")
public class OwnerDestinationRateController {

    private final OwnerDestinationRateService rateService;

    public OwnerDestinationRateController(
            OwnerDestinationRateService rateService) {

        this.rateService = rateService;
    }

    // =========================================================
    // CREATE
    // =========================================================

    @PostMapping
    public ResponseEntity<OwnerDestinationRateResponse> create(
            @Valid @RequestBody OwnerDestinationRateRequest request) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(rateService.create(request));
    }

    // =========================================================
    // GET ALL
    // =========================================================

    @GetMapping
    public ResponseEntity<List<OwnerDestinationRateResponse>> getAll() {

        return ResponseEntity.ok(
                rateService.getAll()
        );
    }

    // =========================================================
    // GET BY ID
    // =========================================================

    @GetMapping("/{id}")
    public ResponseEntity<OwnerDestinationRateResponse> getById(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                rateService.getById(id)
        );
    }

    // =========================================================
    // UPDATE
    // =========================================================

    @PutMapping("/{id}")
    public ResponseEntity<OwnerDestinationRateResponse> update(
            @PathVariable Long id,
            @Valid @RequestBody OwnerDestinationRateRequest request) {

        return ResponseEntity.ok(
                rateService.update(id, request)
        );
    }

    // =========================================================
    // DELETE
    // =========================================================

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(
            @PathVariable Long id) {

        rateService.delete(id);

        return ResponseEntity.ok(
                Map.of(
                        "message",
                        "Owner destination rate deleted successfully"
                )
        );
    }
}