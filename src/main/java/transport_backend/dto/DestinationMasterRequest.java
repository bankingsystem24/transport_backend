package transport_backend.dto;

public class DestinationMasterRequest {

    private String destination;
    private Long createdBy;

    public DestinationMasterRequest() {
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
}