package com.example.studentsystemspring.dao;

import com.example.studentsystemspring.models.Course;

import java.util.List;

public interface CourseDao {
    Course get(String id);
    List<Course> getAll();
    List<Course> getAllByInstructorId(String instructorId);
    void save(String courseId, String courseTitle, String instructorId);
    void update(String id, String title, String instructorId);
    void delete(String id);
}
