package transport_backend.config;

import transport_backend.entity.Role;
import transport_backend.entity.User;
import transport_backend.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner createDefaultAdmin(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder) {

        return args -> {

            if (userRepository.findByUsername("admin").isEmpty()) {

                User admin = new User();

                admin.setName("Administrator");
                admin.setUsername("admin");
                admin.setPassword(
                        passwordEncoder.encode("admin@123")
                );
                admin.setEmail("admin@example.com");
                admin.setMobile("9999999999");
                admin.setRole(Role.ADMIN);
                admin.setActive(true);

                userRepository.save(admin);

                System.out.println(
                        "Default admin user created successfully."
                );
            }
        };
    }
}