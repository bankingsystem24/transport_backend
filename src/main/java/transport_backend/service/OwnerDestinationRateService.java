package transport_backend.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import transport_backend.dto.OwnerDestinationRateRequest;
import transport_backend.dto.OwnerDestinationRateResponse;
import transport_backend.dto.OwnerDestinationRateRevisionItem;
import transport_backend.dto.OwnerDestinationRateRevisionRequest;
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
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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

    @Transactional
        public void reviseRates(
                OwnerDestinationRateRevisionRequest request) {

        LocalDate effectiveDate = request.getEffectiveDate();

        LocalDate previousDate = effectiveDate.minusDays(1);

        LocalDate newToDate = effectiveDate
                .plusYears(10)
                .minusDays(1);

        Set<String> requestKeys = new HashSet<>();

        for (OwnerDestinationRateRevisionItem item : request.getRates()) {

                Long ownerId = item.getOwnerId();
                Long productId = item.getProductId();
                Long destinationId = item.getDestinationId();

                /*
                * Prevent duplicate owner + product + destination
                * in the same request
                */
                String requestKey =
                        ownerId + "_" + productId + "_" + destinationId;

                if (!requestKeys.add(requestKey)) {
                throw new RuntimeException(
                        "Duplicate rate in request for ownerId: "
                                + ownerId
                                + ", productId: "
                                + productId
                                + " and destinationId: "
                                + destinationId);
                }

                /*
                * Validate Owner
                */
                OwnerMaster owner = ownerRepository
                        .findById(ownerId)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Owner not found with id: "
                                                + ownerId));

                /*
                * Validate Product
                */
                ProductMaster product = productRepository
                        .findById(productId)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Product not found with id: "
                                                + productId));

                /*
                * Validate Destination
                */
                DestinationMaster destination = destinationRepository
                        .findById(destinationId)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Destination not found with id: "
                                                + destinationId));

                /*
                * Check whether a record already exists
                * starting on the effective date.
                */
                boolean alreadyExists =
                        rateRepository
                                .existsByOwner_IdAndProduct_IdAndDestination_IdAndFromDate(
                                        ownerId,
                                        productId,
                                        destinationId,
                                        effectiveDate
                                );

                if (alreadyExists) {
                throw new RuntimeException(
                        "Rate already exists for ownerId: "
                                + ownerId
                                + ", productId: "
                                + productId
                                + ", destinationId: "
                                + destinationId
                                + ", effectiveDate: "
                                + effectiveDate);
                }

                /*
                * Find current applicable owner rate
                */
                OwnerDestinationRate existing =
                        rateRepository.findApplicableRate(
                                ownerId,
                                productId,
                                destinationId,
                                effectiveDate
                        ).orElse(null);

                /*
                * Close existing rate
                */
                if (existing != null) {

                existing.setToDate(previousDate);

                rateRepository.save(existing);
                }

                /*
                * Create new owner rate
                */
                OwnerDestinationRate newRate =
                        new OwnerDestinationRate();

                newRate.setOwner(owner);
                newRate.setProduct(product);
                newRate.setDestination(destination);

                newRate.setFromDate(effectiveDate);
                newRate.setToDate(newToDate);

                /*
                * Company rate comes from the request/current
                * company destination rate.
                */
                newRate.setCompanyRate(item.getCompanyRate());

                newRate.setOwnerRate(item.getOwnerRate());

                /*
                * Benefit = Company Rate - Owner Rate
                */
                newRate.setBenefit(
                        item.getCompanyRate()
                                .subtract(item.getOwnerRate())
                );

                rateRepository.save(newRate);
        }
        }
}