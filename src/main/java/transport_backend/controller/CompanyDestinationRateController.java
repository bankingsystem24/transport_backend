package transport_backend.controller;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.format.annotation.DateTimeFormat;
import transport_backend.dto.CompanyDestinationRateRequest;
import transport_backend.dto.CompanyDestinationRateResponse;
import transport_backend.dto.CompanyDestinationRateRevisionRequest;
import transport_backend.dto.CompanyDestinationRateUploadResponseDto;
import transport_backend.service.CompanyDestinationRateService;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/company-destination-rates")
public class CompanyDestinationRateController {

    private final CompanyDestinationRateService rateService;

    public CompanyDestinationRateController(
            CompanyDestinationRateService rateService) {

        this.rateService = rateService;
    }

    @PostMapping
    public ResponseEntity<CompanyDestinationRateResponse> create(
            @Valid @RequestBody CompanyDestinationRateRequest request) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(rateService.create(request));
    }

    @GetMapping
    public ResponseEntity<List<CompanyDestinationRateResponse>> getAll() {

        return ResponseEntity.ok(
                rateService.getAll()
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<CompanyDestinationRateResponse> getById(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                rateService.getById(id)
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<CompanyDestinationRateResponse> update(
            @PathVariable Long id,
            @Valid @RequestBody CompanyDestinationRateRequest request) {

        return ResponseEntity.ok(
                rateService.update(id, request)
        );
    }

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

        @GetMapping("/companyRate")
        public ResponseEntity<CompanyDestinationRateResponse> getCompanyRate(
                @RequestParam Long productId,
                @RequestParam Long destinationId,
                @RequestParam LocalDate fromDate) {

        return ResponseEntity.ok(
                rateService.getCompanyRate(
                        productId,
                        destinationId,
                        fromDate
                )
        );
        }

        @PostMapping("/revise")
        public ResponseEntity<?> reviseRates(
                @Valid @RequestBody CompanyDestinationRateRevisionRequest request) {

        rateService.reviseRates(request);

        return ResponseEntity.ok(
                Map.of(
                        "message",
                        "Company destination rates updated successfully"
                )
        );
        }

        @PostMapping(value = "/upload",consumes = "multipart/form-data")
        public ResponseEntity<CompanyDestinationRateUploadResponseDto>
        uploadRates(@RequestParam("file")
            MultipartFile file,

            @RequestParam("wefDate")
            @DateTimeFormat(pattern = "yyyy-MM-dd")
            LocalDate wefDate) {

        CompanyDestinationRateUploadResponseDto response =
                rateService.uploadRates(
                        file,
                        wefDate
                );

        return ResponseEntity.ok(response);
    }

}