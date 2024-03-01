package com.example.studentsystemspring.util;

import com.example.studentsystemspring.dto.JwtAuthenticationResponse;
import com.example.studentsystemspring.dto.SignInRequest;
import com.example.studentsystemspring.dto.SignUpRequest;
import com.example.studentsystemspring.models.User;

public interface AuthenticateService {
    User signup(SignUpRequest signUpRequest);
}
