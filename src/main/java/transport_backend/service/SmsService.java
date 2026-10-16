package transport_backend.service;

import java.math.BigDecimal;
import java.net.URI;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import transport_backend.dto.BillCompletedSmsRequest;

@Service
public class SmsService {

    @Value("${sms.api-url}")
    private String apiUrl;

    @Value("${sms.username}")
    private String username;

    @Value("${sms.api-password}")
    private String apiPassword;

    @Value("${sms.sender}")
    private String sender;

    @Value("${sms.priority:4}")
    private String priority;

    @Value("${sms.entity-id}")
    private String entityId;

    @Value("${sms.template-id:}")
    private String templateId;

    private final RestTemplate restTemplate = new RestTemplate();

    public String sendBillCompletedSms(
            BillCompletedSmsRequest request) {

        validateRequest(request);

        if (templateId == null || templateId.trim().isEmpty()) {
            throw new IllegalStateException(
                    "SMS template ID is not configured.");
        }

        String message = String.format(
                "Dear %s, your bill entry is completed for Vehicle %s. "
                        + "Amount: ₹%s, Balance: ₹%s. "
                        + "Bill No: %s. — Amey Transport",
                request.getOwnerName().trim(),
                request.getVehicleNumber().trim(),
                formatAmount(request.getTotalAmount()),
                formatAmount(request.getTripBalance()),
                request.getBillNo().trim()
        );

        URI uri = UriComponentsBuilder
                .fromUriString(apiUrl)
                .queryParam("username", username)
                .queryParam("api_password", apiPassword)
                .queryParam("sender", sender)
                .queryParam("to", request.getMobile().trim())
                .queryParam("message", message)
                .queryParam("priority", priority)
                .queryParam("e_id", entityId)
                .queryParam("t_id", templateId.trim())
                .build()
                .encode()
                .toUri();

        // Call the SMS provider.
        // The provider's response format must be checked to
        // determine whether the SMS was actually accepted.
        return restTemplate.getForObject(uri, String.class);
    }

    private void validateRequest(BillCompletedSmsRequest request) {

        if (request == null) {
            throw new IllegalArgumentException(
                    "SMS request is required.");
        }

        if (isBlank(request.getOwnerName())) {
            throw new IllegalArgumentException(
                    "Owner name is required.");
        }

        if (isBlank(request.getMobile())
                || !request.getMobile().trim().matches("[6-9]\\d{9}")) {
            throw new IllegalArgumentException(
                    "Enter a valid 10-digit Indian mobile number.");
        }

        if (isBlank(request.getVehicleNumber())) {
            throw new IllegalArgumentException(
                    "Vehicle number is required.");
        }

        if (request.getTotalAmount() == null
                || request.getTotalAmount().compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException(
                    "Total amount must be zero or greater.");
        }

        if (request.getTripBalance() == null
                || request.getTripBalance().compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException(
                    "Trip balance must be zero or greater.");
        }

        if (isBlank(request.getBillNo())) {
            throw new IllegalArgumentException(
                    "Bill number is required.");
        }
    }

    private boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }

    private String formatAmount(BigDecimal amount) {
        return amount.stripTrailingZeros().toPlainString();
    }
}