package com.example.studentsystemspring.models;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class Grade {
    private String courseId;
    private String studentId;
    private double grade;

    @Override
    public String toString() {
        return "Grade{" +
                "courseId='" + courseId + '\'' +
                ", studentId='" + studentId + '\'' +
                ", grade='" + grade + '\'' +
                '}';
    }
}
