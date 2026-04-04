package com.hkmu.online_course.service.impl;

import com.hkmu.online_course.model.User;
import com.hkmu.online_course.repository.IUserRepository;
import com.hkmu.online_course.service.IUserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private IUserRepository userRepo;

    private PasswordEncoder passwordEncoder;
    private UserServiceImpl userService;

    @BeforeEach
    void setUp() {
        passwordEncoder = mock(PasswordEncoder.class);
        userService = new UserServiceImpl(userRepo, passwordEncoder);
    }

    // --- findById ---

    @Test
    void findById_existingUser_returnsUser() {
        User user = new User("alice", "Alice Chan", "alice@example.com", "12345678", "pass");
        when(userRepo.findById("alice")).thenReturn(Optional.of(user));

        Optional<User> result = userService.findById("alice");

        assertTrue(result.isPresent());
        assertEquals("alice", result.get().getUsername());
    }

    @Test
    void findById_nonExistingUser_returnsEmpty() {
        when(userRepo.findById("bob")).thenReturn(Optional.empty());

        Optional<User> result = userService.findById("bob");

        assertFalse(result.isPresent());
    }

    // --- findAll ---

    @Test
    void findAll_returnsAllUsers() {
        User u1 = new User("alice", "Alice", "a@a.com", "11111111", "pass");
        User u2 = new User("bob", "Bob", "b@b.com", "22222222", "pass");
        when(userRepo.findAll()).thenReturn(List.of(u1, u2));

        List<User> result = userService.findAll();

        assertEquals(2, result.size());
    }

    // --- registerStudent ---

    @Test
    void registerStudent_validData_createsActiveStudent() {
        when(userRepo.existsByUsername("alice")).thenReturn(false);
        when(userRepo.existsByEmail("alice@example.com")).thenReturn(false);
        when(passwordEncoder.encode("password123")).thenReturn("encodedPass");
        when(userRepo.save(any(User.class))).thenAnswer(inv -> inv.getArgument(0));

        User result = userService.registerStudent("alice", "password123", "Alice Chan", "alice@example.com", "12345678");

        assertNotNull(result);
        assertEquals("alice", result.getUsername());
        assertEquals(0, result.getRole()); // student
        assertEquals(0, result.getStatus()); // active
        verify(userRepo).save(any(User.class));
    }

    @Test
    void registerStudent_duplicateUsername_throwsException() {
        when(userRepo.existsByUsername("alice")).thenReturn(true);

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
            () -> userService.registerStudent("alice", "pass", "Alice", "alice@example.com", "12345678"));

        assertEquals("Username already exists", ex.getMessage());
        verify(userRepo, never()).save(any());
    }

    @Test
    void registerStudent_duplicateEmail_throwsException() {
        when(userRepo.existsByUsername("alice")).thenReturn(false);
        when(userRepo.existsByEmail("alice@example.com")).thenReturn(true);

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
            () -> userService.registerStudent("alice", "pass", "Alice", "alice@example.com", "12345678"));

        assertEquals("Email already exists", ex.getMessage());
        verify(userRepo, never()).save(any());
    }

    // --- registerTeacher ---

    @Test
    void registerTeacher_validData_createsPendingTeacher() {
        when(userRepo.existsByUsername("teacher1")).thenReturn(false);
        when(userRepo.existsByEmail("teacher@example.com")).thenReturn(false);
        when(passwordEncoder.encode("password123")).thenReturn("encodedPass");
        when(userRepo.save(any(User.class))).thenAnswer(inv -> inv.getArgument(0));

        User result = userService.registerTeacher("teacher1", "password123", "Teacher One", "teacher@example.com", "12345678");

        assertNotNull(result);
        assertEquals("teacher1", result.getUsername());
        assertEquals(1, result.getRole()); // teacher
        assertEquals(1, result.getStatus()); // pending
        verify(userRepo).save(any(User.class));
    }

    @Test
    void registerTeacher_duplicateUsername_throwsException() {
        when(userRepo.existsByUsername("teacher1")).thenReturn(true);

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
            () -> userService.registerTeacher("teacher1", "pass", "Teacher", "teacher@example.com", "12345678"));

        assertEquals("Username already exists", ex.getMessage());
    }

    @Test
    void registerTeacher_duplicateEmail_throwsException() {
        when(userRepo.existsByUsername("teacher1")).thenReturn(false);
        when(userRepo.existsByEmail("teacher@example.com")).thenReturn(true);

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
            () -> userService.registerTeacher("teacher1", "pass", "Teacher", "teacher@example.com", "12345678"));

        assertEquals("Email already exists", ex.getMessage());
    }

    // --- activateTeacher ---

    @Test
    void activateTeacher_pendingTeacher_setsStatusToActive() {
        User teacher = new User("teacher1", "Teacher", "t@t.com", "12345678", "pass");
        teacher.setRole(1);
        teacher.setStatus(1); // pending
        when(userRepo.findById("teacher1")).thenReturn(Optional.of(teacher));
        when(userRepo.save(any(User.class))).thenAnswer(inv -> inv.getArgument(0));

        userService.activateTeacher("teacher1");

        assertEquals(0, teacher.getStatus()); // active
        verify(userRepo).save(teacher);
    }

    @Test
    void activateTeacher_nonExistingUser_throwsException() {
        when(userRepo.findById("ghost")).thenReturn(Optional.empty());

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
            () -> userService.activateTeacher("ghost"));

        assertTrue(ex.getMessage().contains("User not found"));
    }

    @Test
    void activateTeacher_notATeacher_throwsException() {
        User student = new User("alice", "Alice", "a@a.com", "12345678", "pass");
        student.setRole(0); // student
        student.setStatus(1);
        when(userRepo.findById("alice")).thenReturn(Optional.of(student));

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
            () -> userService.activateTeacher("alice"));

        assertEquals("User is not a teacher", ex.getMessage());
    }

    @Test
    void activateTeacher_notPending_throwsException() {
        User teacher = new User("teacher1", "Teacher", "t@t.com", "12345678", "pass");
        teacher.setRole(1);
        teacher.setStatus(0); // already active
        when(userRepo.findById("teacher1")).thenReturn(Optional.of(teacher));

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
            () -> userService.activateTeacher("teacher1"));

        assertEquals("Teacher is not pending activation", ex.getMessage());
    }

    // --- disableUser ---

    @Test
    void disableUser_existingUser_setsStatusToDisabled() {
        User user = new User("alice", "Alice", "a@a.com", "12345678", "pass");
        user.setStatus(0);
        when(userRepo.findById("alice")).thenReturn(Optional.of(user));
        when(userRepo.save(any(User.class))).thenAnswer(inv -> inv.getArgument(0));

        userService.disableUser("alice", "Inappropriate behaviour");

        assertEquals(2, user.getStatus());
        assertEquals("Inappropriate behaviour", user.getDisabledReason());
        verify(userRepo).save(user);
    }

    @Test
    void disableUser_nonExistingUser_throwsException() {
        when(userRepo.findById("ghost")).thenReturn(Optional.empty());

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
            () -> userService.disableUser("ghost", "reason"));

        assertTrue(ex.getMessage().contains("User not found"));
    }

    // --- enableUser ---

    @Test
    void enableUser_disabledUser_setsStatusToActive() {
        User user = new User("alice", "Alice", "a@a.com", "12345678", "pass");
        user.setStatus(2);
        user.setDisabledReason("Was disabled");
        when(userRepo.findById("alice")).thenReturn(Optional.of(user));
        when(userRepo.save(any(User.class))).thenAnswer(inv -> inv.getArgument(0));

        userService.enableUser("alice");

        assertEquals(0, user.getStatus());
        assertNull(user.getDisabledReason());
        verify(userRepo).save(user);
    }

    @Test
    void enableUser_nonExistingUser_throwsException() {
        when(userRepo.findById("ghost")).thenReturn(Optional.empty());

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
            () -> userService.enableUser("ghost"));

        assertTrue(ex.getMessage().contains("User not found"));
    }

    // --- updateProfile ---

    @Test
    void updateProfile_validData_updatesUser() {
        User user = new User("alice", "Alice", "alice@example.com", "12345678", "pass");
        when(userRepo.findById("alice")).thenReturn(Optional.of(user));
        when(userRepo.existsByEmail("newemail@example.com")).thenReturn(false);
        when(userRepo.save(any(User.class))).thenAnswer(inv -> inv.getArgument(0));

        User result = userService.updateProfile("alice", "Alice Updated", "newemail@example.com", "87654321");

        assertEquals("Alice Updated", result.getFullName());
        assertEquals("newemail@example.com", result.getEmail());
        assertEquals("87654321", result.getPhoneNumber());
    }

    @Test
    void updateProfile_sameEmail_doesNotCheckUniqueness() {
        User user = new User("alice", "Alice", "alice@example.com", "12345678", "pass");
        when(userRepo.findById("alice")).thenReturn(Optional.of(user));
        when(userRepo.save(any(User.class))).thenAnswer(inv -> inv.getArgument(0));

        User result = userService.updateProfile("alice", "Alice Updated", "alice@example.com", "87654321");

        verify(userRepo, never()).existsByEmail(anyString());
        assertEquals("Alice Updated", result.getFullName());
    }

    @Test
    void updateProfile_emailAlreadyTaken_throwsException() {
        User user = new User("alice", "Alice", "alice@example.com", "12345678", "pass");
        when(userRepo.findById("alice")).thenReturn(Optional.of(user));
        when(userRepo.existsByEmail("taken@example.com")).thenReturn(true);

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
            () -> userService.updateProfile("alice", "Alice", "taken@example.com", "12345678"));

        assertEquals("Email already exists", ex.getMessage());
    }

    @Test
    void updateProfile_nonExistingUser_throwsException() {
        when(userRepo.findById("ghost")).thenReturn(Optional.empty());

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
            () -> userService.updateProfile("ghost", "Ghost", "g@g.com", "12345678"));

        assertTrue(ex.getMessage().contains("User not found"));
    }
}
