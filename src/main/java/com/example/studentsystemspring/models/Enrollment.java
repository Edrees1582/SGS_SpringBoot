package com.example.studentsystemspring.models;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class Enrollment {
    private String studentId;
    private String courseId;

    @Override
    public String toString() {
        return "Enrollment{" +
                "studentId='" + studentId + '\'' +
                ", courseId='" + courseId + '\'' +
                '}';
    }
}
