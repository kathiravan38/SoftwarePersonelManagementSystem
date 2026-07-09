package com.spms.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Utility class to obtain a JDBC connection to MySQL.
 * Update DB_URL, DB_USER, DB_PASS to match your environment.
 */
public class DBConnection {

    private static final String DB_URL  = "jdbc:mysql://localhost:3306/spms_db";
    private static final String DB_USER = "root"; 
    private static final String DB_PASS = "kathiravanL8326"; 
  
    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("MySQL JDBC Driver not found. Add mysql-connector-j.jar to WEB-INF/lib.", e);
        }
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
    }
}
