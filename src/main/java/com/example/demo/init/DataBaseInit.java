package com.example.demo.init;

import com.example.demo.models.UserRole;
import com.example.demo.repositories.UserRepository;
import org.springframework.boot.CommandLineRunner;
import com.example.demo.models.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DataBaseInit implements CommandLineRunner {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public DataBaseInit(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) throws Exception {
        if(userRepository.count() == 0) {
            User user = new User();
            user.setUsername("mitko");
            user.setPassword(passwordEncoder.encode("parola"));
            user.setFirstName("Dimitar");
            user.setLastName("Dimitrov");
            user.setEmail("mitko@mail.bg");
            user.setRole(UserRole.ORGANIZER);
            User user1 = new User();
            user1.setUsername("pesho");
            user1.setPassword(passwordEncoder.encode("parola"));
            user1.setFirstName("Petar");
            user1.setLastName("Petrov");
            user1.setEmail("pesho@mail.bg");
            user1.setRole(UserRole.ORGANIZER);
            userRepository.save(user);
            userRepository.save(user1);
        }


    }
}
