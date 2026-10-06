package transport_backend.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import transport_backend.dto.MaxDestinationDieselQtyRequest;
import transport_backend.dto.MaxDestinationDieselQtyResponse;
import transport_backend.security.JwtAuthenticationDetails;
import transport_backend.service.MaxDestinationDieselQtyService;

@RestController
@RequestMapping("/api/max-destination-diesel-qty")
@RequiredArgsConstructor
public class MaxDestinationDieselQtyController {

    private final MaxDestinationDieselQtyService service;

    @PostMapping
    public ResponseEntity<MaxDestinationDieselQtyResponse> create(
            @Valid @RequestBody MaxDestinationDieselQtyRequest request,
            Authentication authentication) {

        JwtAuthenticationDetails details =
                (JwtAuthenticationDetails) authentication.getDetails();

        Long userId = details.getUserId();

        return ResponseEntity.ok(
                service.create(request, userId));
    }

    @GetMapping
    public ResponseEntity<List<MaxDestinationDieselQtyResponse>> getAll() {
        return ResponseEntity.ok(service.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<MaxDestinationDieselQtyResponse> getById(
            @PathVariable Long id) {

        return ResponseEntity.ok(service.getById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<MaxDestinationDieselQtyResponse> update(
            @PathVariable Long id,
            @Valid @RequestBody MaxDestinationDieselQtyRequest request) {

        return ResponseEntity.ok(
                service.update(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(
            @PathVariable Long id) {

        service.delete(id);

        return ResponseEntity.ok(
                "Maximum destination diesel quantity deleted successfully");
    }
}