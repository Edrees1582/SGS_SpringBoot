package com.example.studentsystemspring.controllers;

import com.example.studentsystemspring.dto.JwtAuthenticationResponse;
import com.example.studentsystemspring.dto.SignInRequest;
import com.example.studentsystemspring.dto.SignUpRequest;
import com.example.studentsystemspring.models.User;
import com.example.studentsystemspring.models.UserType;
import com.example.studentsystemspring.util.AuthenticateService;
import com.example.studentsystemspring.util.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
public class AuthenticationController {
    private final AuthenticateService authenticateService;
    private final UserService userService;

    @PostMapping("/signup")
    public ResponseEntity<User> signup(SignUpRequest signUpRequest) {
        return ResponseEntity.ok(authenticateService.signup(signUpRequest));
    }

    @GetMapping("/")
    public String index() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        User user = (User) userService.userDetailsService().loadUserByUsername(authentication.getName());

        if (user.getUserType() == UserType.ADMIN)
            return "redirect:/admin";
        else if (user.getUserType() == UserType.INSTRUCTOR)
            return "redirect:/instructor";
        else if (user.getUserType() == UserType.STUDENT)
            return "redirect:/student";

        return "redirect:/";
    }
}
