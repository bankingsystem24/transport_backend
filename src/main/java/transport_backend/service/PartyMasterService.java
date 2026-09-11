package transport_backend.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import transport_backend.dto.PartyMasterRequest;
import transport_backend.dto.PartyMasterResponse;
import transport_backend.entity.PartyMaster;
import transport_backend.entity.User;
import transport_backend.repository.PartyMasterRepository;
import transport_backend.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class PartyMasterService {

    private final PartyMasterRepository partyRepository;
    private final UserRepository userRepository;

    public PartyMasterService(
            PartyMasterRepository partyRepository,
            UserRepository userRepository) {

        this.partyRepository = partyRepository;
        this.userRepository = userRepository;
    }

    // CREATE
    public PartyMasterResponse create(PartyMasterRequest request) {

        if (request.getPartyName() == null ||
                request.getPartyName().trim().isEmpty()) {

            throw new RuntimeException("Party Name is required");
        }

        if (request.getCreatedBy() == null) {
            throw new RuntimeException("Created By is required");
        }

        String partyName = request.getPartyName().trim();

        if (partyRepository.existsByPartyNameIgnoreCase(partyName)) {
            throw new RuntimeException("Party already exists");
        }

        User user = userRepository
                .findById(request.getCreatedBy())
                .orElseThrow(() ->
                        new RuntimeException("User not found"));

        PartyMaster party = new PartyMaster();

        party.setPartyName(partyName);
        party.setCreatedBy(user);
        party.setCreatedDate(LocalDateTime.now());

        PartyMaster saved = partyRepository.save(party);

        return mapToResponse(saved);
    }

    // GET BY ID
    @Transactional(readOnly = true)
    public PartyMasterResponse getById(Long id) {

        PartyMaster party = partyRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Party not found"));

        return mapToResponse(party);
    }

    // GET ALL
    @Transactional(readOnly = true)
    public List<PartyMasterResponse> getAll() {

        return partyRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    // UPDATE
    public PartyMasterResponse update(
            Long id,
            PartyMasterRequest request) {

        PartyMaster party = partyRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Party not found"));

        if (request.getPartyName() == null ||
                request.getPartyName().trim().isEmpty()) {

            throw new RuntimeException("Party Name is required");
        }

        String partyName = request.getPartyName().trim();

        partyRepository
                .findByPartyNameIgnoreCase(partyName)
                .ifPresent(existing -> {

                    if (!existing.getId().equals(id)) {
                        throw new RuntimeException(
                                "Party already exists");
                    }
                });

        party.setPartyName(partyName);

        if (request.getCreatedBy() != null) {

            User user = userRepository
                    .findById(request.getCreatedBy())
                    .orElseThrow(() ->
                            new RuntimeException("User not found"));

            party.setCreatedBy(user);
        }

        PartyMaster updated = partyRepository.save(party);

        return mapToResponse(updated);
    }

    // DELETE
    public void delete(Long id) {

        PartyMaster party = partyRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Party not found"));

        partyRepository.delete(party);
    }

    // RESPONSE MAPPING
    private PartyMasterResponse mapToResponse(
            PartyMaster party) {

        User user = party.getCreatedBy();

        return new PartyMasterResponse(
                party.getId(),
                party.getPartyName(),
                user.getId(),
                user.getName(),
                party.getCreatedDate()
        );
    }
}