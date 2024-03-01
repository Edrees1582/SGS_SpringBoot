package com.example.studentsystemspring.dao;

import com.example.studentsystemspring.models.Course;
import com.example.studentsystemspring.models.Enrollment;
import com.example.studentsystemspring.models.User;
import com.example.studentsystemspring.models.UserType;
import com.example.studentsystemspring.util.DBUtil;
import org.springframework.stereotype.Service;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Service
public class MySQLEnrollmentDao implements EnrollmentDao {
    @Override
    public List<Course> getStudentCourses(String studentId) {
        try (Connection connection = DBUtil.getConnection()) {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("select\n" +
                    "\tc.*" +
                    "from\n" +
                    "\tcourses c\n" +
                    "inner join enrollment e on e.courseId = c.id\n" +
                    "inner join users s on s.id = e.studentId\n" +
                    "where s.id = '" + studentId + "';");
            List<Course> courses = new ArrayList<>();

            while (resultSet.next())
                courses.add(new Course(resultSet.getString("id"), resultSet.getString("title"), resultSet.getString("instructorId")));

            return courses;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<User> getCourseStudents(String courseId) {
        try (Connection connection = DBUtil.getConnection()) {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("select\n" +
                    "\ts.*\n" +
                    "from\n" +
                    "\tusers s\n" +
                    "inner join enrollment e on e.studentId = s.id\n" +
                    "inner join courses c on c.id = e.courseId\n" +
                    "where c.id = '" + courseId + "';");
            List<User> students = new ArrayList<>();

            while (resultSet.next())
                students.add(new User(resultSet.getString("id"), resultSet.getString("password"), resultSet.getString("name"), UserType.STUDENT));

            return students;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Enrollment> getAll() {
        try (Connection connection = DBUtil.getConnection()) {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("select * from enrollment;");
            List<Enrollment> enrollmentList = new ArrayList<>();

            while (resultSet.next())
                enrollmentList.add(new Enrollment(resultSet.getString("courseId"), resultSet.getString("studentId")));

            return enrollmentList;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void save(String courseId, String studentId) {
        try (Connection connection = DBUtil.getConnection()) {
            String saveSql = "insert into enrollment values (?, ?);";

            PreparedStatement preparedStatement = connection.prepareCall(saveSql);

            preparedStatement.setString(1, courseId);
            preparedStatement.setString(2, studentId);

            preparedStatement.executeUpdate();

            MySQLGradeDao mySQLGradeDao = new MySQLGradeDao();
            mySQLGradeDao.save(courseId, studentId, 0.0);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void delete(String courseId, String studentId) {
        try (Connection connection = DBUtil.getConnection()) {
            String deleteSql = "delete from enrollment where courseId = ? and studentId = ?;";

            PreparedStatement deletePreparedStatement = connection.prepareCall(deleteSql);

            deletePreparedStatement.setString(1, courseId);
            deletePreparedStatement.setString(2, studentId);

            deletePreparedStatement.executeUpdate();

            MySQLGradeDao mySQLGradeDao = new MySQLGradeDao();
            mySQLGradeDao.delete(courseId, studentId);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void deleteByStudent(String studentId) {
        try (Connection connection = DBUtil.getConnection()) {
            String deleteSql = "delete from enrollment where studentId = ?;";

            PreparedStatement deletePreparedStatement = connection.prepareCall(deleteSql);

            deletePreparedStatement.setString(1, studentId);

            deletePreparedStatement.executeUpdate();

            MySQLGradeDao mySQLGradeDao = new MySQLGradeDao();
            mySQLGradeDao.deleteByStudent(studentId);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void deleteByCourse(String courseId) {
        try (Connection connection = DBUtil.getConnection()) {
            String deleteSql = "delete from enrollment where courseId = ?;";

            PreparedStatement deletePreparedStatement = connection.prepareCall(deleteSql);

            deletePreparedStatement.setString(1, courseId);

            deletePreparedStatement.executeUpdate();

            MySQLGradeDao mySQLGradeDao = new MySQLGradeDao();
            mySQLGradeDao.deleteByCourse(courseId);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
