package com.example.studentsystemspring.controllers;

import com.example.studentsystemspring.dao.MySQLCourseDao;
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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.nio.file.AccessDeniedException;
import java.util.HashMap;
import java.util.List;

@Controller
public class CourseController {
    private final MySQLCourseDao mySQLCourseDao;
    private final MySQLUserDao mySQLUserDao;
    private final MySQLEnrollmentDao mySQLEnrollmentDao;
    private final MySQLGradeDao mySQLGradeDao;
    private final UserService userService;

    @Autowired
    public CourseController(MySQLCourseDao mySQLCourseDao, MySQLUserDao mySQLUserDao, MySQLEnrollmentDao mySQLEnrollmentDao, MySQLGradeDao mySQLGradeDao, UserService userService) {
        this.mySQLCourseDao = mySQLCourseDao;
        this.mySQLUserDao = mySQLUserDao;
        this.mySQLEnrollmentDao = mySQLEnrollmentDao;
        this.mySQLGradeDao = mySQLGradeDao;
        this.userService = userService;
    }

    @GetMapping("/course")
    public String index(Model model, @RequestParam String courseId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        User user = (User) userService.userDetailsService().loadUserByUsername(authentication.getName());

        model.addAttribute("user", user);
        Course course = mySQLCourseDao.get(courseId);

        if (user.getUserType() == UserType.ADMIN || user.getId().equals(course.getInstructorId())) {
            User instructor = mySQLUserDao.get(course.getInstructorId());
            List<User> students = mySQLEnrollmentDao.getCourseStudents(courseId);
            HashMap<String, Grade> grades = new HashMap<>();

            for (User student : students) {
                grades.put(student.getId(), mySQLGradeDao.get(courseId, student.getId()));
            }

            List<User> allStudents = mySQLEnrollmentDao.getCourseStudents(courseId);
            List<User> allOtherStudents = mySQLUserDao.getStudents();

            for (User student : allStudents) {
                for (User otherStudent : allOtherStudents) {
                    if (student.getId().equals(otherStudent.getId())) {
                        allOtherStudents.remove(otherStudent);
                        break;
                    }
                }
            }

            Statistics statistics = GradesStatistics.getCourseStatistics(course.getId());

            model.addAttribute("course", course);
            model.addAttribute("instructor", instructor);
            model.addAttribute("students", students);
            model.addAttribute("grades", grades);
            model.addAttribute("allOtherStudents", allOtherStudents);
            model.addAttribute("statistics", statistics);

            return "course";
        }
        else return "redirect:/";
    }

    @PostMapping("/course/create")
    public String create(@RequestParam String newCourseId,
                        @RequestParam String newTitle,
                        @RequestParam String instructorId) {
        mySQLCourseDao.save(newCourseId, newTitle, instructorId);

        return "redirect:/admin";
    }

    @GetMapping("/course/update")
    public String updateForm(Model model, @RequestParam String courseId) {
        Course course = mySQLCourseDao.get(courseId);
        model.addAttribute("course", course);
        model.addAttribute("instructors", mySQLUserDao.getInstructors());

        return "editCourse";
    }

    @PostMapping("/course/update")
    public String update(RedirectAttributes redirectAttributes,
                        @RequestParam String courseId,
                         @RequestParam String title,
                         @RequestParam String instructorId) {
        mySQLCourseDao.update(courseId, title, instructorId);

        redirectAttributes.addAttribute("courseId", courseId);
        return "redirect:/course";
    }

    @PostMapping("/course/delete")
    public String delete(@RequestParam String courseId) {
        mySQLCourseDao.delete(courseId);

        return "redirect:/admin";
    }
}
