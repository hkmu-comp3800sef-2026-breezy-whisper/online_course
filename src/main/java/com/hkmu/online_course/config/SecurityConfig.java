package com.hkmu.online_course.config;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.rememberme.TokenBasedRememberMeServices;

import com.hkmu.online_course.security.CustomUserDetailsService;

import jakarta.servlet.DispatcherType;

/**
 * Spring Security configuration.
 *
 * Security rules are enforced at METHOD LEVEL via @PreAuthorize annotations in Controllers.
 * This class only handles:
 * - Public path configuration (permitAll)
 * - Form login/logout with Remember-Me
 * - H2 console compatibility
 * - Password encoder bean
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    @Autowired
    private CustomUserDetailsService userDetailsService;

    @Value("${spring.security.remember-me.secret}")
    private String rememberMeSecret;

    @Value("${spring.security.remember-me.token-validity-seconds}")
    private int rememberMeTokenValiditySeconds;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Derives a 256-bit HMAC key from the user-provided secret using SHA-256.
     * This ensures the secret maps properly to the cryptographic key space.
     */
    private byte[] deriveKey(String secret) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            return digest.digest(secret.getBytes(StandardCharsets.UTF_8));
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("SHA-256 not available", e);
        }
    }

    @Bean
    public TokenBasedRememberMeServices rememberMeServices() {
        // Derive a proper 256-bit key from the user-provided secret
        byte[] key = deriveKey(rememberMeSecret);

        TokenBasedRememberMeServices rememberMe = new TokenBasedRememberMeServices(
                Base64.getEncoder().encodeToString(key),  // Encode as string for TokenBasedRememberMeServices
                userDetailsService
        );
        rememberMe.setParameter("remember-me");
        rememberMe.setTokenValiditySeconds(rememberMeTokenValiditySeconds);
        return rememberMe;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(auth -> auth
                .dispatcherTypeMatchers(DispatcherType.FORWARD, DispatcherType.ERROR).permitAll()
                .requestMatchers("/", "/login", "/register", "/h2-console/**").permitAll()
                .requestMatchers("/js/**", "/css/**", "/images/**").permitAll()
                .anyRequest().authenticated()
            )
            .formLogin(form -> form
                .loginPage("/login")
                .loginProcessingUrl("/perform-login")
                .defaultSuccessUrl("/", true)
                .permitAll()
            )
            .logout(logout -> logout
                .logoutSuccessUrl("/login?logout")
                .permitAll()
            )
            .csrf(csrf -> csrf
                .ignoringRequestMatchers("/h2-console/**")
            )
            .headers(headers -> headers
                .frameOptions(frame -> frame.sameOrigin())
            );

        return http.build();
    }
}
