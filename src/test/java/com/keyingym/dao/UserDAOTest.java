package com.keyingym.dao;

import com.keyingym.model.User;
import com.keyingym.model.UserRole;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class UserDAOTest {

    @Test
    void shouldPerformUserCrudOperations() {
        UserDAO userDAO = new UserDAO();
        String uniqueId = UUID.randomUUID().toString().substring(0, 8);
        String username = "dao_" + uniqueId;
        int userId = 0;

        try {
            User user = new User(
                    0,
                    username,
                    "test-password-hash",
                    username + "@example.com",
                    "709-555-0100",
                    "Test Address",
                    UserRole.MEMBER
            );

            assertTrue(userDAO.addUser(user));

            User createdUser = userDAO.findByUsername(username);
            assertNotNull(createdUser);
            userId = createdUser.getUserId();

            assertEquals(username, createdUser.getUsername());
            assertEquals(UserRole.MEMBER, createdUser.getRole());

            assertNotNull(userDAO.findById(userId));

            createdUser.setEmail("updated_" + username + "@example.com");
            createdUser.setPhone("709-555-0199");
            createdUser.setRole(UserRole.TRAINER);

            assertTrue(userDAO.updateUser(createdUser));

            User updatedUser = userDAO.findById(userId);
            assertNotNull(updatedUser);
            assertEquals(createdUser.getEmail(), updatedUser.getEmail());
            assertEquals("709-555-0199", updatedUser.getPhone());
            assertEquals(UserRole.TRAINER, updatedUser.getRole());

            assertTrue(userDAO.deleteUser(userId));
            assertNull(userDAO.findById(userId));

        } finally {
            User remainingUser = userDAO.findByUsername(username);

            if (remainingUser != null) {
                userDAO.deleteUser(remainingUser.getUserId());
            }
        }
    }
}