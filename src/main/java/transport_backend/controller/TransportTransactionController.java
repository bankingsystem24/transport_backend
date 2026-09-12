package transport_backend.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

import transport_backend.dto.TransportTransactionRequest;
import transport_backend.dto.TransportTransactionResponse;
import transport_backend.service.TransportTransactionService;

@RestController
@RequestMapping("/api/transport-transactions")
public class TransportTransactionController {

    private final TransportTransactionService service;

    public TransportTransactionController(
            TransportTransactionService service) {

        this.service = service;
    }


    // CREATE
    @PostMapping
    public ResponseEntity<TransportTransactionResponse> create(
            @Valid @RequestBody TransportTransactionRequest request) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(service.create(request));
    }


    // GET ALL
    @GetMapping
    public ResponseEntity<List<TransportTransactionResponse>> getAll() {

        return ResponseEntity.ok(
                service.getAll()
        );
    }


    // GET BY ID
    @GetMapping("/{id}")
    public ResponseEntity<TransportTransactionResponse> getById(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                service.getById(id)
        );
    }


    // UPDATE
    @PutMapping("/{id}")
    public ResponseEntity<TransportTransactionResponse> update(
            @PathVariable Long id,
            @Valid @RequestBody TransportTransactionRequest request) {

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