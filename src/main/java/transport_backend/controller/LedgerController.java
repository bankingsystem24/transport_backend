package transport_backend.controller;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import transport_backend.dto.BusinessStatusResponse;
import transport_backend.dto.LedgerBilledVehicleResponse;
import transport_backend.dto.LedgerPaymentResponse;
import transport_backend.security.JwtAuthenticationDetails;
import transport_backend.service.LedgerService;
import org.springframework.security.core.Authentication;
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
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate toDate,
            Authentication authentication
            ) {
        JwtAuthenticationDetails details = (JwtAuthenticationDetails) authentication.getDetails();
        Long companyId = details.getCompanyId();

        List<LedgerBilledVehicleResponse> response = ledgerService.getLedgerBilled(
                ownerId,
                companyId,
                fromDate,
                toDate);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/payments")
    public ResponseEntity<List<LedgerPaymentResponse>> getLedgerPayments(
            @RequestParam Long ownerId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fromDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate toDate,
            Authentication authentication) {
        JwtAuthenticationDetails details = (JwtAuthenticationDetails) authentication.getDetails();
        Long companyId = details.getCompanyId();
        List<LedgerPaymentResponse> response = ledgerService.getLedgerPayments(
                ownerId,
                companyId,
                fromDate,
                toDate);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/business-status")
    public ResponseEntity<List<BusinessStatusResponse>> getBusinessStatus(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fromDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate toDate,
            Authentication authentication) {
                JwtAuthenticationDetails details = (JwtAuthenticationDetails) authentication.getDetails();
                Long companyId = details.getCompanyId();

            if (companyId == null) {
                throw new RuntimeException("Company ID not found in JWT");
    }
        List<BusinessStatusResponse> response = ledgerService.getBusinessStatus(
                companyId,
                fromDate,
                toDate);

        return ResponseEntity.ok(response);
    }

}