package transport_backend.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import transport_backend.dto.BulkUploadResponse;
import transport_backend.security.JwtAuthenticationDetails;
import transport_backend.service.PartyMasterBulkUploadService;

@RestController
@RequestMapping("/api/party-master")
public class PartyMasterBulkUploadController {

    private final PartyMasterBulkUploadService partyMasterBulkUploadService;

    public PartyMasterBulkUploadController(
            PartyMasterBulkUploadService partyMasterBulkUploadService) {

        this.partyMasterBulkUploadService =
                partyMasterBulkUploadService;
    }

    @PostMapping("/bulk-upload")
    public ResponseEntity<BulkUploadResponse> bulkUpload(
            @RequestParam("file") MultipartFile file,
            Authentication authentication) {

        JwtAuthenticationDetails details =
        (JwtAuthenticationDetails) authentication.getDetails();

        Long userId = (Long) details.getUserId();

        BulkUploadResponse response =
                partyMasterBulkUploadService.uploadParties(
                        file,
                        userId
                );

        return ResponseEntity.ok(response);
    }
}