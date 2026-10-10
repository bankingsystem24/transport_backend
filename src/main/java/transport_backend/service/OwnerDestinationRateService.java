package transport_backend.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import transport_backend.dto.OwnerDestinationRateRequest;
import transport_backend.dto.OwnerDestinationRateResponse;
import transport_backend.dto.OwnerDestinationRateRevisionItem;
import transport_backend.dto.OwnerDestinationRateRevisionRequest;
import transport_backend.entity.CompanyDestinationRate;
import transport_backend.entity.DestinationMaster;
import transport_backend.entity.OwnerDestinationRate;
import transport_backend.entity.OwnerMaster;
import transport_backend.entity.ProductMaster;
import transport_backend.exception.ResourceNotFoundException;
import org.apache.poi.ss.usermodel.*;
import org.springframework.web.multipart.MultipartFile;
import transport_backend.repository.*;
import java.io.InputStream;
import java.util.ArrayList;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@Transactional
public class OwnerDestinationRateService {

    private final OwnerDestinationRateRepository rateRepository;
    private final OwnerMasterRepository ownerRepository;
    private final ProductMasterRepository productRepository;
    private final DestinationMasterRepository destinationRepository;
    private final OwnerDestinationRateRepository ownerRateRepository;
    private final CompanyDestinationRateRepository companyRateRepository;


    public OwnerDestinationRateService(
            OwnerDestinationRateRepository rateRepository,
            OwnerMasterRepository ownerRepository,
            ProductMasterRepository productRepository,
            DestinationMasterRepository destinationRepository,
            OwnerDestinationRateRepository ownerRateRepository,
            CompanyDestinationRateRepository companyRateRepository
            ) {

        this.rateRepository = rateRepository;
        this.ownerRepository = ownerRepository;
        this.productRepository = productRepository;
        this.destinationRepository = destinationRepository;
        this.ownerRateRepository = ownerRateRepository;
        this.companyRateRepository = companyRateRepository;
    }
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
        BigDecimal benefit =
                request.getCompanyRate()
                        .subtract(request.getOwnerRate());

        rate.setBenefit(benefit);

        OwnerDestinationRate saved =
                rateRepository.save(rate);

        return mapToResponse(saved);
    }

