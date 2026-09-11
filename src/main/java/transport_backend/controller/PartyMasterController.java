package transport_backend.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import transport_backend.dto.PartyMasterRequest;
import transport_backend.dto.PartyMasterResponse;
import transport_backend.service.PartyMasterService;

import java.util.List;

@RestController
@RequestMapping("/api/parties")
@CrossOrigin(origins = "*")
public class PartyMasterController {

    private final PartyMasterService partyService;

    public PartyMasterController(
            PartyMasterService partyService) {

        this.partyService = partyService;
    }

    // CREATE
    @PostMapping
    public ResponseEntity<PartyMasterResponse> create(
            @RequestBody PartyMasterRequest request) {

        PartyMasterResponse response =
                partyService.create(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    // GET ALL
    @GetMapping
    public ResponseEntity<List<PartyMasterResponse>> getAll() {

        return ResponseEntity.ok(
                partyService.getAll()
        );
    }

    // GET BY ID
    @GetMapping("/{id}")
    public ResponseEntity<PartyMasterResponse> getById(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                partyService.getById(id)
        );
    }

    // UPDATE
    @PutMapping("/{id}")
    public ResponseEntity<PartyMasterResponse> update(
            @PathVariable Long id,
            @RequestBody PartyMasterRequest request) {

        return ResponseEntity.ok(
                partyService.update(id, request)
        );
    }

    // DELETE
    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(
            @PathVariable Long id) {

        partyService.delete(id);

        return ResponseEntity.ok(
                "Party deleted successfully"
        );
    }
}