package com.keyingym.dao;

import com.keyingym.config.AppLogger;
import com.keyingym.config.DatabaseConnection;
import com.keyingym.model.User;
import com.keyingym.model.UserRole;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object for User records.
 *
 * Handles database operations related to users.
 */
public class UserDAO {

    public boolean addUser(User user) {
        String sql = """
                INSERT INTO users
                    (username, password, email, phone, address, role)
                VALUES (?, ?, ?, ?, ?, ?)
                """;

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, user.getUsername());
            statement.setString(2, user.getPasswordHash());
            statement.setString(3, user.getEmail());
            statement.setString(4, user.getPhone());
            statement.setString(5, user.getAddress());
            statement.setString(6, user.getRole().name());

            return statement.executeUpdate() > 0;

        } catch (SQLException e) {
            AppLogger.error("Database transaction error while adding user.", e);
            return false;
        }
    }

    public User findById(int userId) {
        String sql = """
                SELECT user_id, username, password, email, phone, address, role
                FROM users
                WHERE user_id = ?
                """;

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, userId);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return mapUser(resultSet);
                }
            }

        } catch (SQLException e) {
            AppLogger.error("Database transaction error while finding user: " + userId, e);
        }

        return null;
    }

    public User findByUsername(String username) {
        String sql = """
                SELECT user_id, username, password, email, phone, address, role
                FROM users
                WHERE username = ?
                """;

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, username);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return mapUser(resultSet);
                }
            }

        } catch (SQLException e) {
            AppLogger.error("Database transaction error while finding username: " + username, e);
        }

        return null;
    }

    public List<User> getAllUsers() {
        String sql = """
                SELECT user_id, username, password, email, phone, address, role
                FROM users
                ORDER BY user_id ASC
                """;

        List<User> users = new ArrayList<>();

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                users.add(mapUser(resultSet));
            }

        } catch (SQLException e) {
            AppLogger.error("Database transaction error while loading users.", e);
        }

        return users;
    }

    public boolean updateUser(User user) {
        String sql = """
                UPDATE users
                SET username = ?,
                    password = ?,
                    email = ?,
                    phone = ?,
                    address = ?,
                    role = ?
                WHERE user_id = ?
                """;

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, user.getUsername());
            statement.setString(2, user.getPasswordHash());
            statement.setString(3, user.getEmail());
            statement.setString(4, user.getPhone());
            statement.setString(5, user.getAddress());
            statement.setString(6, user.getRole().name());
            statement.setInt(7, user.getUserId());

            return statement.executeUpdate() > 0;

        } catch (SQLException e) {
            AppLogger.error("Database transaction error while updating user: " + user.getUserId(), e);
            return false;
        }
    }

    public boolean deleteUser(int userId) {
        String sql = "DELETE FROM users WHERE user_id = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, userId);

            return statement.executeUpdate() > 0;

        } catch (SQLException e) {
            AppLogger.error("Database transaction error while deleting user: " + userId, e);
            return false;
        }
    }

    private User mapUser(ResultSet resultSet) throws SQLException {
        User user = new User();

        user.setUserId(resultSet.getInt("user_id"));
        user.setUsername(resultSet.getString("username"));
        user.setPasswordHash(resultSet.getString("password"));
        user.setEmail(resultSet.getString("email"));
        user.setPhone(resultSet.getString("phone"));
        user.setAddress(resultSet.getString("address"));
        user.setRole(UserRole.valueOf(resultSet.getString("role")));

        return user;
    }
}