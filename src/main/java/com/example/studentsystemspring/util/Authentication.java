package com.example.studentsystemspring.util;

import com.example.studentsystemspring.models.User;
import com.example.studentsystemspring.models.UserType;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Authentication {

    public static User authenticateUser(String id, String password) {
        try (Connection connection = DBUtil.getConnection()) {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("select * from users where ID = '" + id + "' AND password = '" + password + "';");
            User user = null;

            if (resultSet.next()) {
                UserType userType = switch (resultSet.getString("userType")) {
                    case "admin" -> UserType.ADMIN;
                    case "instructor" -> UserType.INSTRUCTOR;
                    case "student" -> UserType.STUDENT;
                    default -> throw new IllegalStateException("Unexpected value: " + resultSet.getString("userType"));
                };
                user = new User(id, password, resultSet.getString("name"), userType);
            }

            return user;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
