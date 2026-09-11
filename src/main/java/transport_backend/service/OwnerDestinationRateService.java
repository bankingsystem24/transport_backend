package transport_backend.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import transport_backend.dto.OwnerDestinationRateRequest;
import transport_backend.dto.OwnerDestinationRateResponse;
import transport_backend.entity.DestinationMaster;
import transport_backend.entity.OwnerDestinationRate;
import transport_backend.entity.OwnerMaster;
import transport_backend.entity.ProductMaster;
import transport_backend.exception.ResourceNotFoundException;
import transport_backend.repository.DestinationMasterRepository;
import transport_backend.repository.OwnerDestinationRateRepository;
import transport_backend.repository.OwnerMasterRepository;
import transport_backend.repository.ProductMasterRepository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
@Transactional
public class OwnerDestinationRateService {

    private final OwnerDestinationRateRepository rateRepository;
    private final OwnerMasterRepository ownerRepository;
    private final ProductMasterRepository productRepository;
    private final DestinationMasterRepository destinationRepository;

    public OwnerDestinationRateService(
            OwnerDestinationRateRepository rateRepository,
            OwnerMasterRepository ownerRepository,
            ProductMasterRepository productRepository,
            DestinationMasterRepository destinationRepository) {

        this.rateRepository = rateRepository;
        this.ownerRepository = ownerRepository;
        this.productRepository = productRepository;
        this.destinationRepository = destinationRepository;
    }

    // =========================================================
    // CREATE
    // =========================================================

    public OwnerDestinationRateResponse create(
            OwnerDestinationRateRequest request) {

        validateDates(
                request.getFromDate(),
                request.getToDate()
        );

        OwnerMaster owner =
                ownerRepository.findById(request.getOwnerId())
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Owner not found with id: "
                                                + request.getOwnerId()
                                )
                        );

        ProductMaster product =
                productRepository.findById(request.getProductId())
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Product not found with id: "
                                                + request.getProductId()
                                )
                        );

        DestinationMaster destination =
                destinationRepository.findById(
                                request.getDestinationId()
                        )
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Destination not found with id: "
                                                + request.getDestinationId()
                                )
                        );

        // Check overlapping date range
        List<OwnerDestinationRate> overlappingRates =
                rateRepository.findOverlappingRates(
                        request.getOwnerId(),
                        request.getProductId(),
                        request.getDestinationId(),
                        request.getFromDate(),
                        request.getToDate()
                );

        if (!overlappingRates.isEmpty()) {

            throw new IllegalArgumentException(
                    "Rate already exists for this owner, product and "
                            + "destination for the selected date range"
            );
        }

        OwnerDestinationRate rate =
                new OwnerDestinationRate();

        rate.setOwner(owner);
        rate.setProduct(product);
        rate.setDestination(destination);

        rate.setFromDate(request.getFromDate());
        rate.setToDate(request.getToDate());

        rate.setCompanyRate(request.getCompanyRate());
        rate.setOwnerRate(request.getOwnerRate());

        // Benefit = Company Rate - Owner Rate
        BigDecimal benefit =
                request.getCompanyRate()
                        .subtract(request.getOwnerRate());

        rate.setBenefit(benefit);

        OwnerDestinationRate saved =
                rateRepository.save(rate);

        return mapToResponse(saved);
    }

    // =========================================================
    // GET ALL
    // =========================================================

    @Transactional(readOnly = true)
    public List<OwnerDestinationRateResponse> getAll() {

        return rateRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    // =========================================================
    // GET BY ID
    // =========================================================

    @Transactional(readOnly = true)
    public OwnerDestinationRateResponse getById(Long id) {

        OwnerDestinationRate rate =
                rateRepository.findById(id)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Owner destination rate not found with id: "
                                                + id
                                )
                        );

        return mapToResponse(rate);
    }

    // =========================================================
    // UPDATE
    // =========================================================

    public OwnerDestinationRateResponse update(
            Long id,
            OwnerDestinationRateRequest request) {

        validateDates(
                request.getFromDate(),
                request.getToDate()
        );

        OwnerDestinationRate rate =
                rateRepository.findById(id)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Owner destination rate not found with id: "
                                                + id
                                )
                        );

        OwnerMaster owner =
                ownerRepository.findById(request.getOwnerId())
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Owner not found with id: "
                                                + request.getOwnerId()
                                )
                        );

        ProductMaster product =
                productRepository.findById(request.getProductId())
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Product not found with id: "
                                                + request.getProductId()
                                )
                        );

        DestinationMaster destination =
                destinationRepository.findById(
                                request.getDestinationId()
                        )
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Destination not found with id: "
                                                + request.getDestinationId()
                                )
                        );

        // Check overlap but exclude current record
        List<OwnerDestinationRate> overlappingRates =
                rateRepository.findOverlappingRatesForUpdate(
                        id,
                        request.getOwnerId(),
                        request.getProductId(),
                        request.getDestinationId(),
                        request.getFromDate(),
                        request.getToDate()
                );

        if (!overlappingRates.isEmpty()) {

            throw new IllegalArgumentException(
                    "Rate already exists for this owner, product and "
                            + "destination for the selected date range"
            );
        }

        rate.setOwner(owner);
        rate.setProduct(product);
        rate.setDestination(destination);

        rate.setFromDate(request.getFromDate());
        rate.setToDate(request.getToDate());

        rate.setCompanyRate(request.getCompanyRate());
        rate.setOwnerRate(request.getOwnerRate());

        // Recalculate benefit
        BigDecimal benefit =
                request.getCompanyRate()
                        .subtract(request.getOwnerRate());

        rate.setBenefit(benefit);

        OwnerDestinationRate updated =
                rateRepository.save(rate);

        return mapToResponse(updated);
    }

    // =========================================================
    // DELETE
    // =========================================================

    public void delete(Long id) {

        OwnerDestinationRate rate =
                rateRepository.findById(id)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Owner destination rate not found with id: "
                                                + id
                                )
                        );

        rateRepository.delete(rate);
    }

    // =========================================================
    // DATE VALIDATION
    // =========================================================

    private void validateDates(
            LocalDate fromDate,
            LocalDate toDate) {

        if (fromDate == null || toDate == null) {

            throw new IllegalArgumentException(
                    "From date and To date are required"
            );
        }

        if (toDate.isBefore(fromDate)) {

            throw new IllegalArgumentException(
                    "To date cannot be before From date"
            );
        }
    }

    // =========================================================
    // ENTITY -> RESPONSE
    // =========================================================

    private OwnerDestinationRateResponse mapToResponse(
            OwnerDestinationRate rate) {

        return new OwnerDestinationRateResponse(
                rate.getId(),

                rate.getOwner().getId(),
                rate.getOwner().getOwnerName(),

                rate.getProduct().getId(),
                rate.getProduct().getProductName(),

                rate.getDestination().getId(),
                rate.getDestination().getDestination(),

                rate.getFromDate(),
                rate.getToDate(),

                rate.getCompanyRate(),
                rate.getOwnerRate(),
                rate.getBenefit()
        );
    }
}