package com.hkmu.online_course.security;

import com.hkmu.online_course.model.User;
import com.hkmu.online_course.repository.IUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;

/**
 * Custom UserDetailsService for Spring Security authentication.
 * Loads user from database for login.
 */
@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final IUserRepository userRepo;

    @Autowired
    public CustomUserDetailsService(IUserRepository userRepo) {
        this.userRepo = userRepo;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepo.findById(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));

        return new CustomUserDetails(user);
    }

    /**
     * Custom UserDetails implementation wrapping our User entity.
     */
    public static class CustomUserDetails implements UserDetails {

        private final User user;

        public CustomUserDetails(User user) {
            this.user = user;
        }

        public User getUser() {
            return user;
        }

        @Override
        public Collection<? extends GrantedAuthority> getAuthorities() {
            String role = user.isTeacher() ? "ROLE_TEACHER" : "ROLE_STUDENT";
            return List.of(new SimpleGrantedAuthority(role));
        }

        @Override
        public String getPassword() {
            return user.getPassword();
        }

        @Override
        public String getUsername() {
            return user.getUsername();
        }

        @Override
        public boolean isAccountNonExpired() {
            return true;
        }

        @Override
        public boolean isAccountNonLocked() {
            return !user.isDisabled();
        }

        @Override
        public boolean isCredentialsNonExpired() {
            return true;
        }

        @Override
        public boolean isEnabled() {
            return user.isActive();
        }
    }
}
