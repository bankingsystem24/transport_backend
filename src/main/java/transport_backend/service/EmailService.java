package transport_backend.service;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    private final JavaMailSender mailSender;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendOtp(String email, String otp) {

        SimpleMailMessage message = new SimpleMailMessage();

        message.setTo(email);
        message.setSubject("Inventory Management - Password Reset OTP");

        message.setText(
                "Your password reset OTP is: " + otp
                        + "\n\nThis OTP is valid for 10 minutes."
                        + "\n\nIf you did not request a password reset, please ignore this email."
        );

        mailSender.send(message);
    }
}