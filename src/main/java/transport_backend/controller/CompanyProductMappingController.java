package transport_backend.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import transport_backend.dto.CompanyProductMappingRequest;
import transport_backend.entity.CompanyProductMapping;
import transport_backend.service.CompanyProductMappingService;

@RestController
@RequestMapping("/api/company-product")
@RequiredArgsConstructor
public class CompanyProductMappingController {

    private final CompanyProductMappingService service;

    @PostMapping
    public ResponseEntity<CompanyProductMapping> saveOrUpdate(
            @RequestBody CompanyProductMappingRequest request) {

        CompanyProductMapping result =
                service.saveOrUpdate(request);

        return ResponseEntity.ok(result);
    }
}