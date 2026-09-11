package transport_backend.controller;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import transport_backend.dto.CompanyDestinationRateRequest;
import transport_backend.dto.CompanyDestinationRateResponse;
import transport_backend.service.CompanyDestinationRateService;

import java.util.List;

@RestController
@RequestMapping("/api/company-destination-rates")
public class CompanyDestinationRateController {

    private final CompanyDestinationRateService rateService;

    public CompanyDestinationRateController(
            CompanyDestinationRateService rateService) {

        this.rateService = rateService;
    }

    // =========================================================
    // CREATE
    // =========================================================

    @PostMapping
    public ResponseEntity<CompanyDestinationRateResponse> create(
            @Valid @RequestBody CompanyDestinationRateRequest request) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(rateService.create(request));
    }

    // =========================================================
    // GET ALL
    // =========================================================

    @GetMapping
    public ResponseEntity<List<CompanyDestinationRateResponse>> getAll() {

        return ResponseEntity.ok(
                rateService.getAll()
        );
    }

    // =========================================================
    // GET BY ID
    // =========================================================

    @GetMapping("/{id}")
    public ResponseEntity<CompanyDestinationRateResponse> getById(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                rateService.getById(id)
        );
    }

    // =========================================================
    // UPDATE
    // =========================================================

    @PutMapping("/{id}")
    public ResponseEntity<CompanyDestinationRateResponse> update(
            @PathVariable Long id,
            @Valid @RequestBody CompanyDestinationRateRequest request) {

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
                java.util.Map.of(
                        "message",
                        "Company destination rate deleted successfully"
                )
        );
    }
}