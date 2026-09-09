package transport_backend.controller;

import transport_backend.entity.CompanyMaster;
import transport_backend.service.CompanyMasterService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/company-master")
@CrossOrigin
public class CompanyMasterController {

    private final CompanyMasterService service;

    public CompanyMasterController(CompanyMasterService service) {
        this.service = service;
    }

    // GET ALL
    @GetMapping
    public ResponseEntity<List<CompanyMaster>> getAll() {
        return ResponseEntity.ok(service.getAll());
    }

    // GET BY ID
    @GetMapping("/{id}")
    public ResponseEntity<CompanyMaster> getById(
            @PathVariable Long id) {

        return ResponseEntity.ok(service.getById(id));
    }

    // CREATE
    @PostMapping
    public ResponseEntity<CompanyMaster> create(
            @RequestBody CompanyMaster company) {

        return ResponseEntity.ok(service.create(company));
    }

    // UPDATE
    @PutMapping("/{id}")
    public ResponseEntity<CompanyMaster> update(
            @PathVariable Long id,
            @RequestBody CompanyMaster company) {

        return ResponseEntity.ok(service.update(id, company));
    }

    // DELETE
    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(
            @PathVariable Long id) {

        service.delete(id);

        return ResponseEntity.ok("Company deleted successfully");
    }
}
