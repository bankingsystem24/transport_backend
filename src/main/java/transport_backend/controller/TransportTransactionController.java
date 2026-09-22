package transport_backend.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import transport_backend.dto.TransportTransactionReportResponse;
import transport_backend.dto.TransportTransactionRequest;
import transport_backend.dto.TransportTransactionResponse;
import transport_backend.service.TransportTransactionService;

@RestController
@RequestMapping("/api/transport-transactions")
public class TransportTransactionController {

        private final TransportTransactionService transportTransactionService;

        public TransportTransactionController(
                        TransportTransactionService transportTransactionService) {

                this.transportTransactionService = transportTransactionService;
        }

        // CREATE
        @PostMapping
        public ResponseEntity<TransportTransactionResponse> create(
                        @Valid @RequestBody TransportTransactionRequest request) {

                return ResponseEntity
                                .status(HttpStatus.CREATED)
                                .body(transportTransactionService.create(request));
        }

        // GET ALL
        @GetMapping
        public ResponseEntity<List<TransportTransactionResponse>> getAll() {

                return ResponseEntity.ok(
                                transportTransactionService.getAll());
        }

        // GET BY ID
        @GetMapping("/{id}")
        public ResponseEntity<TransportTransactionResponse> getById(
                        @PathVariable Long id) {

                return ResponseEntity.ok(
                                transportTransactionService.getById(id));
        }

        // UPDATE
        @PutMapping("/{id}")
        public ResponseEntity<TransportTransactionResponse> update(
                        @PathVariable Long id,
                        @Valid @RequestBody TransportTransactionRequest request) {

                return ResponseEntity.ok(
                                transportTransactionService.update(id, request));
        }

        // DELETE
        @DeleteMapping("/{id}")
        public ResponseEntity<Void> delete(
                        @PathVariable Long id) {

                transportTransactionService.delete(id);

                return ResponseEntity.noContent().build();
        }

        @GetMapping("/report")
        public ResponseEntity<List<TransportTransactionReportResponse>> getReport(
                        @RequestParam LocalDate fromDate,
                        @RequestParam LocalDate toDate,
                        @RequestParam(required = false, defaultValue = "false") Boolean owner,
                        @RequestParam(required = false) String ownerId,
                        @RequestParam(required = false, defaultValue = "false") Boolean vehicle,
                        @RequestParam(required = false) String vehicleId) {

                Long parsedOwnerId = null;
                Long parsedVehicleId = null;

                if (ownerId != null && !ownerId.equalsIgnoreCase("null")
                                && !ownerId.isBlank()) {
                        parsedOwnerId = Long.valueOf(ownerId);
                }

                if (vehicleId != null && !vehicleId.equalsIgnoreCase("null")
                                && !vehicleId.isBlank()) {
                        parsedVehicleId = Long.valueOf(vehicleId);
                }

                List<TransportTransactionReportResponse> transactions = transportTransactionService.findReport(
                                fromDate,
                                toDate,
                                owner,
                                parsedOwnerId,
                                vehicle,
                                parsedVehicleId);

                return ResponseEntity.ok(transactions);
        }

}