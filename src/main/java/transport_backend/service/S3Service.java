package transport_backend.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.IOException;
import java.util.UUID;

@Service
public class S3Service {

    private final S3Client s3Client;

    @Value("${aws.s3.bucket-name}")
    private String bucketName;

    @Value("${aws.region}")
    private String region;

    public S3Service(S3Client s3Client) {
        this.s3Client = s3Client;
    }

    // =========================================================
    // UPLOAD IMAGE
    // =========================================================

    public String uploadVehicleImage(
            Long vehicleId,
            MultipartFile file
    ) throws IOException {

        String extension = "";

        if (file.getOriginalFilename() != null
                && file.getOriginalFilename().contains(".")) {

            extension = file.getOriginalFilename()
                    .substring(
                            file.getOriginalFilename()
                                    .lastIndexOf(".")
                    );
        }

        String fileName =
                UUID.randomUUID() + extension;

        String key =
                "vehicles/" + vehicleId + "/" + fileName;

        PutObjectRequest request =
                PutObjectRequest.builder()
                        .bucket(bucketName)
                        .key(key)
                        .contentType(file.getContentType())
                        .build();

        s3Client.putObject(
                request,
                RequestBody.fromInputStream(
                        file.getInputStream(),
                        file.getSize()
                )
        );

        return key;
    }

    // =========================================================
    // GET IMAGE URL
    // =========================================================

    public String getImageUrl(String key) {

        return "https://"
                + bucketName
                + ".s3."
                + region
                + ".amazonaws.com/"
                + key;
    }

    // =========================================================
    // DELETE IMAGE
    // =========================================================

    public void deleteImage(String imageKey) {

        DeleteObjectRequest request =
                DeleteObjectRequest.builder()
                        .bucket(bucketName)
                        .key(imageKey)
                        .build();

        s3Client.deleteObject(request);
    }
}