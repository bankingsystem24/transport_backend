package transport_backend.controller;

import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import transport_backend.dto.BillCompletedSmsRequest;
import transport_backend.service.SmsService;

@RestController
@RequestMapping("/api/sms")
public class SmsController {

    private final SmsService smsService;

    public SmsController(SmsService smsService) {
        this.smsService = smsService;
    }

    @PostMapping("/bill-completed")
    public ResponseEntity<Map<String, Object>> sendBillCompletedSms(
            @RequestBody BillCompletedSmsRequest request) {

        Map<String, Object> response = new LinkedHashMap<>();

        try {
            String providerResponse =
                    smsService.sendBillCompletedSms(request);

            response.put("success", true);
            response.put("message",
                    "SMS request submitted to the provider.");
            response.put("providerResponse", providerResponse);

            return ResponseEntity.ok(response);

        } catch (IllegalArgumentException e) {
            response.put("success", false);
            response.put("message", e.getMessage());

            return ResponseEntity.badRequest().body(response);

        } catch (IllegalStateException e) {
            response.put("success", false);
            response.put("message",
                    "SMS template configuration is incomplete.");

            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                    .body(response);

        } catch (Exception e) {
            response.put("success", false);
            response.put("message",
                    "Unable to submit SMS request to the provider.");

            return ResponseEntity.status(HttpStatus.BAD_GATEWAY)
                    .body(response);
        }
    }
}