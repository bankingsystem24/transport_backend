package transport_backend.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import transport_backend.dto.CompanyDestinationRateRequest;
import transport_backend.dto.CompanyDestinationRateResponse;
import transport_backend.dto.CompanyDestinationRateRevisionItem;
import transport_backend.dto.CompanyDestinationRateRevisionRequest;
import transport_backend.dto.CompanyDestinationRateUploadDto;
import transport_backend.dto.CompanyDestinationRateUploadResponseDto;
import transport_backend.entity.CompanyDestinationRate;
import transport_backend.entity.DestinationMaster;
import transport_backend.entity.ProductMaster;
import transport_backend.exception.ResourceNotFoundException;
import transport_backend.repository.CompanyDestinationRateRepository;
import transport_backend.repository.DestinationMasterRepository;
import transport_backend.repository.ProductMasterRepository;


import org.apache.poi.ss.usermodel.*;

import java.io.InputStream;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

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

public List<CompanyDestinationRateResponse> getAllByDate(
        LocalDate date) {

    List<CompanyDestinationRate> rates =
            rateRepository.findRatesByDate(date);

    return rates.stream()
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

        Set<String> requestKeys = new HashSet<>();

        for (CompanyDestinationRateRevisionItem item : request.getRates()) {

                Long productId = item.getProductId();
                Long destinationId = item.getDestinationId();

                /*
                * Prevent duplicate product + destination
                * in the same request
                */
                String requestKey = productId + "_" + destinationId;

                if (!requestKeys.add(requestKey)) {
                throw new RuntimeException(
                        "Duplicate rate in request for productId: "
                                + productId
                                + " and destinationId: "
                                + destinationId);
                }

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

                /*
                * Check whether a record already exists
                * starting on the effective date.
                */
                boolean alreadyExists =
                        rateRepository.existsByProduct_IdAndDestination_IdAndFromDate(
                                productId,
                                destinationId,
                                effectiveDate
                        );

                if (alreadyExists) {
                throw new RuntimeException(
                        "Rate already exists for productId: "
                                + productId
                                + ", destinationId: "
                                + destinationId
                                + ", effectiveDate: "
                                + effectiveDate);
                }

                /*
                * Find current applicable rate
                */
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

    @Transactional
    public CompanyDestinationRateUploadResponseDto uploadRates(
            MultipartFile file,
            LocalDate wefDate) {

        if (file == null || file.isEmpty()) {
            throw new RuntimeException("Excel file is required.");
        }

        if (wefDate == null) {
            throw new RuntimeException("WEF date is required.");
        }

        List<CompanyDestinationRateUploadDto> excelRecords =
                readExcel(file);

        if (excelRecords.isEmpty()) {
            throw new RuntimeException(
                    "Excel file does not contain any valid records."
            );
        }

        Set<String> uniqueKeys = new HashSet<>();

        for (CompanyDestinationRateUploadDto dto : excelRecords) {

            String key =
                    dto.getProductId()
                            + "_"
                            + dto.getDestinationId();

            if (!uniqueKeys.add(key)) {

                throw new RuntimeException(
                        "Duplicate Product_Id + Destination_Id found "
                                + "in Excel at combination: "
                                + key
                );
            }
        }


        LocalDate previousToDate =
                wefDate.minusDays(1);


        LocalDate newToDate =
                wefDate.plusYears(10);

        int recordsUpdated = 0;
        int recordsInserted = 0;

        for (CompanyDestinationRateUploadDto dto : excelRecords) {

            Long destinationId =
                    dto.getDestinationId();

            Long productId =
                    dto.getProductId();

            DestinationMaster destination =
                    destinationRepository
                            .findById(destinationId)
                            .orElseThrow(() ->
                                    new RuntimeException(
                                            "Destination not found with ID: "
                                                    + destinationId
                                    )
                            );

            ProductMaster product =
                    productRepository
                            .findById(productId)
                            .orElseThrow(() ->
                                    new RuntimeException(
                                            "Product not found with ID: "
                                                    + productId
                                    )
                            );

            Optional<CompanyDestinationRate> existingRate =
                    rateRepository
                            .findActiveRate(
                                    productId,
                                    destinationId,
                                    wefDate
                            );

            if (existingRate.isPresent()) {

                CompanyDestinationRate existing =
                        existingRate.get();

                existing.setToDate(previousToDate);

                rateRepository.save(existing);

                recordsUpdated++;
            }

            CompanyDestinationRate newRate =
                    new CompanyDestinationRate();

            newRate.setDestination(destination);
            newRate.setProduct(product);

            newRate.setFromDate(wefDate);
            newRate.setToDate(newToDate);

            newRate.setCompanyRate(
                    dto.getRevisedRate()
            );

            rateRepository.save(newRate);

            recordsInserted++;
        }

        CompanyDestinationRateUploadResponseDto response =
                new CompanyDestinationRateUploadResponseDto();

        response.setSuccess(true);

        response.setMessage(
                "Company destination rates uploaded successfully."
        );

        response.setWefDate(
                wefDate.toString()
        );

        response.setRecordsProcessed(
                excelRecords.size()
        );

        response.setRecordsUpdated(
                recordsUpdated
        );

        response.setRecordsInserted(
                recordsInserted
        );

        return response;
    }

    private List<CompanyDestinationRateUploadDto> readExcel(
            MultipartFile file) {

        List<CompanyDestinationRateUploadDto> records =
                new ArrayList<>();

        try (InputStream inputStream =
                     file.getInputStream();

             Workbook workbook =
                     WorkbookFactory.create(inputStream)) {

            Sheet sheet =
                    workbook.getSheetAt(0);

            for (int rowIndex = 1;
                 rowIndex <= sheet.getLastRowNum();
                 rowIndex++) {

                Row row =
                        sheet.getRow(rowIndex);

                if (row == null ||
                        isRowEmpty(row)) {
                    continue;
                }

                CompanyDestinationRateUploadDto dto =
                        new CompanyDestinationRateUploadDto();


                dto.setDestinationId(
                        getLongValue(
                                row.getCell(0)
                        )
                );


                dto.setDestination(
                        getStringValue(
                                row.getCell(1)
                        )
                );


                dto.setProductId(
                        getLongValue(
                                row.getCell(2)
                        )
                );


                dto.setProduct(
                        getStringValue(
                                row.getCell(3)
                        )
                );

                dto.setCompanyRate(
                        getDecimalValue(
                                row.getCell(4)
                        )
                );


                dto.setRevisedRate(
                        getDecimalValue(
                                row.getCell(5)
                        )
                );

                validateRow(
                        dto,
                        rowIndex + 1
                );

                records.add(dto);
            }

        } catch (Exception e) {

            throw new RuntimeException(
                    "Error while reading Excel file: "
                            + e.getMessage(),
                    e
            );
        }

        return records;
    }



    private void validateRow(
            CompanyDestinationRateUploadDto dto,
            int rowNumber) {

        if (dto.getDestinationId() == null) {

            throw new RuntimeException(
                    "Destination_Id is missing at Excel row "
                            + rowNumber
            );
        }

        if (dto.getProductId() == null) {

            throw new RuntimeException(
                    "Product_Id is missing at Excel row "
                            + rowNumber
            );
        }

        if (dto.getRevisedRate() == null) {

            throw new RuntimeException(
                    "Revised_Rate is missing at Excel row "
                            + rowNumber
            );
        }
    }

    private boolean isRowEmpty(Row row) {

        for (int cellIndex = 0;
             cellIndex < row.getLastCellNum();
             cellIndex++) {

            Cell cell =
                    row.getCell(cellIndex);

            if (cell != null &&
                    cell.getCellType() != CellType.BLANK &&
                    !getStringValue(cell).isEmpty()) {

                return false;
            }
        }

        return true;
    }

    private String getStringValue(Cell cell) {

        if (cell == null) {
            return "";
        }

        DataFormatter formatter =
                new DataFormatter();

        return formatter
                .formatCellValue(cell)
                .trim();
    }


    private Long getLongValue(Cell cell) {

        if (cell == null) {
            return null;
        }

        if (cell.getCellType() ==
                CellType.NUMERIC) {

            return (long)
                    cell.getNumericCellValue();
        }

        String value =
                getStringValue(cell);

        if (value.isEmpty()) {
            return null;
        }

        try {

            return Long.parseLong(value);

        } catch (NumberFormatException e) {

            throw new RuntimeException(
                    "Invalid number value: "
                            + value
            );
        }
    }

    private BigDecimal getDecimalValue(Cell cell) {

        if (cell == null) {
            return null;
        }

        if (cell.getCellType() ==
                CellType.NUMERIC) {

            return BigDecimal.valueOf(
                    cell.getNumericCellValue()
            );
        }

        String value =
                getStringValue(cell);

        if (value.isEmpty()) {
            return null;
        }

        try {

            return new BigDecimal(value);

        } catch (NumberFormatException e) {

            throw new RuntimeException(
                    "Invalid decimal value: "
                            + value
            );
        }
    }
}
