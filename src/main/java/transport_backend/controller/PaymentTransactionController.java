package transport_backend.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

import transport_backend.dto.PaymentTransactionRequest;
import transport_backend.dto.PaymentTransactionResponse;
import transport_backend.security.JwtAuthenticationDetails;
import transport_backend.service.PaymentTransactionService;

@RestController
@RequestMapping("/api/payment-transactions")
public class PaymentTransactionController {

    private final PaymentTransactionService service;

    public PaymentTransactionController(
            PaymentTransactionService service) {

        this.service = service;
    }


    // CREATE
    @PostMapping
    public ResponseEntity<PaymentTransactionResponse> create(
            @Valid @RequestBody PaymentTransactionRequest request,
            HttpServletRequest httpRequest) {

        String authHeader = httpRequest.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new RuntimeException("Authorization token is missing");
        }

        String token = authHeader.substring(7);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(service.create(request, token));
    }



    // GET ALL
    @GetMapping
    public ResponseEntity<List<PaymentTransactionResponse>> getAll(Authentication authentication) {

        JwtAuthenticationDetails details = (JwtAuthenticationDetails) authentication.getDetails();

        Long companyId = details.getCompanyId();

        return ResponseEntity.ok(
                service.getAll(companyId)
        );
    }


    // GET BY ID
    @GetMapping("/{id}")
    public ResponseEntity<PaymentTransactionResponse> getById(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                service.getById(id)
        );
    }


    // UPDATE
    @PutMapping("/{id}")
    public ResponseEntity<PaymentTransactionResponse> update(
            @PathVariable Long id,
            @Valid @RequestBody PaymentTransactionRequest request) {

        return ResponseEntity.ok(
                service.update(id, request)
        );
    }


    // DELETE
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @PathVariable Long id) {

        service.delete(id);

        return ResponseEntity.noContent().build();
    }
}