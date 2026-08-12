package com.keyingym.service;

import com.keyingym.dao.UserDAO;
import com.keyingym.model.User;
import com.keyingym.model.UserRole;
import org.mindrot.jbcrypt.BCrypt;

/**
 * Provides user registration and authentication operations.
 */
public class UserService {

    private final UserDAO userDAO;

    public UserService() {
        this.userDAO = new UserDAO();
    }

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

    public User authenticate(String username, String plainPassword) {
        if (isBlank(username) || isBlank(plainPassword)) {
            return null;
        }

        User user = userDAO.findByUsername(username);

        if (user == null) {
            return null;
        }

        if (!BCrypt.checkpw(plainPassword, user.getPasswordHash())) {
            return null;
        }

        return user;
    }

    private boolean isBlank(String value) {
        return value == null || value.isBlank();
    }
}