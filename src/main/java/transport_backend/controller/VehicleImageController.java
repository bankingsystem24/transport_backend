package transport_backend.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import transport_backend.entity.VehicleImage;
import transport_backend.security.JwtUtil;
import transport_backend.service.VehicleImageService;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/vehicle-images")
@RequiredArgsConstructor
public class VehicleImageController {

    private final VehicleImageService vehicleImageService;
    private final JwtUtil jwtUtil;

    @PostMapping(
            value = "/upload",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public ResponseEntity<VehicleImage> uploadImage(
            @RequestHeader("Authorization") String authorizationHeader,
            @RequestParam("vehicleId") Long vehicleId,
            @RequestParam("file") MultipartFile file
    ) throws IOException {

        // Remove "Bearer " from token
        String token = authorizationHeader.replace("Bearer ", "");

        // Get userId from JWT
        Long userId = jwtUtil.extractUserId(token);

        VehicleImage savedImage =
                vehicleImageService.uploadImage(
                        vehicleId,
                        file,
                        userId
                );

        return ResponseEntity.ok(savedImage);
    }

    /**
     * Get all images of a vehicle
     *
     * GET /api/vehicle-images/vehicle/{vehicleId}
     */
    @GetMapping("/vehicle/{vehicleId}")
    public ResponseEntity<List<VehicleImage>> getVehicleImages(
            @PathVariable Long vehicleId
    ) {

        List<VehicleImage> images =
                vehicleImageService.getVehicleImages(vehicleId);

        return ResponseEntity.ok(images);
    }

    /**
     * Get image by ID
     *
     * GET /api/vehicle-images/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<VehicleImage> getImageById(
            @PathVariable Long id
    ) {

        VehicleImage image =
                vehicleImageService.getImageById(id);

        return ResponseEntity.ok(image);
    }

    /**
     * Delete vehicle image
     *
     * DELETE /api/vehicle-images/{id}
     *
     * Header:
     * Authorization: Bearer <JWT>
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteImage(
            @RequestHeader("Authorization") String authorizationHeader,
            @PathVariable Long id
    ) throws IOException {

        String token = authorizationHeader.replace("Bearer ", "");

        Long userId = jwtUtil.extractUserId(token);

        vehicleImageService.deleteImage(id, userId);

        return ResponseEntity.ok(
                "Vehicle image deleted successfully"
        );
    }
}