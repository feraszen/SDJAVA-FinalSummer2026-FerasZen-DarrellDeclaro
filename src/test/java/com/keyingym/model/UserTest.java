package com.keyingym.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;

class UserTest {

    @Test
    void shouldCreateUserWithExpectedValues() {
        User user = new User(
                1,
                "testuser",
                "hashed-password",
                "test@example.com",
                "709-555-0100",
                "123 Main Street",
                UserRole.MEMBER
        );

        assertNotNull(user);
        assertEquals(1, user.getUserId());
        assertEquals("testuser", user.getUsername());
        assertEquals("hashed-password", user.getPasswordHash());
        assertEquals("test@example.com", user.getEmail());
        assertEquals("709-555-0100", user.getPhone());
        assertEquals("123 Main Street", user.getAddress());
        assertEquals(UserRole.MEMBER, user.getRole());
    }
}
