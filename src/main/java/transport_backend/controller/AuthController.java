package transport_backend.controller;

import transport_backend.entity.User;
import transport_backend.repository.UserRepository;
import transport_backend.security.JwtUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public AuthController(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            JwtUtil jwtUtil) {

        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(
            @RequestBody Map<String, String> request) {

        String username = request.get("username");
        String password = request.get("password");

        User user = userRepository
                .findByUsername(username)
                .orElse(null);

        if (user == null) {
            return ResponseEntity
                    .status(401)
                    .body(Map.of(
                            "message",
                            "Invalid username or password"
                    ));
        }

        if (!passwordEncoder.matches(
                password,
                user.getPassword())) {

            return ResponseEntity
                    .status(401)
                    .body(Map.of(
                            "message",
                            "Invalid username or password"
                    ));
        }

        if (!user.getActive()) {
            return ResponseEntity
                    .status(401)
                    .body(Map.of(
                            "message",
                            "User account is inactive"
                    ));
        }

        String token = jwtUtil.generateToken(
                user.getUsername()
        );

        Map<String, Object> response = new HashMap<>();

        response.put("id", user.getId());
        response.put("role", user.getRole());
        response.put("message", "Login successful");
        response.put("username", user.getUsername());
        response.put("name", user.getName());
        response.put("token", token);

        return ResponseEntity.ok(response);
    }
}