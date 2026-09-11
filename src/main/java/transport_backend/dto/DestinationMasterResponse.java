package transport_backend.dto;

import java.time.LocalDateTime;

public class DestinationMasterResponse {

    private Long id;
    private String destination;
    private Long createdBy;
    private String createdByName;
    private LocalDateTime createdDate;

    public DestinationMasterResponse() {
    }

    public DestinationMasterResponse(
            Long id,
            String destination,
            Long createdBy,
            String createdByName,
            LocalDateTime createdDate) {

        this.id = id;
        this.destination = destination;
        this.createdBy = createdBy;
        this.createdByName = createdByName;
        this.createdDate = createdDate;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public Long getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(Long createdBy) {
        this.createdBy = createdBy;
    }

    public String getCreatedByName() {
        return createdByName;
    }

    public void setCreatedByName(String createdByName) {
        this.createdByName = createdByName;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(LocalDateTime createdDate) {
        this.createdDate = createdDate;
    }
}