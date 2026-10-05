package transport_backend.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import transport_backend.dto.CompanyDestinationRateRequest;
import transport_backend.dto.CompanyDestinationRateResponse;
import transport_backend.dto.CompanyDestinationRateRevisionItem;
import transport_backend.dto.CompanyDestinationRateRevisionRequest;
import transport_backend.entity.CompanyDestinationRate;
import transport_backend.entity.DestinationMaster;
import transport_backend.entity.ProductMaster;
import transport_backend.exception.ResourceNotFoundException;
import transport_backend.repository.CompanyDestinationRateRepository;
import transport_backend.repository.DestinationMasterRepository;
import transport_backend.repository.ProductMasterRepository;

import java.time.LocalDate;
import java.util.List;

@Service
@Transactional
public class CompanyDestinationRateService {

        private final CompanyDestinationRateRepository rateRepository;
        private final DestinationMasterRepository destinationRepository;
        private final ProductMasterRepository productRepository;

        public CompanyDestinationRateService(
                        CompanyDestinationRateRepository rateRepository,
                        DestinationMasterRepository destinationRepository,
                        ProductMasterRepository productRepository) {

                this.rateRepository = rateRepository;
                this.destinationRepository = destinationRepository;
                this.productRepository = productRepository;
        }

        // =========================================================
        // CREATE
        // =========================================================

        public CompanyDestinationRateResponse create(
                        CompanyDestinationRateRequest request) {

                validateDates(
                                request.getFromDate(),
                                request.getToDate());

                DestinationMaster destination = destinationRepository.findById(request.getDestinationId())
                                .orElseThrow(() -> new ResourceNotFoundException(
                                                "Destination not found with id: "
                                                                + request.getDestinationId()));

                ProductMaster product = productRepository.findById(request.getProductId())
                                .orElseThrow(() -> new ResourceNotFoundException(
                                                "Product not found with id: "
                                                                + request.getProductId()));

                // Check overlapping dates
                List<CompanyDestinationRate> overlappingRates = rateRepository.findOverlappingRates(
                                request.getDestinationId(),
                                request.getProductId(),
                                request.getFromDate(),
                                request.getToDate());

                if (!overlappingRates.isEmpty()) {

                        throw new IllegalArgumentException(
                                        "Rate already exists for this destination and product "
                                                        + "for the selected date range");
                }

                CompanyDestinationRate rate = new CompanyDestinationRate();

                rate.setDestination(destination);
                rate.setProduct(product);
                rate.setFromDate(request.getFromDate());
                rate.setToDate(request.getToDate());
                rate.setCompanyRate(request.getCompanyRate());

                CompanyDestinationRate saved = rateRepository.save(rate);

                return mapToResponse(saved);
        }

        // =========================================================
        // GET ALL
        // =========================================================

        @Transactional(readOnly = true)
        public List<CompanyDestinationRateResponse> getAll() {

                return rateRepository.findAll()
                                .stream()
                                .map(this::mapToResponse)
                                .toList();
        }

        // =========================================================
        // GET BY ID
        // =========================================================

        @Transactional(readOnly = true)
        public CompanyDestinationRateResponse getById(Long id) {

                CompanyDestinationRate rate = rateRepository.findById(id)
                                .orElseThrow(() -> new ResourceNotFoundException(
                                                "Company destination rate not found with id: "
                                                                + id));

                return mapToResponse(rate);
        }

        // =========================================================
        // UPDATE
        // =========================================================

        public CompanyDestinationRateResponse update(
                        Long id,
                        CompanyDestinationRateRequest request) {

                validateDates(
                                request.getFromDate(),
                                request.getToDate());

                CompanyDestinationRate rate = rateRepository.findById(id)
                                .orElseThrow(() -> new ResourceNotFoundException(
                                                "Company destination rate not found with id: "
                                                                + id));

                DestinationMaster destination = destinationRepository.findById(request.getDestinationId())
                                .orElseThrow(() -> new ResourceNotFoundException(
                                                "Destination not found with id: "
                                                                + request.getDestinationId()));

                ProductMaster product = productRepository.findById(request.getProductId())
                                .orElseThrow(() -> new ResourceNotFoundException(
                                                "Product not found with id: "
                                                                + request.getProductId()));

                // Check overlapping dates excluding current record
                List<CompanyDestinationRate> overlappingRates = rateRepository.findOverlappingRatesForUpdate(
                                id,
                                request.getDestinationId(),
                                request.getProductId(),
                                request.getFromDate(),
                                request.getToDate());

                if (!overlappingRates.isEmpty()) {

                        throw new IllegalArgumentException(
                                        "Rate already exists for this destination and product "
                                                        + "for the selected date range");
                }

                rate.setDestination(destination);
                rate.setProduct(product);
                rate.setFromDate(request.getFromDate());
                rate.setToDate(request.getToDate());
                rate.setCompanyRate(request.getCompanyRate());

                CompanyDestinationRate updated = rateRepository.save(rate);

                return mapToResponse(updated);
        }

