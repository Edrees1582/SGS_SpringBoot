package com.example.studentsystemspring.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBUtil {
    public static Connection getConnection() throws SQLException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return DriverManager.getConnection("jdbc:mysql://localhost/sgs" , "root", "Atypon#123");
    }
}
