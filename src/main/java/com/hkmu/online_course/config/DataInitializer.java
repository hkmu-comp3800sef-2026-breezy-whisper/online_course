package com.hkmu.online_course.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.hkmu.online_course.model.User;
import com.hkmu.online_course.repository.IUserRepository;

/**
 * Initializes default users on application startup.
 * Creates one teacher and three students for testing.
 */
@Component
public class DataInitializer implements ApplicationRunner {

    private static final Logger log = LoggerFactory.getLogger(DataInitializer.class);
    private final IUserRepository userRepo;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public DataInitializer(IUserRepository userRepo, PasswordEncoder passwordEncoder) {
        this.userRepo = userRepo;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(ApplicationArguments args) {
        log.info("DataInitializer starting...");
        long count = userRepo.count();
        log.info("Current user count: {}", count);

        if (count > 0) {
            // Users already exist, skip initialization
            log.info("Users already exist, skipping initialization");
            return;
        }

        log.info("Creating default users...");

        // Create Teachers
        createUser("teacher1", "password123", "Dr. Smith", "teacher@school.edu", "12345678", 1, 0);
        createUser("teacher2", "password123", "Prof. Jones", "prof@school.edu", "87654321", 1, 1); // Pending

        // Create Students
        createUser("student1", "password123", "Alice Chan", "alice@school.edu", "23456789", 0, 0);
        createUser("student2", "password123", "Bob Lee", "bob@school.edu", "34567890", 0, 0);
        createUser("student3", "password123", "Carol Wong", "carol@school.edu", "45678901", 0, 0);

        // Create Disabled Student
        createUser("disabled1", "password123", "Disabled User", "disabled@school.edu", "56789012", 0, 2);

        log.info("DataInitializer completed. New user count: {}", userRepo.count());
    }

    private void createUser(String username, String password, String fullName,
            String email, String phoneNumber, int role, int status) {
        User user = new User(username, fullName, email, phoneNumber,
                passwordEncoder.encode(password));
        user.setRole(role);
        user.setStatus(status);
        userRepo.save(user);
    }
}
