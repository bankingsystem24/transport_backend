package transport_backend.service;

import java.util.List;

import org.springframework.stereotype.Service;

import transport_backend.entity.OwnerMaster;
import transport_backend.entity.User;
import transport_backend.repository.OwnerMasterRepository;
import transport_backend.repository.UserRepository;

@Service
public class OwnerMasterService {

    private final OwnerMasterRepository ownerMasterRepository;
    private final UserRepository userRepository;

    public OwnerMasterService(OwnerMasterRepository ownerMasterRepository,
        UserRepository userRepository) {
        this.ownerMasterRepository = ownerMasterRepository;
        this.userRepository = userRepository;
    }

    // =========================
    // CREATE
    // =========================
    public OwnerMaster createOwner(OwnerMaster owner, Long userId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        owner.setCreatedBy(user);

        return ownerMasterRepository.save(owner);
    }

    // =========================
    // GET ALL
    // =========================
    public List<OwnerMaster> getAllOwners() {

        return ownerMasterRepository.findAll();
    }

    // =========================
    // GET BY ID
    // =========================
    public OwnerMaster getOwnerById(Long id) {

        return ownerMasterRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Owner not found with id: " + id
                        )
                );
    }

    // =========================
    // UPDATE
    // =========================
    public OwnerMaster updateOwner(Long id, OwnerMaster ownerDetails) {

        OwnerMaster owner = ownerMasterRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Owner not found with id: " + id
                        )
                );

        owner.setOwnerName(ownerDetails.getOwnerName());
        owner.setAddress(ownerDetails.getAddress());
        owner.setPhone(ownerDetails.getPhone());
        owner.setEmail(ownerDetails.getEmail());

        if (ownerDetails.getOpeningBalance() != null) {
            owner.setOpeningBalance(
                    ownerDetails.getOpeningBalance()
            );
        }

        if (ownerDetails.getActive() != null) {
            owner.setActive(ownerDetails.getActive());
        }

        return ownerMasterRepository.save(owner);
    }

    // =========================
    // DELETE
    // =========================
    public void deleteOwner(Long id) {

        OwnerMaster owner = ownerMasterRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Owner not found with id: " + id
                        )
                );

        ownerMasterRepository.delete(owner);
    }
}