package com.digitaltwin.service;
import com.digitaltwin.model.Role;
import com.digitaltwin.model.User;
import com.digitaltwin.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.Collections;
import java.util.List;

@Service
public class UserService implements UserDetailsService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    public UserService(UserRepository r, PasswordEncoder p) { this.userRepository=r; this.passwordEncoder=p; }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username)
            .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));
        return new org.springframework.security.core.userdetails.User(
            user.getUsername(), user.getPassword(), user.isEnabled(), true, true, true,
            Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + user.getRole().name())));
    }
    @Transactional
    public User registerUser(String username, String email, String rawPassword) {
        if (userRepository.existsByUsername(username)) throw new IllegalArgumentException("Username already taken.");
        if (userRepository.existsByEmail(email)) throw new IllegalArgumentException("Email already registered.");
        return userRepository.save(User.builder().username(username).email(email)
            .password(passwordEncoder.encode(rawPassword)).role(Role.USER).enabled(true).build());
    }
    @Transactional
    public User registerAdmin(String username, String email, String rawPassword) {
        return userRepository.save(User.builder().username(username).email(email)
            .password(passwordEncoder.encode(rawPassword)).role(Role.ADMIN).enabled(true).build());
    }
    public User findByUsername(String username) {
        return userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("Not found: " + username));
    }
    public User findById(Long id) {
        return userRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("User not found: " + id));
    }
    public List<User> findAll() { return userRepository.findAll(); }
    public List<User> findAllByRole(Role role) { return userRepository.findByRole(role); }
    @Transactional
    public void toggleUserEnabled(Long id) { User u=findById(id); u.setEnabled(!u.isEnabled()); userRepository.save(u); }
    @Transactional
    public void deleteUser(Long id) { userRepository.deleteById(id); }
    public long countAllUsers() { return userRepository.count(); }
    public boolean isUsernameTaken(String u) { return userRepository.existsByUsername(u); }
    public boolean isEmailTaken(String e) { return userRepository.existsByEmail(e); }
}