        // =========================================================
        // DELETE
        // =========================================================

        public void delete(Long id) {

                CompanyDestinationRate rate = rateRepository.findById(id)
                                .orElseThrow(() -> new ResourceNotFoundException(
                                                "Company destination rate not found with id: "
                                                                + id));

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
                                        "From date and To date are required");
                }

                if (toDate.isBefore(fromDate)) {
                        throw new IllegalArgumentException(
                                        "To date cannot be before From date");
                }
        }

        // =========================================================
        // ENTITY -> RESPONSE
        // =========================================================

        private CompanyDestinationRateResponse mapToResponse(
                        CompanyDestinationRate rate) {

                return new CompanyDestinationRateResponse(
                                rate.getId(),

                                rate.getDestination().getId(),
                                rate.getDestination().getDestination(),

                                rate.getProduct().getId(),
                                rate.getProduct().getProductName(),

                                rate.getFromDate(),
                                rate.getToDate(),

                                rate.getCompanyRate());
        }

        public CompanyDestinationRateResponse getCompanyRate(
                        Long productId,
                        Long destinationId,
                        LocalDate fromDate) {

                CompanyDestinationRate rate = rateRepository
                                .findByProduct_IdAndDestination_IdAndFromDateLessThanEqualAndToDateGreaterThanEqual(
                                                productId,
                                                destinationId,
                                                fromDate,
                                                fromDate)
                                .orElseThrow(() -> new ResourceNotFoundException(
                                                "Company rate not found for productId: "
                                                                + productId
                                                                + ", destinationId: "
                                                                + destinationId
                                                                + ", date: "
                                                                + fromDate));

                return convertToResponse(rate);
        }

        private CompanyDestinationRateResponse convertToResponse(
                        CompanyDestinationRate rate) {

                CompanyDestinationRateResponse response = new CompanyDestinationRateResponse();

                response.setId(rate.getId());

                if (rate.getProduct() != null) {
                        response.setProductId(rate.getProduct().getId());
                        response.setProductName(
                                        rate.getProduct().getProductName());
                }

                if (rate.getDestination() != null) {
                        response.setDestinationId(rate.getDestination().getId());
                        response.setDestinationName(
                                        rate.getDestination().getDestination());
                }

                response.setFromDate(rate.getFromDate());
                response.setToDate(rate.getToDate());
                response.setCompanyRate(rate.getCompanyRate());

                return response;
        }

        @Transactional
public void reviseRates(
        CompanyDestinationRateRevisionRequest request) {

    LocalDate effectiveDate = request.getEffectiveDate();

    LocalDate previousDate = effectiveDate.minusDays(1);

    LocalDate newToDate = effectiveDate
            .plusYears(10)
            .minusDays(1);

    for (CompanyDestinationRateRevisionItem item : request.getRates()) {

        Long productId = item.getProductId();
        Long destinationId = item.getDestinationId();

        ProductMaster product = productRepository
                .findById(productId)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Product not found with id: "
                                        + productId));

        DestinationMaster destination = destinationRepository
                .findById(destinationId)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Destination not found with id: "
                                        + destinationId));

        CompanyDestinationRate existing =
                rateRepository.findApplicableRate(
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
         * Create new rate
         */
        CompanyDestinationRate newRate =
                new CompanyDestinationRate();

        newRate.setProduct(product);
        newRate.setDestination(destination);
        newRate.setFromDate(effectiveDate);
        newRate.setToDate(newToDate);
        newRate.setCompanyRate(item.getCompanyRate());

        rateRepository.save(newRate);
    }
}

}