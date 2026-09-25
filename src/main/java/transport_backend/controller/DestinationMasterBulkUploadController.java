
package transport_backend.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import transport_backend.dto.BulkUploadResponse;
import transport_backend.security.JwtAuthenticationDetails;
import transport_backend.service.DestinationMasterBulkUploadService;

@RestController
@RequestMapping("/api/destination-master")
public class DestinationMasterBulkUploadController {

    private final DestinationMasterBulkUploadService bulkUploadService;

    public DestinationMasterBulkUploadController(
            DestinationMasterBulkUploadService bulkUploadService) {

        this.bulkUploadService = bulkUploadService;
    }

    @PostMapping("/bulk-upload")
    public ResponseEntity<BulkUploadResponse> bulkUpload(
            @RequestParam("file") MultipartFile file,
            Authentication authentication) {

        JwtAuthenticationDetails details =
        (JwtAuthenticationDetails) authentication.getDetails();


        Long userId = (Long) details.getUserId();

        BulkUploadResponse response =
                bulkUploadService.uploadDestinations(file, userId);

        return ResponseEntity.ok(response);
    }
}
