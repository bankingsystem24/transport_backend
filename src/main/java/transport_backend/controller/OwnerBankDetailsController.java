package transport_backend.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import transport_backend.dto.OwnerBankDetailsDTO;
import transport_backend.service.OwnerBankDetailsService;

@RestController
@RequestMapping("/api/owner-bank-details")
public class OwnerBankDetailsController {

    private final OwnerBankDetailsService bankService;

    public OwnerBankDetailsController(
            OwnerBankDetailsService bankService) {
        this.bankService = bankService;
    }

    // CREATE
    @PostMapping
    public ResponseEntity<OwnerBankDetailsDTO> create(
            @RequestBody OwnerBankDetailsDTO dto) {

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(bankService.create(dto));
    }

    // GET ALL
    @GetMapping
    public ResponseEntity<List<OwnerBankDetailsDTO>> getAll() {

        return ResponseEntity.ok(bankService.getAll());
    }

    // GET BY BANK DETAILS ID
    @GetMapping("/{id}")
    public ResponseEntity<OwnerBankDetailsDTO> getById(
            @PathVariable Long id) {

        return ResponseEntity.ok(bankService.getById(id));
    }

    // GET ALL BANK DETAILS FOR AN OWNER
    @GetMapping("/owner/{ownerId}")
    public ResponseEntity<List<OwnerBankDetailsDTO>> getByOwnerId(
            @PathVariable Long ownerId) {

        return ResponseEntity.ok(
                bankService.getByOwnerId(ownerId));
    }

    // UPDATE
    @PutMapping("/{id}")
    public ResponseEntity<OwnerBankDetailsDTO> update(
            @PathVariable Long id,
            @RequestBody OwnerBankDetailsDTO dto) {

        return ResponseEntity.ok(
                bankService.update(id, dto));
    }

    // DELETE
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @PathVariable Long id) {

        bankService.delete(id);
        return ResponseEntity.noContent().build();
    }
}