package transport_backend.controller;

import jakarta.validation.Valid;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import transport_backend.dto.OwnerDestinationRateRequest;
import transport_backend.dto.OwnerDestinationRateResponse;
import transport_backend.dto.OwnerDestinationRateRevisionRequest;
import transport_backend.dto.OwnerRateResponse;
import transport_backend.security.JwtAuthenticationDetails;
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

        private Long getCompanyId(Authentication authentication) {
        JwtAuthenticationDetails details =
                (JwtAuthenticationDetails) authentication.getDetails();

        return details.getCompanyId();
    }


    // =========================================================
    // CREATE
    // =========================================================

    @PostMapping
    public ResponseEntity<OwnerDestinationRateResponse> create(
            @Valid @RequestBody OwnerDestinationRateRequest request,
            Authentication authentication) {

        Long companyId = getCompanyId(authentication);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(rateService.create(request,companyId));
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
            @Valid @RequestBody OwnerDestinationRateRequest request,
            Authentication authentication) {

        Long companyId = getCompanyId(authentication);

        return ResponseEntity.ok(
                rateService.update(id, request,companyId)
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
                LocalDate date,Authentication authentication) 
        {

                Long companyId = getCompanyId(authentication);

                return ResponseEntity.ok(
                        rateService.getAll(date,companyId)
                );
        }

        @PostMapping(
        value = "/upload",
        consumes = "multipart/form-data"
        )
        public ResponseEntity<Map<String, Object>> uploadExcel(
                @RequestParam("file") MultipartFile file,
                @RequestParam("wefDate")
                @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
                LocalDate wefDate,Authentication authentication) {

                JwtAuthenticationDetails details =
                (JwtAuthenticationDetails) authentication.getDetails();
                                 Long companyId = details.getCompanyId();

                int count = rateService.uploadExcel(file, wefDate, companyId);

                return ResponseEntity.ok(Map.of(
                        "message", "Owner destination rates uploaded successfully",
                        "recordsInserted", count,
                        "wefDate", wefDate,
                        "toDate", wefDate.plusYears(10)
                ));
        }

        @GetMapping("/rate")
        public ResponseEntity<OwnerRateResponse> getOwnerRate(
                @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
                @RequestParam Long ownerId,
                @RequestParam Long productId,
                @RequestParam Long destinationId,
                Authentication authentication) {

        Long companyId = getCompanyId(authentication);

        return rateService
                .getOwnerRate(date, companyId, ownerId, productId, destinationId)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
        }

}