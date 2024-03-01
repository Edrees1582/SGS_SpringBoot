package com.example.studentsystemspring.models;

public class Course {
    private String id;
    private String title;
    private String instructorId;

    public Course(String id, String title, String instructorId) {
        this.id = id;
        this.title = title;
        this.instructorId = instructorId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getInstructorId() {
        return instructorId;
    }

    public void setInstructorId(String instructorId) {
        this.instructorId = instructorId;
    }

    @Override
    public String toString() {
        return "Course{" +
                "id='" + id + '\'' +
                ", title='" + title + '\'' +
                ", instructorId='" + instructorId + '\'' +
                '}';
    }
}
