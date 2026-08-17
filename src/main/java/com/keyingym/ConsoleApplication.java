package com.keyingym;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.Scanner;

import com.keyingym.config.AppLogger;
import com.keyingym.console.AdminConsole;
import com.keyingym.console.ConsoleInput;
import com.keyingym.console.MemberConsole;
import com.keyingym.console.MerchandiseConsole;
import com.keyingym.console.WorkoutClassConsole;
import com.keyingym.model.User;
import com.keyingym.model.UserRole;
import com.keyingym.service.MembershipService;
import com.keyingym.service.MerchandiseService;
import com.keyingym.service.ReportExportService;
import com.keyingym.service.RoleMenuService;
import com.keyingym.service.UserService;
import com.keyingym.service.WorkoutClassService;

/**
 * Application entry point and role-based console dispatcher.
 */
public class ConsoleApplication {

    private final UserService userService;
    private final RoleMenuService roleMenuService;
    private final ReportExportService reportExportService;
    private final MembershipService membershipService;
    private final ConsoleInput input;
    private final AdminConsole adminConsole;
    private final MemberConsole memberConsole;
    private final MerchandiseConsole merchandiseConsole;
    private final WorkoutClassConsole workoutClassConsole;

    public ConsoleApplication() {
        this(
                new UserService(),
                new RoleMenuService(),
                new ReportExportService(),
                new MembershipService(),
                new MerchandiseService(),
                new WorkoutClassService(),
                new Scanner(System.in)
        );
    }

    ConsoleApplication(
            UserService userService,
            RoleMenuService roleMenuService,
            ReportExportService reportExportService,
            MembershipService membershipService,
            MerchandiseService merchandiseService,
            WorkoutClassService workoutClassService,
            Scanner scanner
    ) {
        this.userService = userService;
        this.roleMenuService = roleMenuService;
        this.reportExportService = reportExportService;
        this.membershipService = membershipService;
        this.input = new ConsoleInput(scanner);

        this.merchandiseConsole = new MerchandiseConsole(merchandiseService, input);
        this.workoutClassConsole = new WorkoutClassConsole(workoutClassService, input);
        this.adminConsole = new AdminConsole(
                userService,
                membershipService,
                merchandiseConsole,
                workoutClassConsole,
                input
        );
        this.memberConsole = new MemberConsole(
                membershipService,
                merchandiseService,
                merchandiseConsole,
                input
        );
    }

    /** Backward-compatible constructor used by existing tests. */
    ConsoleApplication(
            UserService userService,
            RoleMenuService roleMenuService,
            Scanner scanner
    ) {
        this(
                userService,
                roleMenuService,
                new ReportExportService(),
                new MembershipService(),
                new MerchandiseService(),
                new WorkoutClassService(),
                scanner
        );
    }

    public void run() {
        AppLogger.info("System startup.");

        System.out.println("=================================");
        System.out.println("     GYM MANAGEMENT SYSTEM");
        System.out.println("=================================");

        while (true) {
            System.out.println();
            System.out.println("----------- START -----------");
            System.out.println("1. Login");
            System.out.println("2. Register as Member");
            System.out.println("3. Exit");
            System.out.println("-----------------------------");

            if (!input.hasNextLine()) {
                return;
            }

            System.out.print("Select an option: ");
            Integer option = input.readInteger();

            if (option == null) {
                System.out.println("Invalid option. Please enter a number.");
                continue;
            }

            switch (option) {
                case 1:
                    User authenticatedUser = login();
                    if (authenticatedUser == null) {
                        System.out.println("Login failed. Exiting application.");
                        return;
                    }

                    System.out.println();
                    System.out.println("Login successful.");
                    System.out.println("Welcome, " + authenticatedUser.getUsername() + "!");
                    System.out.println("Role: " + authenticatedUser.getRole());
                    displayMenu(authenticatedUser);
                    return;

                case 2:
                    registerMember();
                    break;

                case 3:
                    System.out.println("Goodbye.");
                    return;

                default:
                    System.out.println("Invalid option. Please try again.");
                    break;
            }
        }
    }

    private User login() {
        System.out.print("Username: ");

        if (!input.hasNextLine()) {
            return null;
        }

        String username = input.readLine();

        System.out.print("Password: ");

        if (!input.hasNextLine()) {
            return null;
        }

        String password = input.readRawLine();

        User authenticatedUser = userService.authenticate(username, password);

        if (authenticatedUser == null) {
            AppLogger.warning("Failed login attempt for username: " + username);
        }

        return authenticatedUser;
    }

    /**
     * Registers a new self-service account as MEMBER.
     * Users cannot self-register as ADMIN or TRAINER.
     */
    private void registerMember() {
        System.out.println();
        System.out.println("------- MEMBER REGISTRATION -------");

        System.out.print("Username: ");
        String username = input.readLine();

        System.out.print("Password: ");
        String password = input.readRawLine();

        System.out.print("Email: ");
        String email = input.readLine();

        System.out.print("Phone number: ");
        String phone = input.readLine();

        System.out.print("Address: ");
        String address = input.readLine();

        if (username == null || username.isBlank()
                || password == null || password.isBlank()
                || email == null || email.isBlank()
                || phone == null || phone.isBlank()
                || address == null || address.isBlank()) {
            System.out.println("Registration failed. All fields are required.");
            return;
        }

        boolean registered = userService.registerUser(
                username,
                password,
                email,
                phone,
                address,
                UserRole.MEMBER
        );

        if (registered) {
            System.out.println("Registration successful. You can now log in as a Member.");
            AppLogger.info("New Member registration completed for username: " + username);
        } else {
            System.out.println("Registration failed. The username may already exist or the data may be invalid.");
        }
    }

