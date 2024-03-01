package com.example.studentsystemspring.dto;

import com.example.studentsystemspring.models.UserType;
import lombok.Data;

@Data
public class SignUpRequest {
    private String name;
    private String id;
    private String password;
    private String userType;
}
