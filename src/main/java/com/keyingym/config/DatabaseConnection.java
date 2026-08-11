package com.keyingym.config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Provides PostgreSQL database connections for the application.
 *
 * Database credentials are read from environment variables so that
 * sensitive information is not stored in the source code.
 */
public final class DatabaseConnection {

    private static final String DB_URL = getRequiredEnvironmentVariable("GYM_DB_URL");
    private static final String DB_USER = getRequiredEnvironmentVariable("GYM_DB_USER");
    private static final String DB_PASSWORD = getRequiredEnvironmentVariable("GYM_DB_PASSWORD");

    private DatabaseConnection() {
        // Prevent instantiation because this class only provides database connections.
    }

    /**
     * Creates a new connection to the PostgreSQL database.
     *
     * @return an active database connection
     * @throws SQLException if the database connection cannot be established
     */
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
    }

    private static String getRequiredEnvironmentVariable(String name) {
        String value = System.getenv(name);

        if (value == null || value.isBlank()) {
            throw new IllegalStateException(
                    "Required environment variable is not configured: " + name
            );
        }

        return value;
    }
}