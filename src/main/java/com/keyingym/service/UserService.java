package com.keyingym.service;

import java.util.Collections;
import java.util.List;

import org.mindrot.jbcrypt.BCrypt;

import com.keyingym.dao.UserDAO;
import com.keyingym.model.User;
import com.keyingym.model.UserRole;

/**
 * Provides user registration, authentication, and user management operations.
 */
public class UserService {

    private final UserDAO userDAO;

    /**
     * Creates the service using the application's real DAO.
     */
    public UserService() {
        this.userDAO = new UserDAO();
    }

    /**
     * Registers a new user.
     *
     * @param username username
     * @param plainPassword plain-text password
     * @param email email address
     * @param phone phone number
     * @param address user address
     * @param role user role
     * @return true when the user is successfully registered
     */
    public boolean registerUser(
            String username,
            String plainPassword,
            String email,
            String phone,
            String address,
            UserRole role
    ) {
        if (isBlank(username)
                || isBlank(plainPassword)
                || isBlank(email)
                || isBlank(phone)
                || isBlank(address)
                || role == null) {
            return false;
        }

        if (userDAO.findByUsername(username) != null) {
            return false;
        }

        String passwordHash = BCrypt.hashpw(
                plainPassword,
                BCrypt.gensalt()
        );

        User user = new User(
                0,
                username,
                passwordHash,
                email,
                phone,
                address,
                role
        );

        return userDAO.addUser(user);
    }

    /**
     * Authenticates a user using username and password.
     *
     * @param username username
     * @param plainPassword plain-text password
     * @return authenticated user, or null when authentication fails
     */
    public User authenticate(
            String username,
            String plainPassword
    ) {
        if (isBlank(username) || isBlank(plainPassword)) {
            return null;
        }

        User user = userDAO.findByUsername(username);

        if (user == null) {
            return null;
        }

        if (!BCrypt.checkpw(
                plainPassword,
                user.getPasswordHash()
        )) {
            return null;
        }

        return user;
    }

    /**
     * Returns all users.
     *
     * @return list of all users
     */
    public List<User> getAllUsers() {
        List<User> users = userDAO.getAllUsers();

        if (users == null) {
            return Collections.emptyList();
        }

        return users;
    }

    /**
     * Finds a user by ID.
     *
     * @param userId user ID
     * @return matching user, or null when not found
     */
    public User findUserById(int userId) {
        if (userId <= 0) {
            return null;
        }

        return userDAO.findById(userId);
    }

    /**
     * Deletes a user by ID.
     *
     * @param userId user ID
     * @return true when the user is successfully deleted
     */
    public boolean deleteUser(int userId) {
        if (userId <= 0) {
            return false;
        }

        return userDAO.deleteUser(userId);
    }

    /**
     * Checks whether a string is null or blank.
     */
    private boolean isBlank(String value) {
        return value == null || value.isBlank();
    }
}