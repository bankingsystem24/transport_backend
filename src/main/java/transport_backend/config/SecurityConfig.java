package transport_backend.config;

import transport_backend.security.JwtAuthenticationFilter;

import jakarta.servlet.http.HttpServletResponse;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfig {

        private final JwtAuthenticationFilter jwtAuthenticationFilter;

        public SecurityConfig(
                        JwtAuthenticationFilter jwtAuthenticationFilter) {

                this.jwtAuthenticationFilter = jwtAuthenticationFilter;
        }

        @Bean
        public PasswordEncoder passwordEncoder() {
                return new BCryptPasswordEncoder();
        }

        @Bean
        public SecurityFilterChain securityFilterChain(
                        HttpSecurity http) throws Exception {

                http
                                .csrf(csrf -> csrf.disable())

                                .sessionManagement(session -> session.sessionCreationPolicy(
                                                SessionCreationPolicy.STATELESS))

                                .exceptionHandling(exception -> exception

                                                .authenticationEntryPoint(
                                                                (request, response, authException) -> {

                                                                        response.setStatus(
                                                                                        HttpServletResponse.SC_UNAUTHORIZED);

                                                                        response.setContentType(
                                                                                        "application/json");

                                                                        response.getWriter().write(
                                                                                        "{\"message\":\"Authentication required\"}");
                                                                })

                                                .accessDeniedHandler(
                                                                (request, response, accessDeniedException) -> {

                                                                        response.setStatus(
                                                                                        HttpServletResponse.SC_FORBIDDEN);

                                                                        response.setContentType(
                                                                                        "application/json");

                                                                        response.getWriter().write(
                                                                                        "{\"message\":\"Access denied\"}");
                                                                }))

                                .authorizeHttpRequests(auth -> auth
                                        .requestMatchers(
                                                "/api/auth/login",
                                                "/api/auth/forgot-password",
                                                "/api/auth/verify-otp",
                                                "/api/auth/reset-password"
                                        ).permitAll()

                                        

                                        .anyRequest().authenticated()
                        )

                                .formLogin(form -> form.disable())

                                .httpBasic(basic -> basic.disable())

                                .addFilterBefore(
                                                jwtAuthenticationFilter,
                                                UsernamePasswordAuthenticationFilter.class);

                return http.build();
        }
}