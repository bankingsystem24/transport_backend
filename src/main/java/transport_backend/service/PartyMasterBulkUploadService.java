package transport_backend.service;

import org.apache.poi.ss.usermodel.*;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import transport_backend.dto.BulkUploadResponse;
import transport_backend.entity.PartyMaster;
import transport_backend.entity.User;
import transport_backend.repository.PartyMasterRepository;
import transport_backend.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Service
public class PartyMasterBulkUploadService {

    private final PartyMasterRepository partyMasterRepository;
    private final UserRepository userRepository;

    public PartyMasterBulkUploadService(
            PartyMasterRepository partyMasterRepository,
            UserRepository userRepository) {

        this.partyMasterRepository = partyMasterRepository;
        this.userRepository = userRepository;
    }

    public BulkUploadResponse uploadParties(
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

        Set<String> excelPartyNames = new HashSet<>();

        try (Workbook workbook = WorkbookFactory.create(file.getInputStream())) {

            Sheet sheet = workbook.getSheetAt(0);

            // Start from row 1 because row 0 is header
            for (int i = 1; i <= sheet.getLastRowNum(); i++) {

                Row row = sheet.getRow(i);

                if (row == null) {
                    continue;
                }

                totalRows++;

                try {

                    Cell partyNameCell = row.getCell(0);

                    if (partyNameCell == null ||
                            partyNameCell.getCellType() == CellType.BLANK) {

                        failedCount++;
                        continue;
                    }

                    String partyName =
                            getCellValueAsString(partyNameCell).trim();

                    if (partyName.isEmpty()) {
                        failedCount++;
                        continue;
                    }

                    // Duplicate inside Excel
                    String normalizedName = partyName.toLowerCase();

                    if (!excelPartyNames.add(normalizedName)) {
                        failedCount++;
                        continue;
                    }

                    // Duplicate already present in database
                    if (partyMasterRepository
                            .existsByPartyNameIgnoreCase(partyName)) {

                        failedCount++;
                        continue;
                    }

                    PartyMaster party = new PartyMaster();

                    party.setPartyName(partyName);
                    party.setCreatedBy(createdBy);
                    party.setCreatedDate(LocalDateTime.now());

                    partyMasterRepository.save(party);

                    successCount++;

                } catch (Exception e) {

                    failedCount++;
                }
            }

        } catch (Exception e) {

            throw new RuntimeException(
                    "Failed to process Excel file: " + e.getMessage(), e);
        }

        return new BulkUploadResponse(
                totalRows,
                successCount,
                failedCount,
                "Party bulk upload completed"
        );
    }

    private String getCellValueAsString(Cell cell) {

        DataFormatter formatter = new DataFormatter();

        return formatter.formatCellValue(cell);
    }
}