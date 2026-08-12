package com.keyingym.service;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

import com.keyingym.dao.UserDAO;
import com.keyingym.model.User;
import com.keyingym.model.UserRole;

class UserServiceTest {

    @Test
    void shouldRegisterAndAuthenticateUser() {
        UserService userService = new UserService();
        UserDAO userDAO = new UserDAO();

        String uniqueId = UUID.randomUUID().toString().substring(0, 8);
        String username = "service_user_" + uniqueId;
        String password = "SecurePassword123!";

        try {
            assertTrue(userService.registerUser(
                    username,
                    password,
                    username + "@example.com",
                    "709-555-0100",
                    "Test Address",
                    UserRole.MEMBER
            ));

            User registeredUser = userDAO.findByUsername(username);
            assertNotNull(registeredUser);

            assertFalse(password.equals(registeredUser.getPasswordHash()));

            User authenticatedUser =
                    userService.authenticate(username, password);

            assertNotNull(authenticatedUser);
            assertTrue(authenticatedUser.getUsername().equals(username));

            assertNull(userService.authenticate(
                    username,
                    "wrong-password"
            ));

            assertFalse(userService.registerUser(
                    username,
                    password,
                    username + "@example.com",
                    "709-555-0100",
                    "Test Address",
                    UserRole.MEMBER
            ));

        } finally {
            User remainingUser = userDAO.findByUsername(username);

            if (remainingUser != null) {
                userDAO.deleteUser(remainingUser.getUserId());
            }
        }
    }
}
