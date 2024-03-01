package com.example.studentsystemspring.util.impl;

import com.example.studentsystemspring.dto.SignUpRequest;
import com.example.studentsystemspring.models.User;
import com.example.studentsystemspring.models.UserType;
import com.example.studentsystemspring.repository.UserRepository;
import com.example.studentsystemspring.util.AuthenticateService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticateServiceImpl implements AuthenticateService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public User signup(SignUpRequest signUpRequest) {
        User user = new User();
        user.setId(signUpRequest.getId());
        user.setName(signUpRequest.getName());
        user.setUserType(UserType.valueOf(signUpRequest.getUserType().toUpperCase()));
        user.setPassword(passwordEncoder.encode(signUpRequest.getPassword()));

        return userRepository.save(user);
    }
}
