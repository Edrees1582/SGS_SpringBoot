package com.example.studentsystemspring.dao;

import com.example.studentsystemspring.models.Grade;
import com.example.studentsystemspring.util.DBUtil;
import org.springframework.stereotype.Service;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Service
public class MySQLGradeDao implements GradeDao {
    @Override
    public Grade get(String courseId, String studentId) {
        try (Connection connection = DBUtil.getConnection()) {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("select * from grades where courseId = '" + courseId + "' and studentId = '" + studentId + "';");

            if (resultSet.next()) return new Grade(courseId, studentId, resultSet.getDouble("grade"));

            return null;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Grade> getStudentGrades(String studentId) {
        try (Connection connection = DBUtil.getConnection()) {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("select * from grades where studentId = '" + studentId + "';");
            List<Grade> grades = new ArrayList<>();

            while (resultSet.next())
                grades.add(new Grade(resultSet.getString("courseId"), resultSet.getString("studentId"), resultSet.getDouble("grade")));

            return grades;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Grade> getCourseGrades(String courseId) {
        try (Connection connection = DBUtil.getConnection()) {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("select * from grades where courseId = '" + courseId + "';");
            List<Grade> grades = new ArrayList<>();

            while (resultSet.next())
                grades.add(new Grade(resultSet.getString("courseId"), resultSet.getString("studentId"), resultSet.getDouble("grade")));

            return grades;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void save(String courseId, String studentId, double grade) {
        try (Connection connection = DBUtil.getConnection()) {
            String saveSql = "insert into grades values (?, ?, ?);";

            PreparedStatement preparedStatement = connection.prepareCall(saveSql);

            preparedStatement.setString(1, courseId);
            preparedStatement.setString(2, studentId);
            preparedStatement.setDouble(3, grade);

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void update(String courseId, String studentId, double updatedGrade) {
        try (Connection connection = DBUtil.getConnection()) {
            String updateSql = "update grades set grade = ? where courseId = ? and studentId = ?;";

            PreparedStatement updatePreparedStatement = connection.prepareCall(updateSql);
            updatePreparedStatement.setDouble(1, updatedGrade);
            updatePreparedStatement.setString(2, courseId);
            updatePreparedStatement.setString(3, studentId);

            updatePreparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void delete(String courseId, String studentId) {
        try (Connection connection = DBUtil.getConnection()) {
            String deleteSql = "delete from grades where courseId = ? and studentId = ?;";

            PreparedStatement deletePreparedStatement = connection.prepareCall(deleteSql);
            deletePreparedStatement.setString(1, courseId);
            deletePreparedStatement.setString(2, studentId);

            deletePreparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void deleteByStudent(String studentId) {
        try (Connection connection = DBUtil.getConnection()) {
            String deleteSql = "delete from grades where studentId = ?;";

            PreparedStatement deletePreparedStatement = connection.prepareCall(deleteSql);

            deletePreparedStatement.setString(1, studentId);

            deletePreparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void deleteByCourse(String courseId) {
        try (Connection connection = DBUtil.getConnection()) {
            String deleteSql = "delete from grades where courseId = ?;";

            PreparedStatement deletePreparedStatement = connection.prepareCall(deleteSql);

            deletePreparedStatement.setString(1, courseId);

            deletePreparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
