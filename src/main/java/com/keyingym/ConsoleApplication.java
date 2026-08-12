package com.keyingym;

import java.util.List;
import java.util.Scanner;

import com.keyingym.config.AppLogger;
import com.keyingym.model.User;
import com.keyingym.service.RoleMenuService;
import com.keyingym.service.UserService;

/**
 * Main console application for the Gym Management System.
 *
 * Handles user login and displays the menu available to the
 * authenticated user's role.
 */
public class ConsoleApplication {

    private final UserService userService;
    private final RoleMenuService roleMenuService;
    private final Scanner scanner;

    /**
     * Creates the application with its real services.
     */
    public ConsoleApplication() {
        this(
                new UserService(),
                new RoleMenuService(),
                new Scanner(System.in)
        );
    }

    /**
     * Constructor used to provide services and input for testing.
     *
     * @param userService service responsible for authentication
     * @param roleMenuService service responsible for role-based menus
     * @param scanner console input scanner
     */
    ConsoleApplication(
            UserService userService,
            RoleMenuService roleMenuService,
            Scanner scanner
    ) {
        this.userService = userService;
        this.roleMenuService = roleMenuService;
        this.scanner = scanner;
    }

    /**
     * Starts the console application.
     */
    public void run() {

        AppLogger.info("System startup.");

        System.out.println("=================================");
        System.out.println("     GYM MANAGEMENT SYSTEM");
        System.out.println("=================================");

        User authenticatedUser = login();

        if (authenticatedUser == null) {
            System.out.println("Login failed. Exiting application.");
            return;
        }

        System.out.println();
        System.out.println("Login successful.");
        System.out.println("Welcome, "
                + authenticatedUser.getUsername()
                + "!");
        System.out.println("Role: "
                + authenticatedUser.getRole());

        displayMenu(authenticatedUser);
    }

    /**
     * Handles the login process.
     *
     * @return authenticated user, or null when authentication fails
     */
    private User login() {

        System.out.print("Username: ");
        String username = scanner.nextLine();

        System.out.print("Password: ");
        String password = scanner.nextLine();

        return userService.authenticate(username, password);
    }

    /**
     * Displays the menu available to the authenticated user.
     *
     * @param user authenticated user
     */
    private void displayMenu(User user) {

        List<String> menuOptions =
                roleMenuService.getMenuOptions(user.getRole());

        System.out.println();
        System.out.println("----------- MENU -----------");

        for (int i = 0; i < menuOptions.size(); i++) {
            System.out.println((i + 1) + ". " + menuOptions.get(i));
        }

        System.out.println("----------------------------");
    }

    public static void main(String[] args) {
        ConsoleApplication application =
                new ConsoleApplication();

        application.run();
    }
}