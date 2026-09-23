package transport_backend.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import transport_backend.entity.User;
import transport_backend.entity.VehicleImage;
import transport_backend.entity.VehicleMaster;
import transport_backend.repository.UserRepository;
import transport_backend.repository.VehicleImageRepository;
import transport_backend.repository.VehicleMasterRepository;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class VehicleImageService {

    private final VehicleMasterRepository vehicleRepository;
    private final VehicleImageRepository vehicleImageRepository;
    private final UserRepository userRepository;
    private final S3Service s3Service;


    // =========================================================
    // UPLOAD IMAGE
    // =========================================================

    public VehicleImage uploadImage(
            Long vehicleId,
            MultipartFile file,
            Long userId
    ) throws IOException {

        if (file == null || file.isEmpty()) {
            throw new RuntimeException("Vehicle image is required");
        }

        // Check vehicle
        VehicleMaster vehicle = vehicleRepository.findById(vehicleId)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Vehicle not found with id: " + vehicleId
                        )
                );

        // Check user
        User user = userRepository.findById(userId)
                .orElseThrow(() ->
                        new RuntimeException(
                                "User not found with id: " + userId
                        )
                );

        // Upload to S3
        String imageKey =
                s3Service.uploadVehicleImage(vehicleId, file);

        // Generate URL
        String imageUrl =
                s3Service.getImageUrl(imageKey);

        // Save database record
        VehicleImage vehicleImage = new VehicleImage();

        vehicleImage.setVehicle(vehicle);
        vehicleImage.setImageKey(imageKey);
        vehicleImage.setImageUrl(imageUrl);
        vehicleImage.setCreatedBy(user);
        vehicleImage.setCreatedDate(LocalDateTime.now());

        return vehicleImageRepository.save(vehicleImage);
    }


    // =========================================================
    // GET ALL IMAGES FOR VEHICLE
    // =========================================================

    public List<VehicleImage> getVehicleImages(Long vehicleId) {

        // Check vehicle exists
        if (!vehicleRepository.existsById(vehicleId)) {
            throw new RuntimeException(
                    "Vehicle not found with id: " + vehicleId
            );
        }

        return vehicleImageRepository.findByVehicleId(vehicleId);
    }


    // =========================================================
    // GET IMAGE BY ID
    // =========================================================

    public VehicleImage getImageById(Long id) {

        return vehicleImageRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Vehicle image not found with id: " + id
                        )
                );
    }


    // =========================================================
    // DELETE IMAGE
    // =========================================================

    public void deleteImage(
            Long id,
            Long userId
    ) throws IOException {

        VehicleImage vehicleImage =
                vehicleImageRepository.findById(id)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Vehicle image not found with id: " + id
                                )
                        );

        // Delete from S3
        if (vehicleImage.getImageKey() != null) {
            s3Service.deleteImage(vehicleImage.getImageKey());
        }

        // Delete from database
        vehicleImageRepository.delete(vehicleImage);
    }
}