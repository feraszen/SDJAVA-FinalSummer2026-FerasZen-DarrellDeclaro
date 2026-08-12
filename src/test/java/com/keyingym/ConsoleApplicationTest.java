package com.keyingym;

import com.keyingym.model.User;
import com.keyingym.model.UserRole;
import com.keyingym.service.RoleMenuService;
import com.keyingym.service.UserService;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.assertTrue;

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

        String input = "testuser\npassword123\n";

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
                    new PrintStream(output, true, StandardCharsets.UTF_8)
            );

            ConsoleApplication application =
                    new ConsoleApplication(
                            fakeUserService,
                            new RoleMenuService(),
                            scanner
                    );

            application.run();

        } finally {
            System.setOut(originalOut);
        }

        String consoleOutput =
                output.toString(StandardCharsets.UTF_8);

        assertTrue(consoleOutput.contains("Login successful."));
        assertTrue(consoleOutput.contains("Welcome, testuser!"));
        assertTrue(consoleOutput.contains("Role: MEMBER"));
        assertTrue(consoleOutput.contains("View My Membership"));
        assertTrue(consoleOutput.contains("View My Expenses"));
        assertTrue(consoleOutput.contains("Browse Workout Classes"));
        assertTrue(consoleOutput.contains("Browse Merchandise"));
        assertTrue(consoleOutput.contains("Purchase Membership"));
        assertTrue(consoleOutput.contains("Logout"));
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

        String input = "wronguser\nwrongpassword\n";

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
                    new PrintStream(output, true, StandardCharsets.UTF_8)
            );

            ConsoleApplication application =
                    new ConsoleApplication(
                            fakeUserService,
                            new RoleMenuService(),
                            scanner
                    );

            application.run();

        } finally {
            System.setOut(originalOut);
        }

        String consoleOutput =
                output.toString(StandardCharsets.UTF_8);

        assertTrue(consoleOutput.contains(
                "Login failed. Exiting application."
        ));
    }
}