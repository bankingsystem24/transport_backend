
package transport_backend.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import transport_backend.dto.BulkUploadResponse;
import transport_backend.entity.DestinationMaster;
import transport_backend.entity.User;
import transport_backend.repository.DestinationMasterRepository;
import transport_backend.repository.UserRepository;

@Service
public class DestinationMasterBulkUploadService {

    private final DestinationMasterRepository destinationMasterRepository;
    private final UserRepository userRepository;

    public DestinationMasterBulkUploadService(
            DestinationMasterRepository destinationMasterRepository,
            UserRepository userRepository) {

        this.destinationMasterRepository = destinationMasterRepository;
        this.userRepository = userRepository;
    }

    public BulkUploadResponse uploadDestinations(
            MultipartFile file,
            Long userId) {

        // Check file
        if (file == null || file.isEmpty()) {
            throw new RuntimeException("Excel file is required");
        }

        // Check user
        User createdBy = userRepository.findById(userId)
                .orElseThrow(() ->
                        new RuntimeException(
                                "User not found with id: " + userId));

        int totalRows = 0;
        int successCount = 0;
        int failedCount = 0;

        List<DestinationMaster> destinations = new ArrayList<>();

        try (Workbook workbook =
                     WorkbookFactory.create(file.getInputStream())) {

            Sheet sheet = workbook.getSheetAt(0);

            // Start from row 1 because row 0 is header
            for (int i = 1; i <= sheet.getLastRowNum(); i++) {

                Row row = sheet.getRow(i);

                // Skip completely empty rows
                if (row == null) {
                    continue;
                }

                Cell destinationCell = row.getCell(0);

                // Skip blank destination
                if (destinationCell == null ||
                        destinationCell.toString().trim().isEmpty()) {
                    continue;
                }

                totalRows++;

                String destination =
                        destinationCell.toString().trim();

                try {

                    // Check duplicate destination
                    if (destinationMasterRepository
                            .existsByDestinationIgnoreCase(destination)) {

                        failedCount++;
                        continue;
                    }

                    DestinationMaster destinationMaster =
                            new DestinationMaster();

                    destinationMaster.setDestination(destination);
                    destinationMaster.setCreatedBy(createdBy);
                    destinationMaster.setCreatedDate(LocalDateTime.now());

                    destinations.add(destinationMaster);

                    successCount++;

                } catch (Exception e) {

                    failedCount++;
                }
            }

            // Save all valid destinations
            if (!destinations.isEmpty()) {
                destinationMasterRepository.saveAll(destinations);
            }

            return new BulkUploadResponse(
                    totalRows,
                    successCount,
                    failedCount,
                    "Destination bulk upload completed successfully"
            );

        } catch (Exception e) {

            throw new RuntimeException(
                    "Failed to process Excel file: "
                            + e.getMessage(),
                    e
            );
        }
    }
}
