package com.keyingym;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.Scanner;

import com.keyingym.config.AppLogger;
import com.keyingym.model.User;
import com.keyingym.model.UserRole;
import com.keyingym.service.ReportExportService;
import com.keyingym.service.RoleMenuService;
import com.keyingym.service.UserService;

/**
 * Main console application for the Gym Management System.
 *
 * Handles user authentication, role-based menus, and
 * administrative report export operations.
 */
public class ConsoleApplication {

    private final UserService userService;
    private final RoleMenuService roleMenuService;
    private final ReportExportService reportExportService;
    private final Scanner scanner;

    /**
     * Creates the application with the real application services.
     */
    public ConsoleApplication() {
        this(
                new UserService(),
                new RoleMenuService(),
                new ReportExportService(),
                new Scanner(System.in)
        );
    }

    /**
     * Constructor used for testing with supplied services and input.
     *
     * @param userService service responsible for authentication
     * @param roleMenuService service responsible for role-based menus
     * @param reportExportService service responsible for report exports
     * @param scanner console input scanner
     */
    ConsoleApplication(
            UserService userService,
            RoleMenuService roleMenuService,
            ReportExportService reportExportService,
            Scanner scanner
    ) {
        this.userService = userService;
        this.roleMenuService = roleMenuService;
        this.reportExportService = reportExportService;
        this.scanner = scanner;
    }

    /**
     * Backward-compatible constructor used by existing tests.
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
        this(
                userService,
                roleMenuService,
                new ReportExportService(),
                scanner
        );
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

        System.out.println(
                "Welcome, "
                        + authenticatedUser.getUsername()
                        + "!"
        );

        System.out.println(
                "Role: "
                        + authenticatedUser.getRole()
        );

        displayMenu(authenticatedUser);
    }

    /**
     * Handles the login process.
     *
     * Failed login attempts are recorded in the application log.
     *
     * @return authenticated user, or null when authentication fails
     */
    private User login() {

        System.out.print("Username: ");

        if (!scanner.hasNextLine()) {
            return null;
        }

        String username = scanner.nextLine();

        System.out.print("Password: ");

        if (!scanner.hasNextLine()) {
            return null;
        }

        String password = scanner.nextLine();

        User authenticatedUser =
                userService.authenticate(
                        username,
                        password
                );

        if (authenticatedUser == null) {
            AppLogger.warning(
                    "Failed login attempt for username: "
                            + username
            );
        }

        return authenticatedUser;
    }

    /**
     * Displays and processes the menu available to the authenticated user.
     *
     * @param user authenticated user
     */
    private void displayMenu(User user) {

        while (true) {

            List<String> menuOptions =
                    roleMenuService.getMenuOptions(
                            user.getRole()
                    );

            System.out.println();
            System.out.println("----------- MENU -----------");

            for (int i = 0; i < menuOptions.size(); i++) {

                System.out.println(
                        (i + 1)
                                + ". "
                                + menuOptions.get(i)
                );
            }

            System.out.println("----------------------------");

            /*
             * Prevents NoSuchElementException when the input stream
             * has no more lines, such as during automated tests.
             */
            if (!scanner.hasNextLine()) {
                return;
            }

            System.out.print("Select an option: ");

            String input = scanner.nextLine();

            int selectedOption;

            try {

                selectedOption =
                        Integer.parseInt(input);

            } catch (NumberFormatException e) {

                System.out.println(
                        "Invalid option. Please enter a number."
                );

                continue;
            }

            if (selectedOption < 1
                    || selectedOption > menuOptions.size()) {

                System.out.println(
                        "Invalid option. Please try again."
                );

                continue;
            }

            String selectedMenuOption =
                    menuOptions.get(
                            selectedOption - 1
                    );

            if ("Export Reports".equals(selectedMenuOption)
                    && user.getRole() == UserRole.ADMIN) {

                exportReports(user);

                continue;
            }

            if ("Logout".equals(selectedMenuOption)) {

                AppLogger.info(
                        "User logged out: "
                                + user.getUsername()
                );

                System.out.println(
                        "Logged out successfully."
                );

                break;
            }

            System.out.println(
                    "Selected: "
                            + selectedMenuOption
            );
        }
    }

    /**
     * Displays the Admin report selection menu and exports
     * the selected report.
     *
     * @param user authenticated Admin user
     */
    private void exportReports(User user) {

        if (user == null
                || user.getRole() != UserRole.ADMIN) {

            System.out.println(
                    "Access denied. Only Admin users can export reports."
            );

            AppLogger.warning(
                    "Unauthorized report export attempt."
            );

            return;
        }

        System.out.println();
        System.out.println("------- EXPORT REPORTS -------");
        System.out.println("1. Membership Revenue Report");
        System.out.println("2. Merchandise Inventory Report");
        System.out.println("3. Cancel");
        System.out.println("------------------------------");

        /*
         * Prevents NoSuchElementException when there is no
         * additional input available.
         */
        if (!scanner.hasNextLine()) {
            return;
        }

        System.out.print("Select report: ");

        String input = scanner.nextLine();

        int selectedReport;

        try {

            selectedReport =
                    Integer.parseInt(input);

        } catch (NumberFormatException e) {

            System.out.println(
                    "Invalid report option."
            );

            return;
        }

        try {

            if (selectedReport == 1) {

                Path report =
                        reportExportService
                                .exportMembershipRevenueReport();

                System.out.println();
                System.out.println(
                        "Membership Revenue Report exported successfully."
                );

                System.out.println(
                        "File: "
                                + report.toAbsolutePath()
                );

                AppLogger.info(
                        "Admin exported membership revenue report: "
                                + user.getUsername()
                );

                return;
            }

            if (selectedReport == 2) {

                Path report =
                        reportExportService
                                .exportMerchandiseInventoryReport();

                System.out.println();
                System.out.println(
                        "Merchandise Inventory Report exported successfully."
                );

                System.out.println(
                        "File: "
                                + report.toAbsolutePath()
                );

                AppLogger.info(
                        "Admin exported merchandise inventory report: "
                                + user.getUsername()
                );

                return;
            }

            if (selectedReport == 3) {

                System.out.println(
                        "Report export cancelled."
                );

                return;
            }

            System.out.println(
                    "Invalid report option."
            );

        } catch (IOException e) {

            System.out.println(
                    "Unable to export the report."
            );

            /*
             * AppLogger.error requires:
             * error(String message, Exception exception)
             */
            AppLogger.error(
                    "Report export failed for Admin: "
                            + user.getUsername(),
                    e
            );
        }
    }

    /**
     * Starts the application.
     *
     * @param args command-line arguments
     */
    public static void main(String[] args) {

        ConsoleApplication application =
                new ConsoleApplication();

        application.run();
    }
}
