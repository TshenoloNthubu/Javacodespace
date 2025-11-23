package com.banking.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseConnection {

    private static final String URL = "jdbc:h2:mem:banking;DB_CLOSE_DELAY=-1";
    private static final String USER = "sa";
    private static final String PASSWORD = "";

    private static Connection connection;

    public static Connection getConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            connection = DriverManager.getConnection(URL, USER, PASSWORD);
        }
        return connection;
    }

    public static void initializeDatabase() {
        try (Connection conn = getConnection(); Statement stmt = conn.createStatement()) {

            // Create customers table (NO TEXT BLOCKS)
            String createCustomersTable =
                    "CREATE TABLE IF NOT EXISTS customers (" +
                            "customer_id VARCHAR(20) PRIMARY KEY, " +
                            "first_name VARCHAR(50) NOT NULL, " +
                            "surname VARCHAR(50) NOT NULL, " +
                            "address VARCHAR(200), " +
                            "phone_number VARCHAR(15), " +
                            "email VARCHAR(100), " +
                            "created_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP" +
                            ");";

            // Create accounts table
            String createAccountsTable =
                    "CREATE TABLE IF NOT EXISTS accounts (" +
                            "account_number VARCHAR(20) PRIMARY KEY, " +
                            "customer_id VARCHAR(20) NOT NULL, " +
                            "account_type VARCHAR(20) NOT NULL, " +
                            "balance DECIMAL(15,2) DEFAULT 0.00, " +
                            "branch VARCHAR(50), " +
                            "employer_name VARCHAR(100), " +
                            "company_address VARCHAR(200), " +
                            "created_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP, " +
                            "FOREIGN KEY (customer_id) REFERENCES customers(customer_id)" +
                            ");";

            stmt.execute(createCustomersTable);
            stmt.execute(createAccountsTable);

            System.out.println("Database tables created successfully");

        } catch (SQLException e) {
            System.err.println("Error initializing database: " + e.getMessage());
        }
    }
}
