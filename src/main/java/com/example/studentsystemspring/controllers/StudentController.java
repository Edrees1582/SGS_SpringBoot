package com.example.studentsystemspring.controllers;

import com.example.studentsystemspring.dao.MySQLEnrollmentDao;
import com.example.studentsystemspring.dao.MySQLGradeDao;
import com.example.studentsystemspring.dao.MySQLUserDao;
import com.example.studentsystemspring.models.*;
import com.example.studentsystemspring.util.GradesStatistics;
import com.example.studentsystemspring.util.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.HashMap;
import java.util.List;

@Controller
public class StudentController {
    private final MySQLUserDao mySQLUserDao;
    private final MySQLEnrollmentDao mySQLEnrollmentDao;
    private final MySQLGradeDao mySQLGradeDao;
    private final UserService userService;

    @Autowired
    public StudentController(MySQLUserDao mySQLUserDao, MySQLEnrollmentDao mySQLEnrollmentDao, MySQLGradeDao mySQLGradeDao, UserService userService) {
        this.mySQLUserDao = mySQLUserDao;
        this.mySQLEnrollmentDao = mySQLEnrollmentDao;
        this.mySQLGradeDao = mySQLGradeDao;
        this.userService = userService;
    }

    @GetMapping("/student")
    public String index(Model model, @RequestParam(required = false) String studentId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        User user = (User) userService.userDetailsService().loadUserByUsername(authentication.getName());

        model.addAttribute("user", user);

        if (studentId == null) {
            studentId = user.getId();
        }

        User student = mySQLUserDao.get(studentId);
        List<Course> courses = mySQLEnrollmentDao.getStudentCourses(studentId);

        HashMap<String, User> instructors = new HashMap<>();
        HashMap<String, Grade> grades = new HashMap<>();

        for (Course course : courses) {
            instructors.put(course.getInstructorId(), mySQLUserDao.get(course.getInstructorId()));
            grades.put(course.getId(), mySQLGradeDao.get(course.getId(), student.getId()));
        }

        Statistics statistics = GradesStatistics.getStudentStatistics(student.getId());

        model.addAttribute("student", student);
        model.addAttribute("courses", courses);
        model.addAttribute("instructors", instructors);
        model.addAttribute("grades", grades);
        model.addAttribute("statistics", statistics);

        return "student";
    }

    @GetMapping("/student/update")
    public String updateForm(Model model, @RequestParam String studentId) {
        model.addAttribute("student", mySQLUserDao.get(studentId));

        return "editStudent";
    }

    @PostMapping("/student/update")
    public String update(RedirectAttributes redirectAttributes,
                         @RequestParam String studentId,
                         @RequestParam String password,
                         @RequestParam String name) {
        if (password.isEmpty()) password = mySQLUserDao.get(studentId).getPassword();

        mySQLUserDao.update(studentId, password, name);

        redirectAttributes.addAttribute("studentId", studentId);
        return "redirect:/student";
    }

    @PostMapping("/student/delete")
    public String delete(@RequestParam String studentId) {
        mySQLUserDao.delete(studentId);

        return "redirect:/admin";
    }

    @PostMapping("/student/enroll")
    public String enroll(RedirectAttributes redirectAttributes,
                         @RequestParam String courseId,
                         @RequestParam String studentId) {
        mySQLEnrollmentDao.save(courseId, studentId);
        redirectAttributes.addAttribute("courseId", courseId);

        return "redirect:/course";
    }

    @PostMapping("/student/unEnroll")
    public String unEnroll(RedirectAttributes redirectAttributes,
                         @RequestParam String courseId,
                         @RequestParam String studentId) {
        mySQLEnrollmentDao.delete(courseId, studentId);
        redirectAttributes.addAttribute("courseId", courseId);

        return "redirect:/course";
    }
}