public List<OwnerDestinationRateResponse> getAll(LocalDate date) {

    return rateRepository.findRatesForDate(date)
            .stream()
            .map(rate -> {
                OwnerDestinationRateResponse response =
                        new OwnerDestinationRateResponse();

                response.setId(rate.getId());
                response.setOwnerId(rate.getOwner().getId());
                response.setOwnerName(rate.getOwner().getOwnerName());
                response.setFromDate(rate.getFromDate());
                response.setToDate(rate.getToDate());
                response.setCompanyRate(rate.getCompanyRate());
                response.setDestinationId(rate.getDestination().getId());
                response.setDestinationName(rate.getDestination().getDestination());
                response.setProductId(rate.getProduct().getId());
                response.setProductName(rate.getProduct().getProductName());
                response.setOwnerRate(rate.getOwnerRate());
                response.setBenefit(rate.getBenefit());

                return response;
            })
            .toList();
}

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

                OwnerMaster owner = ownerRepository
                        .findById(ownerId)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Owner not found with id: "
                                                + ownerId));

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


                OwnerDestinationRate existing =
                        rateRepository.findApplicableRate(
                                ownerId,
                                productId,
                                destinationId,
                                effectiveDate
                        ).orElse(null);

                if (existing != null) {

                existing.setToDate(previousDate);

                rateRepository.save(existing);
                }

                OwnerDestinationRate newRate =
                        new OwnerDestinationRate();

                newRate.setOwner(owner);
                newRate.setProduct(product);
                newRate.setDestination(destination);
                newRate.setFromDate(effectiveDate);
                newRate.setToDate(newToDate);
                newRate.setCompanyRate(item.getCompanyRate());
                newRate.setOwnerRate(item.getOwnerRate());
                newRate.setBenefit(
                        item.getCompanyRate()
                                .subtract(item.getOwnerRate())
                );

                rateRepository.save(newRate);
        }
        }

        @Transactional
        public List<OwnerDestinationRateResponse> getAllOwnerRates(LocalDate effectiveDate) {

        List<OwnerDestinationRate> ownerRates =
            rateRepository.findAll();

        for (OwnerDestinationRate ownerRate : ownerRates) 
        {
                Long productId = ownerRate.getProduct().getId();
                Long destinationId = ownerRate.getDestination().getId();

                Optional<CompanyDestinationRate> companyRateOptional =
                        rateRepository.findApplicableRate(
                                productId,
                                destinationId,
                                effectiveDate
                        );

                if (companyRateOptional.isPresent()) {

                CompanyDestinationRate companyRate =
                        companyRateOptional.get();

                BigDecimal companyRateValue =
                        companyRate.getCompanyRate();

                ownerRate.setCompanyRate(companyRateValue);

                ownerRate.setBenefit(
                        companyRateValue.subtract(
                                ownerRate.getOwnerRate()
                        )
                );
                }
        }

          return ownerRates.stream()
            .map(this::mapToResponse)
            .toList();
        }

    @Transactional
    public int uploadExcel(MultipartFile file, LocalDate wefDate) {

        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("Excel file is required");
        }

        List<OwnerDestinationRate> records = new ArrayList<>();

        try (InputStream inputStream = file.getInputStream();
             Workbook workbook = WorkbookFactory.create(inputStream)) {

            Sheet sheet = workbook.getSheetAt(0);
            DataFormatter formatter = new DataFormatter();

            for (int i = 1; i <= sheet.getLastRowNum(); i++) {

                Row row = sheet.getRow(i);

                if (row == null || isBlank(row, formatter)) {
                    continue;
                }

                int excelRow = i + 1;

                try {
                    Long ownerId = getLong(row, 0, formatter);
                    Long productId = getLong(row, 2, formatter);
                    Long destinationId = getLong(row, 4, formatter);
                //     BigDecimal ownerRate = getDecimal(row, 6, formatter);
                    BigDecimal revisedRate = getDecimal(row, 7, formatter);

                    OwnerMaster owner = ownerRepository
                            .findById(ownerId)
                            .orElseThrow(() -> new IllegalArgumentException(
                                    "Owner not found: " + ownerId));

                    ProductMaster product = productRepository
                            .findById(productId)
                            .orElseThrow(() -> new IllegalArgumentException(
                                    "Product not found: " + productId));

                    DestinationMaster destination = destinationRepository
                            .findById(destinationId)
                            .orElseThrow(() -> new IllegalArgumentException(
                                    "Destination not found: " + destinationId));

                        CompanyDestinationRate companyRate = companyRateRepository
                        .findFirstByProduct_IdAndDestination_IdAndFromDateLessThanEqualAndToDateGreaterThanEqual(
                                productId,
                                destinationId,
                                wefDate,
                                wefDate
                        )
                        .orElseThrow(() -> new IllegalArgumentException(
                                "No applicable company rate for product "
                                + productId + ", destination "
                                + destinationId + ", date " + wefDate
                        ));


                    BigDecimal companyRateValue =
                            companyRate.getCompanyRate();

                    BigDecimal benefit =
                            companyRateValue.subtract(revisedRate);

                    OwnerDestinationRate record =
                            new OwnerDestinationRate();

                    record.setOwner(owner);
                    record.setProduct(product);
                    record.setDestination(destination);
                    record.setFromDate(wefDate);
                    record.setToDate(wefDate.plusYears(10));
                    record.setCompanyRate(companyRateValue);
                    record.setOwnerRate(revisedRate);
                    record.setBenefit(benefit);

                    records.add(record);

                } catch (RuntimeException ex) {
                    throw new IllegalArgumentException(
                            "Error in Excel row " + excelRow + ": "
                            + ex.getMessage(), ex);
                }
            }

            if (records.isEmpty()) {
                throw new IllegalArgumentException(
                        "No valid data rows found in Excel");
            }

            ownerRateRepository.saveAll(records);

            return records.size();

        } catch (IllegalArgumentException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new IllegalArgumentException(
                    "Unable to read Excel file: " + ex.getMessage(), ex);
        }
    }

    private Long getLong(
            Row row, int column, DataFormatter formatter) {

        String value = getCellValue(row, column, formatter);

        try {
            return new BigDecimal(value).longValueExact();
        } catch (Exception ex) {
            throw new IllegalArgumentException(
                    "Invalid ID in column " + (column + 1)
                    + ": " + value);
        }
    }

    private BigDecimal getDecimal(
            Row row, int column, DataFormatter formatter) {

        String value = getCellValue(row, column, formatter);

        try {
            return new BigDecimal(value.replace(",", ""));
        } catch (Exception ex) {
            throw new IllegalArgumentException(
                    "Invalid rate in column " + (column + 1)
                    + ": " + value);
        }
    }

    private String getCellValue(
            Row row, int column, DataFormatter formatter) 
        {

                Cell cell = row.getCell(column, Row.MissingCellPolicy.RETURN_BLANK_AS_NULL);

                if (cell == null) {
                throw new IllegalArgumentException("Missing value in column " + (column + 1));
                }

                String value = formatter.formatCellValue(cell).trim();

                if (value.isEmpty()) {
                throw new IllegalArgumentException(
                        "Missing value in column " + (column + 1));
                }

        return value;
    }

        private boolean isBlank(Row row, DataFormatter formatter) {
        for (int i = 0; i < 8; i++) {
                Cell cell = row.getCell(
                        i, Row.MissingCellPolicy.RETURN_BLANK_AS_NULL);

                if (cell != null
                        && !formatter.formatCellValue(cell).trim().isEmpty()) {
                return false;
                }
        }

        return true;
        }

}

