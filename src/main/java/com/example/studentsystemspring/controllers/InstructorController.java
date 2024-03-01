package com.example.studentsystemspring.controllers;

import com.example.studentsystemspring.dao.MySQLCourseDao;
import com.example.studentsystemspring.dao.MySQLEnrollmentDao;
import com.example.studentsystemspring.dao.MySQLUserDao;
import com.example.studentsystemspring.models.*;
import com.example.studentsystemspring.util.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.HashMap;
import java.util.List;

@Controller
public class InstructorController {
    private final MySQLUserDao mySQLUserDao;
    private final MySQLCourseDao mySQLCourseDao;
    private final UserService userService;

    @Autowired
    public InstructorController(MySQLUserDao mySQLUserDao, MySQLCourseDao mySQLCourseDao, UserService userService) {
        this.mySQLUserDao = mySQLUserDao;
        this.mySQLCourseDao = mySQLCourseDao;
        this.userService = userService;
    }

    @GetMapping("/instructor")
    protected void index(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        User user = (User) userService.userDetailsService().loadUserByUsername(authentication.getName());

        model.addAttribute("user", user);

        model.addAttribute("students", mySQLUserDao.getStudents());
        model.addAttribute("courses", mySQLCourseDao.getAllByInstructorId(user.getId()));
    }

    @PostMapping("/instructor")
    public String instructorActions(RedirectAttributes redirectAttributes, Model model,
                         @RequestParam String instructorAction,
                         @RequestParam String instructorId,
                         @RequestParam(required = false) String password,
                         @RequestParam(required = false) String name) {
        if (instructorAction.equals("updateForm")) {
            model.addAttribute("instructor", mySQLUserDao.get(instructorId));

            return "editInstructor";
        }
        else if (instructorAction.equals("update")) {
            if (password.isEmpty()) password = mySQLUserDao.get(instructorId).getPassword();

            mySQLUserDao.update(instructorId, password, name);

            redirectAttributes.addAttribute("instructorId", instructorId);
        }
        else if (instructorAction.equals("delete"))
            mySQLUserDao.delete(instructorId);

        return "redirect:/admin";
    }
}
