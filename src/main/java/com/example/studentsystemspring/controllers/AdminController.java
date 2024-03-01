package com.example.studentsystemspring.controllers;

import com.example.studentsystemspring.dao.MySQLCourseDao;
import com.example.studentsystemspring.dao.MySQLUserDao;
import com.example.studentsystemspring.models.Course;
import com.example.studentsystemspring.models.User;
import com.example.studentsystemspring.models.UserType;
import com.example.studentsystemspring.util.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class AdminController {
    private final MySQLUserDao mySQLUserDao;
    private final MySQLCourseDao mySQLCourseDao;
    private final UserService userService;

    @Autowired
    public AdminController(MySQLUserDao mySQLUserDao, MySQLCourseDao mySQLCourseDao, UserService userService) {
        this.mySQLUserDao = mySQLUserDao;
        this.mySQLCourseDao = mySQLCourseDao;
        this.userService = userService;
    }
    @GetMapping("/admin")
    public String index(Model model) {
        List<User> students = mySQLUserDao.getStudents();
        List<User> instructors = mySQLUserDao.getInstructors();
        List<Course> courses = mySQLCourseDao.getAll();

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        User user = (User) userService.userDetailsService().loadUserByUsername(authentication.getName());

        model.addAttribute("user", user);

        model.addAttribute("students", students);
        model.addAttribute("instructors", instructors);
        model.addAttribute("courses", courses);

        return "admin";
    }
}
