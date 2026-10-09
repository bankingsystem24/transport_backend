package transport_backend.controller;

import jakarta.validation.Valid;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import transport_backend.dto.OwnerDestinationRateRequest;
import transport_backend.dto.OwnerDestinationRateResponse;
import transport_backend.dto.OwnerDestinationRateRevisionRequest;
import transport_backend.service.OwnerDestinationRateService;

import java.time.LocalDate;
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

    @GetMapping("/{id}")
    public ResponseEntity<OwnerDestinationRateResponse> getById(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                rateService.getById(id)
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<OwnerDestinationRateResponse> update(
            @PathVariable Long id,
            @Valid @RequestBody OwnerDestinationRateRequest request) {

        return ResponseEntity.ok(
                rateService.update(id, request)
        );
    }
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

        @PostMapping("/revise")
        public ResponseEntity<?> reviseRates(
                @Valid @RequestBody OwnerDestinationRateRevisionRequest request) {

                rateService.reviseRates(request);

                return ResponseEntity.ok(
                        Map.of(
                                "message",
                                "Owner destination rates updated successfully"
                        )
                );
        }

        @GetMapping
        public ResponseEntity<List<OwnerDestinationRateResponse>> getAll(
                @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
                LocalDate date) {

        return ResponseEntity.ok(
                rateService.getAll(date)
        );
        }

}