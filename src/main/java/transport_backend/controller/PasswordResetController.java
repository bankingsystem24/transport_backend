package transport_backend.controller;

import transport_backend.entity.User;
import transport_backend.repository.UserRepository;
import transport_backend.service.PasswordResetService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class PasswordResetController {

    private final PasswordResetService passwordResetService;
    private final UserRepository userRepository;

    public PasswordResetController(
            PasswordResetService passwordResetService,
            UserRepository userRepository
            ) {

        this.passwordResetService = passwordResetService;
        this.userRepository = userRepository;
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<?> forgotPassword(
            @RequestBody ForgotPasswordRequest request) {

        User user = userRepository.findByEmail(request.getEmail())
            .orElse(null);


    if (user == null) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(new MessageResponse("User Not Found"));
    }


        passwordResetService.sendOtp(request.getEmail());

        return ResponseEntity.ok(
                new MessageResponse("OTP sent to your email")
        );
    }

    @PostMapping("/verify-otp")
    public ResponseEntity<?> verifyOtp(
            @RequestBody VerifyOtpRequest request) {

        passwordResetService.verifyOtp(
                request.getEmail(),
                request.getOtp()
        );

        return ResponseEntity.ok(
                new MessageResponse("OTP verified successfully")
        );
    }

    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(
            @RequestBody ResetPasswordRequest request) {

        passwordResetService.resetPassword(
                request.getEmail(),
                request.getOtp(),
                request.getNewPassword()
        );

        return ResponseEntity.ok(
                new MessageResponse("Password reset successfully")
        );
    }

    public static class ForgotPasswordRequest {

        private String email;

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }
    }

    public static class VerifyOtpRequest {

        private String email;
        private String otp;

        public String getEmail() {
            return email;
        }

        public String getOtp() {
            return otp;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public void setOtp(String otp) {
            this.otp = otp;
        }
    }

    public static class ResetPasswordRequest {

        private String email;
        private String otp;
        private String newPassword;

        public String getEmail() {
            return email;
        }

        public String getOtp() {
            return otp;
        }

        public String getNewPassword() {
            return newPassword;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public void setOtp(String otp) {
            this.otp = otp;
        }

        public void setNewPassword(String newPassword) {
            this.newPassword = newPassword;
        }
    }

    public static class MessageResponse {

        private String message;

        public MessageResponse(String message) {
            this.message = message;
        }

        public String getMessage() {
            return message;
        }
    }
}