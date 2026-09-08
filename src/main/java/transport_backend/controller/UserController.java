package transport_backend.controller;

import transport_backend.entity.User;
import transport_backend.repository.UserRepository;

import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserController(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder) {

        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {

        List<User> users = userRepository.findAll();

        return ResponseEntity.ok(users);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getUserById(@PathVariable Long id) {

        return userRepository.findById(id)
                .map(user -> ResponseEntity.ok((Object) user))
                .orElseGet(() -> ResponseEntity
                        .status(HttpStatus.NOT_FOUND)
                        .body(Map.of("message", "User not found with id: " + id)));
    }

    @PostMapping
    public ResponseEntity<?> createUser(
            @RequestBody User user) {

        if (user.getName() == null || user.getName().isBlank()) {
            return ResponseEntity.badRequest()
                    .body("Name is required");
        }

        if (user.getUsername() == null || user.getUsername().isBlank()) {
            return ResponseEntity.badRequest()
                    .body("Username is required");
        }

        if (user.getPassword() == null || user.getPassword().isBlank()) {
            return ResponseEntity.badRequest()
                    .body("Password is required");
        }

        if (user.getRole() == null) {
            return ResponseEntity.badRequest()
                    .body("Role is required");
        }

        if (userRepository.findByUsername(user.getUsername()).isPresent()) {
            return ResponseEntity.badRequest()
                    .body("Username already exists");
        }

        user.setPassword(
                passwordEncoder.encode(user.getPassword()));

        if (user.getActive() == null) {
            user.setActive(true);
        }

        User savedUser = userRepository.save(user);

        return ResponseEntity.ok(savedUser);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateUser(
            @PathVariable Long id,
            @RequestBody User request) {

        return userRepository.findById(id)
                .map(user -> {

                    // Username is not changed here
                    user.setName(request.getName());
                    user.setEmail(request.getEmail());
                    user.setMobile(request.getMobile());
                    user.setRole(request.getRole());
                    user.setActive(request.getActive());

                    // Change password only when supplied
                    if (request.getPassword() != null
                            && !request.getPassword().isBlank()) {

                        user.setPassword(
                                passwordEncoder.encode(
                                        request.getPassword()));
                    }

                    User updatedUser = userRepository.save(user);

                    return ResponseEntity.ok(updatedUser);
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUser(
            @PathVariable Long id) {

        return userRepository.findById(id)
                .map(user -> {

                    // Soft delete
                    user.setActive(false);

                    userRepository.save(user);

                    return ResponseEntity.ok(
                            "User deactivated successfully");
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

}