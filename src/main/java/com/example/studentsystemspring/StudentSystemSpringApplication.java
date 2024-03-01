package com.example.studentsystemspring;

import com.example.studentsystemspring.models.User;
import com.example.studentsystemspring.models.UserType;
import com.example.studentsystemspring.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class StudentSystemSpringApplication implements CommandLineRunner {
    @Autowired
    private UserRepository userRepository;

    public static void main(String[] args) {
        SpringApplication.run(StudentSystemSpringApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        User adminAccount = userRepository.findByUserType(UserType.ADMIN);
        if (adminAccount == null) {
            User user = new User();

            user.setId("0");
            user.setName("Admin 0");
            user.setUserType(UserType.ADMIN);
            user.setPassword(new BCryptPasswordEncoder().encode("0"));
            userRepository.save(user);
        }
    }
}
