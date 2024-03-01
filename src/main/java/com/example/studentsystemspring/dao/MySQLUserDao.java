package com.example.studentsystemspring.dao;

import com.example.studentsystemspring.models.Course;
import com.example.studentsystemspring.models.User;
import com.example.studentsystemspring.models.UserType;
import com.example.studentsystemspring.util.DBUtil;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Service
public class MySQLUserDao implements UserDao {
    @Override
    public User get(String id) {
        try (Connection connection = DBUtil.getConnection()) {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("select * from users where ID = '" + id + "';");

            if (resultSet.next()) {
                UserType userType = switch (resultSet.getString("user_type")) {
                    case "0" -> UserType.ADMIN;
                    case "1" -> UserType.INSTRUCTOR;
                    case "2" -> UserType.STUDENT;
                    default -> throw new IllegalStateException("Unexpected value: " + resultSet.getString("userType"));
                };
                return new User(id, resultSet.getString("password"), resultSet.getString("name"), userType);
            }

            return null;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<User> getAll() {
        try (Connection connection = DBUtil.getConnection()) {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("select * from users;");
            List<User> users = new ArrayList<>();

            while (resultSet.next()) {
                UserType userType = switch (resultSet.getString("userType")) {
                    case "admin" -> UserType.ADMIN;
                    case "instructor" -> UserType.INSTRUCTOR;
                    case "student" -> UserType.STUDENT;
                    default -> throw new IllegalStateException("Unexpected value: " + resultSet.getString("userType"));
                };
                users.add(new User(resultSet.getString("id"), resultSet.getString("password"), resultSet.getString("name"), userType));
            }

            return users;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<User> getStudents() {
        try (Connection connection = DBUtil.getConnection()) {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("select * from users where user_type = '2';");
            List<User> students = new ArrayList<>();

            while (resultSet.next())
                students.add(new User(resultSet.getString("id"), resultSet.getString("password"), resultSet.getString("name"), UserType.STUDENT));

            return students;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<User> getInstructors() {
        try (Connection connection = DBUtil.getConnection()) {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("select * from users where user_type = '1';");
            List<User> instructors = new ArrayList<>();

            while (resultSet.next())
                instructors.add(new User(resultSet.getString("id"), resultSet.getString("password"), resultSet.getString("name"), UserType.INSTRUCTOR));

            return instructors;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void save(String id, String password, String name, UserType userType) {
        try (Connection connection = DBUtil.getConnection()) {
            String saveSql = switch (userType) {
                case ADMIN -> "insert into users values (?, ?, ?, 'admin');";
                case INSTRUCTOR -> "insert into users values (?, ?, ?, 'instructor');";
                case STUDENT -> "insert into users values (?, ?, ?, 'student');";
            };

            PreparedStatement preparedStatement = connection.prepareCall(saveSql);
            preparedStatement.setString(1, id);
            preparedStatement.setString(2, password);
            preparedStatement.setString(3, name);

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void update(String id, String password, String name) {
        try (Connection connection = DBUtil.getConnection()) {
            String updateSql = "update users set password = ?, name = ? where id = ?;";

            PreparedStatement updatePreparedStatement = connection.prepareCall(updateSql);
            updatePreparedStatement.setString(1, new BCryptPasswordEncoder().encode(password));
            updatePreparedStatement.setString(2, name);
            updatePreparedStatement.setString(3, id);

            updatePreparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void delete(String id) {
        try (Connection connection = DBUtil.getConnection()) {
            User user = get(id);
            String deleteSql = "delete from users where id = ?;";
            
            PreparedStatement deletePreparedStatement = connection.prepareCall(deleteSql);
            deletePreparedStatement.setString(1, id);

            if (user.getUserType() == UserType.STUDENT) {
                MySQLEnrollmentDao mySQLEnrollmentDao = new MySQLEnrollmentDao();
                List<Course> courses = mySQLEnrollmentDao.getStudentCourses(id);
                for (Course course : courses) mySQLEnrollmentDao.delete(course.getId(), id);
            }
            else if (user.getUserType() == UserType.INSTRUCTOR) {
                MySQLCourseDao mySQLCourseDao = new MySQLCourseDao();
                List<Course> courses = mySQLCourseDao.getAllByInstructorId(id);
                for (Course course : courses) mySQLCourseDao.delete(course.getId());
            }

            deletePreparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
