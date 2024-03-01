package com.example.studentsystemspring.controllers;

import com.example.studentsystemspring.dao.MySQLCourseDao;
import com.example.studentsystemspring.dao.MySQLEnrollmentDao;
import com.example.studentsystemspring.dao.MySQLGradeDao;
import com.example.studentsystemspring.dao.MySQLUserDao;
import com.example.studentsystemspring.models.*;
import com.example.studentsystemspring.util.GradesStatistics;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.HashMap;
import java.util.List;

@Controller
public class GradeController {
    private final MySQLGradeDao mySQLGradeDao;

    @Autowired
    public GradeController(MySQLGradeDao mySQLGradeDao) {
        this.mySQLGradeDao = mySQLGradeDao;
    }

    @GetMapping("/grade/update")
    public String updateForm(Model model,
                             @RequestParam String courseId,
                             @RequestParam String studentId) {
        Grade grade = mySQLGradeDao.get(courseId, studentId);
        model.addAttribute("grade", grade);
        model.addAttribute("courseId", courseId);

        return "editGrade";
    }

    @PostMapping("/grade/update")
    public String update(RedirectAttributes redirectAttributes,
                        @RequestParam String courseId,
                         @RequestParam String studentId,
                         @RequestParam double grade) {
        mySQLGradeDao.update(courseId, studentId, grade);

        redirectAttributes.addAttribute("courseId", courseId);
        return "redirect:/course";
    }
}
