package com.example.studentsystemspring.dao;

import com.example.studentsystemspring.models.Course;
import com.example.studentsystemspring.util.DBUtil;
import org.springframework.stereotype.Service;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Service
public class MySQLCourseDao implements CourseDao {
    @Override
    public Course get(String id) {
        try (Connection connection = DBUtil.getConnection()) {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("select * from courses where id = '" + id + "';");

            if (resultSet.next()) return new Course(id, resultSet.getString("title"), resultSet.getString("instructorId"));

            return null;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Course> getAll() {
        try (Connection connection = DBUtil.getConnection()) {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("select * from courses;");
            List<Course> courses = new ArrayList<>();

            while (resultSet.next())
                courses.add(new Course(resultSet.getString("id"), resultSet.getString("title"), resultSet.getString("instructorId")));

            return courses;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Course> getAllByInstructorId(String instructorId) {
        try (Connection connection = DBUtil.getConnection()) {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("select * from courses where instructorId = " + instructorId + ";");
            List<Course> courses = new ArrayList<>();

            while (resultSet.next())
                courses.add(new Course(resultSet.getString("id"), resultSet.getString("title"), instructorId));

            return courses;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void save(String courseId, String courseTitle, String instructorId) {
        try (Connection connection = DBUtil.getConnection()) {
            String saveSql = "insert into courses values (?, ?, ?);";

            PreparedStatement preparedStatement = connection.prepareCall(saveSql);

            preparedStatement.setString(1, courseId);
            preparedStatement.setString(2, courseTitle);
            preparedStatement.setString(3, instructorId);

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void update(String id, String title, String instructorId) {
        try (Connection connection = DBUtil.getConnection()) {
            String updateSql = "update courses set title = ?, instructorId = ? where id = ?;";

            PreparedStatement updatePreparedStatement = connection.prepareCall(updateSql);
            updatePreparedStatement.setString(1, title);
            updatePreparedStatement.setString(2, instructorId);
            updatePreparedStatement.setString(3, id);

            updatePreparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void delete(String id) {
        try (Connection connection = DBUtil.getConnection()) {
            String deleteSql = "delete from courses where id = ?;";

            PreparedStatement deletePreparedStatement = connection.prepareCall(deleteSql);
            deletePreparedStatement.setString(1, id);

            MySQLEnrollmentDao mySQLEnrollmentDao = new MySQLEnrollmentDao();
            mySQLEnrollmentDao.deleteByCourse(id);

            MySQLGradeDao mySQLGradeDao = new MySQLGradeDao();
            mySQLGradeDao.deleteByCourse(id);

            deletePreparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
