package com.example.studentsystemspring.dto;

import lombok.Data;

@Data
public class SignInRequest {
    private String id;
    private String password;
}
