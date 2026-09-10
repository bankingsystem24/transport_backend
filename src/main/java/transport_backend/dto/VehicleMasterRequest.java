package transport_backend.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class VehicleMasterRequest {

    @NotBlank(message = "Vehicle number is required")
    private String vehicleNumber;

    @NotNull(message = "Owner is required")
    private Long ownerId;

    @NotNull(message = "Created by is required")
    private Long createdBy;

    public VehicleMasterRequest() {
    }

    public String getVehicleNumber() {
        return vehicleNumber;
    }

    public void setVehicleNumber(String vehicleNumber) {
        this.vehicleNumber = vehicleNumber;
    }

    public Long getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(Long ownerId) {
        this.ownerId = ownerId;
    }

    public Long getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(Long createdBy) {
        this.createdBy = createdBy;
    }
}