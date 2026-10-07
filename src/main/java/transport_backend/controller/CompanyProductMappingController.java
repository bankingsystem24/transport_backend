package transport_backend.controller;

import lombok.RequiredArgsConstructor;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import transport_backend.dto.CompanyProductMappingRequest;
import transport_backend.entity.CompanyProductMapping;
import transport_backend.security.JwtAuthenticationDetails;
import transport_backend.service.CompanyProductMappingService;

@RestController
@RequestMapping("/api/company-product")
@RequiredArgsConstructor
public class CompanyProductMappingController {

    private final CompanyProductMappingService service;

    @PostMapping                       //For create and Update
    public ResponseEntity<CompanyProductMapping> saveOrUpdate(
            @RequestBody CompanyProductMappingRequest request,
            Authentication authentication) {

        JwtAuthenticationDetails details =
                (JwtAuthenticationDetails) authentication.getDetails();

        Long companyId = details.getCompanyId();

        CompanyProductMapping result =
                service.saveOrUpdate(request, companyId);

        return ResponseEntity.ok(result);
    }

   @GetMapping
    public ResponseEntity<List<CompanyProductMapping>> getAll(
            Authentication authentication) {

        JwtAuthenticationDetails details =
                (JwtAuthenticationDetails) authentication.getDetails();

        Long companyId = details.getCompanyId();

        return ResponseEntity.ok(
                service.getAllByCompanyId(companyId)
        );
    }

}