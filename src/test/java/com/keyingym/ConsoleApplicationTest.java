package com.keyingym;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

import com.keyingym.model.User;
import com.keyingym.model.UserRole;
import com.keyingym.service.RoleMenuService;
import com.keyingym.service.UserService;

class ConsoleApplicationTest {

    @Test
    void shouldLoginAndDisplayMemberMenu() {

        UserService fakeUserService = new UserService() {
            @Override
            public User authenticate(
                    String username,
                    String plainPassword
            ) {
                if ("testuser".equals(username)
                        && "password123".equals(plainPassword)) {

                    return new User(
                            1,
                            "testuser",
                            "hashed-password",
                            "test@example.com",
                            "709-555-0100",
                            "123 Main Street",
                            UserRole.MEMBER
                    );
                }

                return null;
            }
        };

        String output = runApplication(
                fakeUserService,
                "testuser\npassword123\n"
        );

        assertTrue(output.contains("Login successful."));
        assertTrue(output.contains("Welcome, testuser!"));
        assertTrue(output.contains("Role: MEMBER"));
        assertTrue(output.contains("View My Membership"));
        assertTrue(output.contains("View My Expenses"));
        assertTrue(output.contains("Browse Workout Classes"));
        assertTrue(output.contains("Browse Merchandise"));
        assertTrue(output.contains("Purchase Membership"));
        assertTrue(output.contains("Logout"));
    }

    @Test
    void shouldRejectInvalidLogin() {

        UserService fakeUserService = new UserService() {
            @Override
            public User authenticate(
                    String username,
                    String plainPassword
            ) {
                return null;
            }
        };

        String output = runApplication(
                fakeUserService,
                "wronguser\nwrongpassword\n"
        );

        assertTrue(
                output.contains(
                        "Login failed. Exiting application."
                )
        );
    }

    /**
     * Runs the console application with simulated input
     * and captures the console output.
     */
    private String runApplication(
            UserService userService,
            String input
    ) {

        Scanner scanner = new Scanner(
                new ByteArrayInputStream(
                        input.getBytes(StandardCharsets.UTF_8)
                )
        );

        ByteArrayOutputStream output =
                new ByteArrayOutputStream();

        PrintStream originalOut = System.out;

        try {
            System.setOut(
                    new PrintStream(
                            output,
                            true,
                            StandardCharsets.UTF_8
                    )
            );

            ConsoleApplication application =
                    new ConsoleApplication(
                            userService,
                            new RoleMenuService(),
                            scanner
                    );

            application.run();

        } finally {
            System.setOut(originalOut);
            scanner.close();
        }

        return output.toString(StandardCharsets.UTF_8);
    }
}