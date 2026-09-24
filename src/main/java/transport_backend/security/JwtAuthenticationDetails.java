package transport_backend.security;

public class JwtAuthenticationDetails {

    private final Long userId;
    private final Long companyId;

    public JwtAuthenticationDetails(Long userId, Long companyId) {
        this.userId = userId;
        this.companyId = companyId;
    }

    public Long getUserId() {
        return userId;
    }

    public Long getCompanyId() {
        return companyId;
    }
}