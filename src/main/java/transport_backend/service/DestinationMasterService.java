package transport_backend.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import transport_backend.dto.DestinationMasterRequest;
import transport_backend.dto.DestinationMasterResponse;
import transport_backend.entity.DestinationMaster;
import transport_backend.entity.User;
import transport_backend.repository.DestinationMasterRepository;
import transport_backend.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class DestinationMasterService {

    private final DestinationMasterRepository destinationRepository;
    private final UserRepository userRepository;

    public DestinationMasterService(
            DestinationMasterRepository destinationRepository,
            UserRepository userRepository) {

        this.destinationRepository = destinationRepository;
        this.userRepository = userRepository;
    }

    // CREATE
    public DestinationMasterResponse create(
            DestinationMasterRequest request) {

        if (request.getDestination() == null ||
                request.getDestination().trim().isEmpty()) {

            throw new RuntimeException("Destination is required");
        }

        if (request.getCreatedBy() == null) {
            throw new RuntimeException("Created By is required");
        }

        String destinationName = request.getDestination().trim();

        if (destinationRepository
                .existsByDestinationIgnoreCase(destinationName)) {

            throw new RuntimeException(
                    "Destination already exists");
        }

        User user = userRepository
                .findById(request.getCreatedBy())
                .orElseThrow(() ->
                        new RuntimeException("User not found"));

        DestinationMaster destination =
                new DestinationMaster();

        destination.setDestination(destinationName);
        destination.setCreatedBy(user);
        destination.setCreatedDate(LocalDateTime.now());

        DestinationMaster saved =
                destinationRepository.save(destination);

        return mapToResponse(saved);
    }

    // GET BY ID
    @Transactional(readOnly = true)
    public DestinationMasterResponse getById(Long id) {

        DestinationMaster destination =
                destinationRepository.findById(id)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Destination not found"));

        return mapToResponse(destination);
    }

    // GET ALL
    @Transactional(readOnly = true)
    public List<DestinationMasterResponse> getAll() {

        return destinationRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    // UPDATE
    public DestinationMasterResponse update(
            Long id,
            DestinationMasterRequest request) {

        DestinationMaster destination =
                destinationRepository.findById(id)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Destination not found"));

        if (request.getDestination() == null ||
                request.getDestination().trim().isEmpty()) {

            throw new RuntimeException("Destination is required");
        }

        String destinationName =
                request.getDestination().trim();

        // Check duplicate destination
        destinationRepository
                .findByDestinationIgnoreCase(destinationName)
                .ifPresent(existing -> {

                    if (!existing.getId().equals(id)) {
                        throw new RuntimeException(
                                "Destination already exists");
                    }
                });

        destination.setDestination(destinationName);

        // Change createdBy only if supplied
        if (request.getCreatedBy() != null) {

            User user = userRepository
                    .findById(request.getCreatedBy())
                    .orElseThrow(() ->
                            new RuntimeException(
                                    "User not found"));

            destination.setCreatedBy(user);
        }

        DestinationMaster updated =
                destinationRepository.save(destination);

        return mapToResponse(updated);
    }

    // DELETE
    public void delete(Long id) {

        DestinationMaster destination =
                destinationRepository.findById(id)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Destination not found"));

        destinationRepository.delete(destination);
    }

private DestinationMasterResponse mapToResponse(
        DestinationMaster destination) {

    User user = destination.getCreatedBy();

    return new DestinationMasterResponse(
            destination.getId(),
            destination.getDestination(),
            user.getId(),
            user.getName(),
            destination.getCreatedDate()
    );
}

}