    private void displayMenu(User user) {
        while (true) {
            List<String> menuOptions = roleMenuService.getMenuOptions(user.getRole());

            System.out.println();
            System.out.println("----------- MENU -----------");

            for (int i = 0; i < menuOptions.size(); i++) {
                System.out.println((i + 1) + ". " + menuOptions.get(i));
            }

            System.out.println("----------------------------");

            if (!input.hasNextLine()) {
                return;
            }

            System.out.print("Select an option: ");
            Integer selectedOption = input.readInteger();

            if (selectedOption == null) {
                System.out.println("Invalid option. Please enter a number.");
                continue;
            }

            if (selectedOption < 1 || selectedOption > menuOptions.size()) {
                System.out.println("Invalid option. Please try again.");
                continue;
            }

            String selected = menuOptions.get(selectedOption - 1);

            if ("Logout".equals(selected)) {
                AppLogger.info("User logged out: " + user.getUsername());
                System.out.println("Logged out successfully.");
                return;
            }

            handleMenuOption(selected, user);
        }
    }

    private void handleMenuOption(String option, User user) {
        switch (option) {
            case "View All Users":
                adminConsole.viewAllUsers(user);
                break;
            case "Delete User":
                adminConsole.deleteUser(user);
                break;
            case "View Membership Revenue":
                adminConsole.viewMembershipRevenue(user);
                break;
            case "Manage Merchandise Inventory":
                merchandiseConsole.manageInventory(user);
                break;
            case "Manage Workout Classes":
                workoutClassConsole.manage(user);
                break;
            case "Export Reports":
                exportReports(user);
                break;
            case "View Assigned Classes":
<<<<<<< HEAD
                workoutClassConsole.browse(user);
=======
                workoutClassConsole.browseAssigned(user);
>>>>>>> fix/final-review-priority
                break;
            case "Browse Workout Classes":
                workoutClassConsole.browse(user);
                break;
            case "Purchase Membership":
                memberConsole.purchaseMembership(user);
                break;
            case "View My Membership":
                memberConsole.viewMyMembership(user);
                break;
            case "View My Expenses":
                memberConsole.viewMyExpenses(user);
                break;
            case "Browse Merchandise":
                merchandiseConsole.browse(user);
                break;
            default:
                System.out.println("This menu option is not available.");
                break;
        }
    }

    private void exportReports(User user) {
        if (user == null
                || user.getRole() == null
                || !"ADMIN".equals(user.getRole().name())) {
            System.out.println("Access denied. Only Admin users can export reports.");
            AppLogger.warning("Unauthorized report export attempt.");
            return;
        }

        System.out.println();
        System.out.println("------- EXPORT REPORTS -------");
        System.out.println("1. Membership Revenue Report");
        System.out.println("2. Merchandise Inventory Report");
        System.out.println("3. Merchandise Sales Report");
        System.out.println("4. Cancel");
        System.out.println("------------------------------");

<<<<<<< HEAD
        if (!input.hasNextLine()) {
            return;
        }

=======
        if (!input.hasNextLine()) return;
>>>>>>> fix/final-review-priority
        System.out.print("Select report: ");
        Integer selectedReport = input.readInteger();

        if (selectedReport == null) {
            System.out.println("Invalid report option.");
            return;
        }

        try {
            Path report;
            String message;
            String logMessage;

            switch (selectedReport) {
                case 1:
                    report = reportExportService.exportMembershipRevenueReport();
                    message = "Membership Revenue Report exported successfully.";
                    logMessage = "Admin exported membership revenue report: ";
                    break;
                case 2:
                    report = reportExportService.exportMerchandiseInventoryReport();
                    message = "Merchandise Inventory Report exported successfully.";
                    logMessage = "Admin exported merchandise inventory report: ";
                    break;
                case 3:
                    report = reportExportService.exportMerchandiseSalesReport();
                    message = "Merchandise Sales Report exported successfully.";
                    logMessage = "Admin exported merchandise sales report: ";
                    break;
                case 4:
                    System.out.println("Report export cancelled.");
                    return;
                default:
                    System.out.println("Invalid report option.");
                    return;
            }

            System.out.println();
            System.out.println(message);
            System.out.println("File: " + report.toAbsolutePath());
            AppLogger.info(logMessage + user.getUsername());

        } catch (IOException e) {
            System.out.println("Unable to export the report.");
            AppLogger.error(
                    "Report export failed for Admin: " + user.getUsername(),
                    e
            );
        }
    }

    public static void main(String[] args) {
        new ConsoleApplication().run();
    }
}
