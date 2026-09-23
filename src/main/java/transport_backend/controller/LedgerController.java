package transport_backend.controller;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import transport_backend.dto.LedgerBilledVehicleResponse;
import transport_backend.dto.LedgerPaymentResponse;
import transport_backend.service.LedgerService;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/ledger")
public class LedgerController {

    private final LedgerService ledgerService;

    public LedgerController(LedgerService ledgerService) {
        this.ledgerService = ledgerService;
    }

    @GetMapping("/billed")
    public ResponseEntity<List<LedgerBilledVehicleResponse>> getLedgerBilled(
            @RequestParam Long ownerId,

            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fromDate,

            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate toDate) {

        List<LedgerBilledVehicleResponse> response = ledgerService.getLedgerBilled(
                ownerId,
                fromDate,
                toDate);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/payments")
    public ResponseEntity<List<LedgerPaymentResponse>> getLedgerPayments(
            @RequestParam Long ownerId,

            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fromDate,

            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate toDate) {

        List<LedgerPaymentResponse> response = ledgerService.getLedgerPayments(
                ownerId,
                fromDate,
                toDate);

        return ResponseEntity.ok(response);
    }
}