package transport_backend.dto;

import java.time.LocalDateTime;

public class PartyMasterResponse {

    private Long id;
    private String partyName;
    private Long createdBy;
    private String createdByName;
    private LocalDateTime createdDate;

    public PartyMasterResponse() {
    }

    public PartyMasterResponse(
            Long id,
            String partyName,
            Long createdBy,
            String createdByName,
            LocalDateTime createdDate) {

        this.id = id;
        this.partyName = partyName;
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

    public String getPartyName() {
        return partyName;
    }

    public void setPartyName(String partyName) {
        this.partyName = partyName;
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