package transport_backend.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.ss.usermodel.*;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import transport_backend.dto.BulkUploadResponse;
import transport_backend.entity.OwnerMaster;
import transport_backend.entity.User;
import transport_backend.repository.OwnerMasterRepository;
import transport_backend.repository.UserRepository;

@Service
public class OwnerMasterBulkUploadService {

    private final OwnerMasterRepository ownerMasterRepository;
    private final UserRepository userRepository;

    public OwnerMasterBulkUploadService(
            OwnerMasterRepository ownerMasterRepository,
            UserRepository userRepository) {
        this.ownerMasterRepository = ownerMasterRepository;
        this.userRepository = userRepository;
    }

    public BulkUploadResponse uploadOwners(
            MultipartFile file,
            Long userId) {

        if (file == null || file.isEmpty()) {
            throw new RuntimeException("Excel file is required");
        }

        User createdBy = userRepository.findById(userId)
                .orElseThrow(() ->
                        new RuntimeException("User not found with id: " + userId));

        int totalRows = 0;
        int successCount = 0;
        int failedCount = 0;

        List<String> errors = new ArrayList<>();

        try (Workbook workbook = WorkbookFactory.create(file.getInputStream())) {

            Sheet sheet = workbook.getSheetAt(0);

            // Row 0 = header
            for (int i = 1; i <= sheet.getLastRowNum(); i++) {

                Row row = sheet.getRow(i);

                if (row == null || isEmptyRow(row)) {
                    continue;
                }

                totalRows++;

                try {

                    String ownerName = getStringCellValue(row.getCell(0));
                    String address = getStringCellValue(row.getCell(1));
                    String phone = getStringCellValue(row.getCell(2));
                    String email = getStringCellValue(row.getCell(3));
                    BigDecimal openingBalance =
                            getBigDecimalCellValue(row.getCell(4));
                    Boolean active =
                            getBooleanCellValue(row.getCell(5));

                    // Required field
                    if (ownerName == null || ownerName.trim().isEmpty()) {
                        throw new RuntimeException("Owner Name is required");
                    }

                    // Duplicate validation
                    if (ownerMasterRepository
                            .existsByOwnerNameIgnoreCase(ownerName.trim())) {

                        throw new RuntimeException(
                                "Owner already exists: " + ownerName);
                    }

                    OwnerMaster owner = new OwnerMaster();

                    owner.setOwnerName(ownerName.trim());
                    owner.setAddress(address);
                    owner.setPhone(phone);
                    owner.setEmail(email);

                    owner.setOpeningBalance(
                            openingBalance != null
                                    ? openingBalance
                                    : BigDecimal.ZERO);

                    owner.setActive(
                            active != null
                                    ? active
                                    : true);

                    // USER FROM JWT
                    owner.setCreatedBy(createdBy);

                    owner.setCreatedDate(LocalDateTime.now());

                    ownerMasterRepository.save(owner);

                    successCount++;

                } catch (Exception e) {

                    failedCount++;

                    errors.add(
                            "Row " + (i + 1) + ": " + e.getMessage());
                }
            }

        } catch (Exception e) {
            throw new RuntimeException(
                    "Failed to read Excel file: " + e.getMessage(), e);
        }

        String message =
                "Bulk upload completed. "
                + "Success: " + successCount
                + ", Failed: " + failedCount;

        if (!errors.isEmpty()) {
            message += ". Errors: " + String.join(" | ", errors);
        }

        return new BulkUploadResponse(
                totalRows,
                successCount,
                failedCount,
                message);
    }

    private String getStringCellValue(Cell cell) {

        if (cell == null) {
            return null;
        }

        DataFormatter formatter = new DataFormatter();

        String value = formatter.formatCellValue(cell);

        return value != null && !value.trim().isEmpty()
                ? value.trim()
                : null;
    }

    private BigDecimal getBigDecimalCellValue(Cell cell) {

        if (cell == null) {
            return null;
        }

        if (cell.getCellType() == CellType.NUMERIC) {
            return BigDecimal.valueOf(cell.getNumericCellValue());
        }

        String value = getStringCellValue(cell);

        if (value == null) {
            return null;
        }

        return new BigDecimal(value);
    }

    private Boolean getBooleanCellValue(Cell cell) {

        if (cell == null) {
            return null;
        }

        if (cell.getCellType() == CellType.BOOLEAN) {
            return cell.getBooleanCellValue();
        }

        String value = getStringCellValue(cell);

        if (value == null) {
            return null;
        }

        if ("true".equalsIgnoreCase(value)
                || "yes".equalsIgnoreCase(value)
                || "1".equals(value)) {
            return true;
        }

        if ("false".equalsIgnoreCase(value)
                || "no".equalsIgnoreCase(value)
                || "0".equals(value)) {
            return false;
        }

        throw new RuntimeException(
                "Invalid Active value. Use TRUE/FALSE");
    }

    private boolean isEmptyRow(Row row) {

        for (int i = 0; i <= 5; i++) {

            Cell cell = row.getCell(i);

            if (cell != null
                    && cell.getCellType() != CellType.BLANK
                    && !getStringCellValue(cell).isEmpty()) {

                return false;
            }
        }

        return true;
    }
}