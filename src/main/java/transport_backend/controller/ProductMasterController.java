package transport_backend.controller;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import transport_backend.dto.ProductMasterRequest;
import transport_backend.dto.ProductMasterResponse;
import transport_backend.service.ProductMasterService;

import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ProductMasterController {

    private final ProductMasterService productMasterService;

    public ProductMasterController(ProductMasterService productMasterService) {
        this.productMasterService = productMasterService;
    }

    // CREATE
    @PostMapping
    public ResponseEntity<ProductMasterResponse> create(
            @Valid @RequestBody ProductMasterRequest request) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(productMasterService.create(request));
    }

    // GET ALL
    @GetMapping
    public ResponseEntity<List<ProductMasterResponse>> getAll() {

        return ResponseEntity.ok(
                productMasterService.getAll()
        );
    }

    // GET BY ID
    @GetMapping("/{id}")
    public ResponseEntity<ProductMasterResponse> getById(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                productMasterService.getById(id)
        );
    }

    // UPDATE
    @PutMapping("/{id}")
    public ResponseEntity<ProductMasterResponse> update(
            @PathVariable Long id,
            @Valid @RequestBody ProductMasterRequest request) {

        return ResponseEntity.ok(
                productMasterService.update(id, request)
        );
    }

    // DELETE
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @PathVariable Long id) {

        productMasterService.delete(id);

        return ResponseEntity.noContent().build();
    }
}