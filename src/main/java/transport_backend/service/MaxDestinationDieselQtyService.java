package transport_backend.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import transport_backend.dto.MaxDestinationDieselQtyRequest;
import transport_backend.dto.MaxDestinationDieselQtyResponse;
import transport_backend.entity.CompanyMaster;
import transport_backend.entity.DestinationMaster;
import transport_backend.entity.MaxDestinationDieselQty;
import transport_backend.repository.CompanyMasterRepository;
import transport_backend.repository.DestinationMasterRepository;
import transport_backend.repository.MaxDestinationDieselQtyRepository;

import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MaxDestinationDieselQtyService {

    private final MaxDestinationDieselQtyRepository repository;
    private final CompanyMasterRepository companyRepository;
    private final DestinationMasterRepository destinationRepository;

    @Transactional
    public MaxDestinationDieselQtyResponse create(
            MaxDestinationDieselQtyRequest request,
            Long userId,Long companyId) {
        
        if (repository.existsByCompany_IdAndDestination_Id(
                companyId,
                request.getDestinationId())) {

            throw new RuntimeException(
                    "Maximum diesel quantity already exists for companyId: "
                            + companyId
                            + " and destinationId: "
                            + request.getDestinationId());
        }

        CompanyMaster company =
                companyRepository.findById(
                        companyId)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Company not found with id: "
                                        + companyId));

        DestinationMaster destination =
                destinationRepository.findById(
                        request.getDestinationId())
                .orElseThrow(() ->
                        new RuntimeException(
                                "Destination not found with id: "
                                        + request.getDestinationId()));

        MaxDestinationDieselQty entity =
                new MaxDestinationDieselQty();

        entity.setCompany(company);
        entity.setDestination(destination);
        entity.setMaxDieselQty(request.getMaxDieselQty());
        entity.setCreatedBy(userId);
        entity.setCreatedDate(LocalDateTime.now());

        return mapToResponse(repository.save(entity));
    }

    @Transactional(readOnly = true)
    public List<MaxDestinationDieselQtyResponse> getAll(Long companyId) {

        return repository.findByCompanyId(companyId)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public MaxDestinationDieselQtyResponse getById(Long id) {

        MaxDestinationDieselQty entity =
                repository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Maximum diesel quantity not found with id: "
                                        + id));

        return mapToResponse(entity);
    }

    @Transactional
    public MaxDestinationDieselQtyResponse update(
            Long id,
            MaxDestinationDieselQtyRequest request,
            Long companyId) {

        MaxDestinationDieselQty entity =
                repository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Maximum diesel quantity not found with id: "
                                        + id));
        boolean companyChanged =
                !entity.getCompany().getId()
                        .equals(companyId);

        boolean destinationChanged =
                !entity.getDestination().getId()
                        .equals(request.getDestinationId());

        if (companyChanged || destinationChanged) {

            if (repository.existsByCompany_IdAndDestination_Id(
                    companyId,
                    request.getDestinationId())) {

                throw new RuntimeException(
                        "Maximum diesel quantity already exists for companyId: "
                                + companyId
                                + " and destinationId: "
                                + request.getDestinationId());
            }

            CompanyMaster company =
                    companyRepository.findById(
                            companyId)
                    .orElseThrow(() ->
                            new RuntimeException(
                                    "Company not found with id: "
                                            + companyId));

            DestinationMaster destination =
                    destinationRepository.findById(
                            request.getDestinationId())
                    .orElseThrow(() ->
                            new RuntimeException(
                                    "Destination not found with id: "
                                            + request.getDestinationId()));

            entity.setCompany(company);
            entity.setDestination(destination);
        }

        entity.setMaxDieselQty(request.getMaxDieselQty());
        entity.setUpdatedDate(LocalDateTime.now());

        return mapToResponse(repository.save(entity));
    }

    @Transactional
    public void delete(Long id) {

        MaxDestinationDieselQty entity =
                repository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Maximum diesel quantity not found with id: "
                                        + id));

        repository.delete(entity);
    }

    private MaxDestinationDieselQtyResponse mapToResponse(
            MaxDestinationDieselQty entity) {

        MaxDestinationDieselQtyResponse response = new MaxDestinationDieselQtyResponse();

        response.setId(entity.getId());
        response.setCompanyId(entity.getCompany().getId());
        response.setCompanyName(entity.getCompany().getCompanyName());
        response.setDestinationId(entity.getDestination().getId());
        response.setDestinationName(entity.getDestination().getDestination());
        response.setMaxDieselQty(entity.getMaxDieselQty());
        response.setCreatedBy(entity.getCreatedBy());
        response.setCreatedDate(entity.getCreatedDate());
        response.setUpdatedDate(entity.getUpdatedDate());
        return response;
    }
}
