package com.digitaltwin.config;
import com.digitaltwin.repository.UserRepository;
import com.digitaltwin.service.UserService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {
    private final UserRepository userRepository;
    private final UserService userService;
    public DataInitializer(UserRepository userRepository, UserService userService) {
        this.userRepository = userRepository; this.userService = userService;
    }
    @Override
    public void run(String... args) {
        if (!userRepository.existsByUsername("admin")) {
            userService.registerAdmin("admin","admin@digitaltwin.com","admin123");
            System.out.println("✅ Default admin created — username: admin, password: admin123");
        }
    }
}
