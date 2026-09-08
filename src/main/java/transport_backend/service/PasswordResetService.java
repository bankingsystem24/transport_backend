package transport_backend.service;

import transport_backend.entity.PasswordResetOtp;
import transport_backend.entity.User;
import transport_backend.repository.PasswordResetOtpRepository;
import transport_backend.repository.UserRepository;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Random;

@Service
public class PasswordResetService {

    private final UserRepository userRepository;
    private final PasswordResetOtpRepository otpRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;

    public PasswordResetService(
            UserRepository userRepository,
            PasswordResetOtpRepository otpRepository,
            PasswordEncoder passwordEncoder,
            EmailService emailService) {

        this.userRepository = userRepository;
        this.otpRepository = otpRepository;
        this.passwordEncoder = passwordEncoder;
        this.emailService = emailService;
    }

    @Transactional
    public void sendOtp(String email) {

        // User user = userRepository.findByEmail(email)
        //         .orElseThrow(() ->
        //                 new RuntimeException("User not found"));

        String otp = String.format(
                "%06d",
                new Random().nextInt(1000000)
        );

        otpRepository.deleteByEmail(email);

        PasswordResetOtp resetOtp = new PasswordResetOtp();

        resetOtp.setEmail(email);
        resetOtp.setOtp(otp);
        resetOtp.setExpiryTime(
                LocalDateTime.now().plusMinutes(10)
        );

        otpRepository.save(resetOtp);

        emailService.sendOtp(email, otp);
    }

    public void verifyOtp(String email, String otp) {

        PasswordResetOtp resetOtp =
                otpRepository.findTopByEmailOrderByIdDesc(email)
                        .orElseThrow(() ->
                                new RuntimeException("OTP not found"));

        if (!resetOtp.getOtp().equals(otp)) {
            throw new RuntimeException("Invalid OTP");
        }

        if (LocalDateTime.now()
                .isAfter(resetOtp.getExpiryTime())) {

            throw new RuntimeException("OTP expired");
        }
    }

    @Transactional
    public void resetPassword(
            String email,
            String otp,
            String newPassword) {

        PasswordResetOtp resetOtp =
                otpRepository.findTopByEmailOrderByIdDesc(email)
                        .orElseThrow(() ->
                                new RuntimeException("OTP not found"));

        if (!resetOtp.getOtp().equals(otp)) {
            throw new RuntimeException("Invalid OTP");
        }

        if (LocalDateTime.now()
                .isAfter(resetOtp.getExpiryTime())) {

            throw new RuntimeException("OTP expired");
        }

        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new RuntimeException("User not found"));

        user.setPassword(
                passwordEncoder.encode(newPassword)
        );

        userRepository.save(user);

        otpRepository.deleteByEmail(email);
    }
}