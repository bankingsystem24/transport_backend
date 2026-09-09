package transport_backend.controller;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import transport_backend.entity.OwnerMaster;
import transport_backend.entity.User;
import transport_backend.repository.OwnerMasterRepository;
import transport_backend.repository.UserRepository;
import transport_backend.service.OwnerMasterService;

@RestController
@RequestMapping("/api/owners")
@CrossOrigin(origins = "*")
public class OwnerMasterController {

    private final OwnerMasterService ownerMasterService;
    private final UserRepository userRepository;
    private final OwnerMasterRepository ownerMasterRepository;

    public OwnerMasterController(OwnerMasterService ownerMasterService,
        UserRepository userRepository,
        OwnerMasterRepository ownerMasterRepository) {
        this.ownerMasterService = ownerMasterService;
        this.userRepository = userRepository;
        this.ownerMasterRepository = ownerMasterRepository;
    }

    // =========================
    // CREATE OWNER
    // =========================
    @PostMapping
    public ResponseEntity<OwnerMaster> createOwner(
            @RequestBody OwnerMaster owner) {

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        String username = authentication.getName();

        User user = userRepository.findByUsername(username)
                .orElseThrow(() ->
                        new RuntimeException("Logged-in user not found"));

        owner.setCreatedBy(user);
        owner.setCreatedDate(LocalDateTime.now());
        
        OwnerMaster savedOwner = ownerMasterRepository.save(owner);

        return ResponseEntity.ok(savedOwner);
    }

    // =========================
    // GET ALL OWNERS
    // =========================
    @GetMapping
    public ResponseEntity<List<OwnerMaster>> getAllOwners() {

        List<OwnerMaster> owners =
                ownerMasterService.getAllOwners();

        return ResponseEntity.ok(owners);
    }

    // =========================
    // GET OWNER BY ID
    // =========================
    @GetMapping("/{id}")
    public ResponseEntity<OwnerMaster> getOwnerById(
            @PathVariable Long id) {

        OwnerMaster owner =
                ownerMasterService.getOwnerById(id);

        return ResponseEntity.ok(owner);
    }

    // =========================
    // UPDATE OWNER
    // =========================
    @PutMapping("/{id}")
    public ResponseEntity<OwnerMaster> updateOwner(
            @PathVariable Long id,
            @RequestBody OwnerMaster ownerDetails) {

        OwnerMaster updatedOwner =
                ownerMasterService.updateOwner(
                        id,
                        ownerDetails
                );

        return ResponseEntity.ok(updatedOwner);
    }

    // =========================
    // DELETE OWNER
    // =========================
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteOwner(
            @PathVariable Long id) {

        ownerMasterService.deleteOwner(id);

        return ResponseEntity.ok(
                "Owner deleted successfully"
        );
    }
}