package transport_backend.dto;

import java.time.LocalDate;

public class OwnerRateDateRequest {

    private LocalDate date;

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }
}
