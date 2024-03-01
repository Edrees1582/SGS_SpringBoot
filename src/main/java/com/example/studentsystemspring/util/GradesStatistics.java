package com.example.studentsystemspring.util;

import com.example.studentsystemspring.dao.MySQLGradeDao;
import com.example.studentsystemspring.models.Grade;
import com.example.studentsystemspring.models.Statistics;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class GradesStatistics {
    private static final MySQLGradeDao mySQLGradeDao = new MySQLGradeDao();

    public static Statistics getCourseStatistics(String courseId) {
        List<Grade> grades = mySQLGradeDao.getCourseGrades(courseId);
        if (grades.isEmpty()) return new Statistics(0.0, 0.0, 0.0, 0.0);

        double average = grades.stream().mapToDouble(Grade::getGrade).reduce(0.0, Double::sum) / grades.size();

        double median = 0.0;
        List<Double> medianGrades = grades.stream().mapToDouble(Grade::getGrade).sorted().boxed().toList();
        if (!medianGrades.isEmpty() && medianGrades.size() % 2 != 0)
            median = medianGrades.get(medianGrades.size() / 2);
        else if (!medianGrades.isEmpty())
            median = (medianGrades.get((medianGrades.size() - 1) / 2) + medianGrades.get(medianGrades.size() / 2)) / 2.0;

        double highest = Collections.max(grades, Comparator.comparing(Grade::getGrade)).getGrade();
        double lowest = Collections.min(grades, Comparator.comparing(Grade::getGrade)).getGrade();

        return new Statistics(average, median, highest, lowest);
    }

    public static Statistics getStudentStatistics(String studentId) {
        List<Grade> grades = mySQLGradeDao.getStudentGrades(studentId);
        if (grades.isEmpty()) return new Statistics(0.0, 0.0, 0.0, 0.0);

        double average = grades.stream().mapToDouble(Grade::getGrade).reduce(0.0, Double::sum) / grades.size();

        double median = 0.0;
        List<Double> medianGrades = grades.stream().mapToDouble(Grade::getGrade).sorted().boxed().toList();
        if (!medianGrades.isEmpty() && medianGrades.size() % 2 != 0)
            median = medianGrades.get(medianGrades.size() / 2);
        else if (!medianGrades.isEmpty())
            median = (medianGrades.get((medianGrades.size() - 1) / 2) + medianGrades.get(medianGrades.size() / 2)) / 2.0;

        double highest = Collections.max(grades, Comparator.comparing(Grade::getGrade)).getGrade();
        double lowest = Collections.min(grades, Comparator.comparing(Grade::getGrade)).getGrade();

        return new Statistics(average, median, highest, lowest);
    }
}
