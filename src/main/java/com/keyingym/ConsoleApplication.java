package com.keyingym;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Scanner;

import com.keyingym.config.AppLogger;
import com.keyingym.model.Membership;
import com.keyingym.model.MembershipPurchase;
import com.keyingym.model.Merchandise;
import com.keyingym.model.MerchandisePurchase;
import com.keyingym.model.User;
import com.keyingym.model.UserRole;
import com.keyingym.model.WorkoutClass;
import com.keyingym.service.MembershipService;
import com.keyingym.service.MerchandiseService;
import com.keyingym.service.ReportExportService;
import com.keyingym.service.RoleMenuService;
import com.keyingym.service.UserService;
import com.keyingym.service.WorkoutClassService;

/**
 * Main console application for the Gym Management System.
 *
 * Handles authentication, role-based menus, user management,
 * memberships, merchandise, workout classes, and report exports.
 */
public class ConsoleApplication {

    private final UserService userService;
    private final RoleMenuService roleMenuService;
    private final ReportExportService reportExportService;
    private final MembershipService membershipService;
    private final MerchandiseService merchandiseService;
    private final WorkoutClassService workoutClassService;
    private final Scanner scanner;

    /**
     * Creates the application with the real application services.
     */
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

    /**
     * Constructor used for testing with supplied services and input.
     *
     * @param userService user service
     * @param roleMenuService role menu service
     * @param reportExportService report export service
     * @param membershipService membership service
     * @param merchandiseService merchandise service
     * @param workoutClassService workout class service
     * @param scanner console input scanner
     */
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
        this.merchandiseService = merchandiseService;
        this.workoutClassService = workoutClassService;
        this.scanner = scanner;
    }

    /**
     * Backward-compatible constructor used by existing tests.
     *
     * @param userService user service
     * @param roleMenuService role menu service
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
                new MembershipService(),
                new MerchandiseService(),
                new WorkoutClassService(),
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
     * @return authenticated user, or null when authentication fails
     */
    private User login() {

        System.out.print("Username: ");

        if (!scanner.hasNextLine()) {
            return null;
        }

        String username = scanner.nextLine().trim();

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
     * Displays and processes the menu for the authenticated user.
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

            if (!scanner.hasNextLine()) {
                return;
            }

            System.out.print("Select an option: ");

            String input = scanner.nextLine();

            int selectedOption;

            try {
                selectedOption = Integer.parseInt(input);
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
                    menuOptions.get(selectedOption - 1);

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

            handleMenuOption(
                    selectedMenuOption,
                    user
            );
        }
    }

    /**
     * Executes the selected menu operation.
     *
     * @param selectedMenuOption selected menu option
     * @param user authenticated user
     */
    private void handleMenuOption(
            String selectedMenuOption,
            User user
    ) {

        switch (selectedMenuOption) {

            case "View All Users":
                viewAllUsers(user);
                break;

            case "Delete User":
                deleteUser(user);
                break;

            case "View Membership Revenue":
                viewMembershipRevenue(user);
                break;

            case "Manage Merchandise Inventory":
                manageMerchandiseInventory(user);
                break;

            case "Manage Workout Classes":
                manageWorkoutClasses(user);
                break;

            case "Export Reports":
                exportReports(user);
                break;

            case "View Assigned Classes":
                viewWorkoutClasses(user);
                break;

            case "Purchase Membership":
                purchaseMembership(user);
                break;

            case "View My Membership":
                viewMyMembership(user);
                break;

            case "View My Expenses":
                viewMyExpenses(user);
                break;

            case "Browse Workout Classes":
                viewWorkoutClasses(user);
                break;

            case "Browse Merchandise":
                browseMerchandise(user);
                break;

            default:
                System.out.println(
                        "This menu option is not available."
                );
                break;
        }
    }

    /**
     * Displays all users to an Admin.
     */
    private void viewAllUsers(User user) {

        if (user == null
                || user.getRole() != UserRole.ADMIN) {

            System.out.println(
                    "Access denied. Admin access required."
            );

            return;
        }

        List<User> users = userService.getAllUsers();

        System.out.println();
        System.out.println("----------- ALL USERS -----------");

        if (users.isEmpty()) {
            System.out.println("No users found.");
            return;
        }

        for (User currentUser : users) {

            System.out.println(
                    "ID: "
                            + currentUser.getUserId()
                            + " | Username: "
                            + currentUser.getUsername()
                            + " | Email: "
                            + currentUser.getEmail()
                            + " | Phone: "
                            + currentUser.getPhone()
                            + " | Role: "
                            + currentUser.getRole()
            );
        }

        System.out.println("---------------------------------");
    }

    /**
     * Deletes a user by ID.
     */
    private void deleteUser(User user) {

        if (user == null
                || user.getRole() != UserRole.ADMIN) {

            System.out.println(
                    "Access denied. Admin access required."
            );

            return;
        }

        System.out.print("Enter user ID to delete: ");

        if (!scanner.hasNextLine()) {
            return;
        }

        Integer userId = readInteger();

        if (userId == null || userId <= 0) {
            System.out.println("Invalid user ID.");
            return;
        }

        User targetUser =
                userService.findUserById(userId);

        if (targetUser == null) {
            System.out.println("User not found.");
            return;
        }

        if (targetUser.getUserId() == user.getUserId()) {
            System.out.println(
                    "You cannot delete the currently logged-in user."
            );
            return;
        }

        System.out.println(
                "User found: "
                        + targetUser.getUsername()
        );

        System.out.print(
                "Confirm deletion? Enter Y to continue: "
        );

        if (!scanner.hasNextLine()) {
            return;
        }

        String confirmation =
                scanner.nextLine().trim();

        if (!"Y".equalsIgnoreCase(confirmation)) {
            System.out.println("Deletion cancelled.");
            return;
        }

        boolean deleted =
                userService.deleteUser(userId);

        if (deleted) {
            System.out.println(
                    "User deleted successfully."
            );

            AppLogger.info(
                    "Admin override: deleted user : "
                            + targetUser.getUsername()
            );
        } else {
            System.out.println(
                    "Unable to delete the user."
            );
        }
    }

    /**
     * Displays membership revenue.
     */
    private void viewMembershipRevenue(User user) {

        if (user == null
                || user.getRole() != UserRole.ADMIN) {

            System.out.println(
                    "Access denied. Admin access required."
            );

            return;
        }

        List<MembershipPurchase> purchases =
                membershipService.getAllPurchases();

        BigDecimal totalRevenue = BigDecimal.ZERO;

        System.out.println();
        System.out.println(
                "------- MEMBERSHIP REVENUE -------"
        );

        if (purchases.isEmpty()) {
            System.out.println(
                    "No membership purchases found."
            );
            return;
        }

        for (MembershipPurchase purchase : purchases) {

            BigDecimal price = purchase.getPrice();

            if (price == null) {
                price = BigDecimal.ZERO;
            }

            totalRevenue =
                    totalRevenue.add(price);

            System.out.println(
                    "Purchase ID: "
                            + purchase.getPurchaseId()
                            + " | User ID: "
                            + purchase.getUserId()
                            + " | Membership ID: "
                            + purchase.getMembershipId()
                            + " | Price: $"
                            + price
                            + " | Purchased At: "
                            + purchase.getPurchasedAt()
            );
        }

        System.out.println(
                "Total Membership Revenue: $"
                        + totalRevenue
        );

        System.out.println(
                "----------------------------------"
        );
    }

    /**
     * Displays the merchandise management menu for Admin users.
     */
    private void manageMerchandiseInventory(User user) {

        if (user == null
                || user.getRole() != UserRole.ADMIN) {

            System.out.println(
                    "Access denied. Admin access required."
            );

            return;
        }

        while (true) {

            System.out.println();
            System.out.println(
                    "----- MERCHANDISE INVENTORY -----"
            );
            System.out.println("1. View Inventory");
            System.out.println("2. Add Merchandise");
            System.out.println("3. Update Merchandise");
            System.out.println("4. Delete Merchandise");
            System.out.println("5. Return");
            System.out.println("---------------------------------");

            if (!scanner.hasNextLine()) {
                return;
            }

            System.out.print("Select an option: ");

            Integer option = readInteger();

            if (option == null) {
                System.out.println("Invalid option.");
                continue;
            }

            switch (option) {

                case 1:
                    browseMerchandise(user);
                    break;

                case 2:
                    addMerchandise();
                    break;

                case 3:
                    updateMerchandise();
                    break;

                case 4:
                    deleteMerchandise();
                    break;

                case 5:
                    return;

                default:
                    System.out.println(
                            "Invalid option."
                    );
                    break;
            }
        }
    }

    /**
     * Displays available merchandise.
     */
    private void browseMerchandise(User user) {

        List<Merchandise> merchandise =
                merchandiseService.getAvailableMerchandise();

        System.out.println();
        System.out.println(
                "----------- MERCHANDISE -----------"
        );

        if (merchandise.isEmpty()) {
            System.out.println(
                    "No merchandise found."
            );
            return;
        }

        for (Merchandise item : merchandise) {

            System.out.println(
                    "ID: "
                            + item.getMerchId()
                            + " | Product: "
                            + item.getProductName()
                            + " | Type: "
                            + item.getType()
                            + " | Price: $"
                            + item.getPrice()
                            + " | Stock: "
                            + item.getCurrentStock()
            );
        }

        System.out.println(
                "-----------------------------------"
        );

        if (user != null
                && user.getRole() != UserRole.ADMIN) {

            System.out.print(
                    "Would you like to purchase merchandise? "
                            + "Enter Y to continue: "
            );

            if (!scanner.hasNextLine()) {
                return;
            }

            String answer =
                    scanner.nextLine().trim();

            if ("Y".equalsIgnoreCase(answer)) {
                purchaseMerchandise(user);
            }
        }
    }

    /**
     * Adds new merchandise.
     */
    private void addMerchandise() {

        System.out.println();
        System.out.println("----- ADD MERCHANDISE -----");

        System.out.print("Product name: ");
        String productName = readLine();

        System.out.print("Type: ");
        String type = readLine();

        System.out.print("Price: ");
        BigDecimal price = readBigDecimal();

        System.out.print("Current stock: ");
        Integer stock = readInteger();

        if (productName == null
                || type == null
                || price == null
                || stock == null
                || price.signum() < 0
                || stock < 0) {

            System.out.println(
                    "Invalid merchandise information."
            );

            return;
        }

        Merchandise merchandise =
                new Merchandise(
                        0,
                        productName,
                        type,
                        price,
                        stock
                );

        if (merchandiseService.addMerchandise(
                merchandise
        )) {

            System.out.println(
                    "Merchandise added successfully."
            );

            AppLogger.info(
                    "Admin override: added merchandise "
                     + merchandise.getProductName()
            );

        } else {
            System.out.println(
                    "Unable to add merchandise."
            );
        }
    }

    /**
     * Updates existing merchandise.
     */
    private void updateMerchandise() {

        System.out.print(
                "Enter merchandise ID to update: "
        );

        Integer merchId = readInteger();

        if (merchId == null || merchId <= 0) {
            System.out.println("Invalid merchandise ID.");
            return;
        }

        Merchandise existing =
                merchandiseService.findMerchandiseById(
                        merchId
                );

        if (existing == null) {
            System.out.println("Merchandise not found.");
            return;
        }

        System.out.println(
                "Current product: "
                        + existing.getProductName()
        );

        System.out.print(
                "New product name: "
        );

        String productName = readLine();

        System.out.print("New type: ");
        String type = readLine();

        System.out.print("New price: ");
        BigDecimal price = readBigDecimal();

        System.out.print("New stock: ");
        Integer stock = readInteger();

        if (productName == null
                || type == null
                || price == null
                || stock == null
                || productName.isBlank()
                || type.isBlank()
                || price.signum() < 0
                || stock < 0) {

            System.out.println(
                    "Invalid merchandise information."
            );

            return;
        }

        existing.setProductName(productName);
        existing.setType(type);
        existing.setPrice(price);
        existing.setCurrentStock(stock);

        if (merchandiseService.updateMerchandise(
                existing
        )) {

            System.out.println(
                    "Merchandise updated successfully."
            );

            AppLogger.info(
            "Admin override: updated merchandise "
                    + existing.getProductName()
         );

        } else {
            System.out.println(
                    "Unable to update merchandise."
            );
        }
    }

    /**
     * Deletes merchandise by ID.
     */
    private void deleteMerchandise() {

        System.out.print(
                "Enter merchandise ID to delete: "
        );

        Integer merchId = readInteger();

        if (merchId == null || merchId <= 0) {
            System.out.println(
                    "Invalid merchandise ID."
            );
            return;
        }

        Merchandise merchandise =
                merchandiseService.findMerchandiseById(
                        merchId
                );

        if (merchandise == null) {
            System.out.println(
                    "Merchandise not found."
            );
            return;
        }

        System.out.print(
                "Confirm deletion? Enter Y to continue: "
        );

        String confirmation = readLine();

        if (!"Y".equalsIgnoreCase(confirmation)) {
            System.out.println(
                    "Deletion cancelled."
            );
            return;
        }

        if (merchandiseService.deleteMerchandise(
                merchId
        )) {

            System.out.println(
                    "Merchandise deleted successfully."
            );

            AppLogger.info(
            "Admin override: deleted merchandise ID "
                    + merchId
            );

        } else {
            System.out.println(
                    "Unable to delete merchandise."
            );
        }
    }

    /**
     * Displays workout classes.
     */
    private void viewWorkoutClasses(User user) {

        List<WorkoutClass> classes =
                workoutClassService.getSafeWorkoutClasses();

        System.out.println();
        System.out.println(
                "---------- WORKOUT CLASSES ----------"
        );

        if (classes.isEmpty()) {
            System.out.println(
                    "No workout classes found."
            );
            return;
        }

        for (WorkoutClass workoutClass : classes) {

            System.out.println(
                    "ID: "
                            + workoutClass.getClassId()
                            + " | Name: "
                            + workoutClass.getClassName()
                            + " | Description: "
                            + workoutClass.getDescription()
                            + " | Trainer ID: "
                            + workoutClass.getTrainerId()
                            + " | Scheduled: "
                            + workoutClass.getScheduledAt()
            );
        }

        System.out.println(
                "-------------------------------------"
        );
    }

    /**
     * Manages workout classes for Admin and Trainer users.
     */
    private void manageWorkoutClasses(User user) {

        if (user == null
                || (user.getRole() != UserRole.ADMIN
                && user.getRole() != UserRole.TRAINER)) {

            System.out.println(
                    "Access denied."
            );

            return;
        }

        while (true) {

            System.out.println();
            System.out.println(
                    "------- WORKOUT CLASSES -------"
            );
            System.out.println("1. View Classes");
            System.out.println("2. Add Class");
            System.out.println("3. Update Class");
            System.out.println("4. Delete Class");
            System.out.println("5. Return");
            System.out.println("-------------------------------");

            System.out.print("Select an option: ");

            if (!scanner.hasNextLine()) {
                return;
            }

            Integer option = readInteger();

            if (option == null) {
                System.out.println("Invalid option.");
                continue;
            }

            switch (option) {

                case 1:
                    viewWorkoutClasses(user);
                    break;

                case 2:
                    addWorkoutClass();
                    break;

                case 3:
                    updateWorkoutClass();
                    break;

                case 4:
                    deleteWorkoutClass();
                    break;

                case 5:
                    return;

                default:
                    System.out.println(
                            "Invalid option."
                    );
                    break;
            }
        }
    }

    /**
     * Adds a workout class.
     */
    private void addWorkoutClass() {

        System.out.println();
        System.out.println("--------- ADD CLASS ---------");

        System.out.print("Class name: ");
        String className = readLine();

        System.out.print("Description: ");
        String description = readLine();

        System.out.print("Trainer ID: ");
        Integer trainerId = readInteger();

        System.out.print(
                "Scheduled date/time "
                        + "(YYYY-MM-DDTHH:MM): "
        );

        LocalDateTime scheduledAt =
                readDateTime();

        if (className == null
                || description == null
                || trainerId == null
                || trainerId <= 0
                || scheduledAt == null) {

            System.out.println(
                    "Invalid workout class information."
            );

            return;
        }

        WorkoutClass workoutClass =
                new WorkoutClass(
                        0,
                        className,
                        description,
                        trainerId,
                        scheduledAt
                );

        if (workoutClassService.addWorkoutClass(
                workoutClass
        )) {

            System.out.println(
                    "Workout class added successfully."
            );

        } else {
            System.out.println(
                    "Unable to add workout class."
            );
        }
    }

    /**
     * Updates a workout class.
     */
    private void updateWorkoutClass() {

        System.out.print(
                "Enter class ID to update: "
        );

        Integer classId = readInteger();

        if (classId == null || classId <= 0) {
            System.out.println(
                    "Invalid class ID."
            );
            return;
        }

        WorkoutClass workoutClass =
                workoutClassService.findWorkoutClassById(
                        classId
                );

        if (workoutClass == null) {
            System.out.println(
                    "Workout class not found."
            );
            return;
        }

        System.out.print("New class name: ");
        String className = readLine();

        System.out.print("New description: ");
        String description = readLine();

        System.out.print("New trainer ID: ");
        Integer trainerId = readInteger();

        System.out.print(
                "New scheduled date/time "
                        + "(YYYY-MM-DDTHH:MM): "
        );

        LocalDateTime scheduledAt =
                readDateTime();

        if (className == null
                || description == null
                || trainerId == null
                || trainerId <= 0
                || scheduledAt == null
                || className.isBlank()) {

            System.out.println(
                    "Invalid workout class information."
            );

            return;
        }

        workoutClass.setClassName(className);
        workoutClass.setDescription(description);
        workoutClass.setTrainerId(trainerId);
        workoutClass.setScheduledAt(scheduledAt);

        if (workoutClassService.updateWorkoutClass(
                workoutClass
        )) {

            System.out.println(
                    "Workout class updated successfully."
            );

        } else {
            System.out.println(
                    "Unable to update workout class."
            );
        }
    }

    /**
     * Deletes a workout class.
     */
    private void deleteWorkoutClass() {

        System.out.print(
                "Enter class ID to delete: "
        );

        Integer classId = readInteger();

        if (classId == null || classId <= 0) {
            System.out.println(
                    "Invalid class ID."
            );
            return;
        }

        WorkoutClass workoutClass =
                workoutClassService.findWorkoutClassById(
                        classId
                );

        if (workoutClass == null) {
            System.out.println(
                    "Workout class not found."
            );
            return;
        }

        System.out.print(
                "Confirm deletion? Enter Y to continue: "
        );

        String confirmation = readLine();

        if (!"Y".equalsIgnoreCase(confirmation)) {
            System.out.println(
                    "Deletion cancelled."
            );
            return;
        }

        if (workoutClassService.deleteWorkoutClass(
                classId
        )) {

            System.out.println(
                    "Workout class deleted successfully."
            );

        } else {
            System.out.println(
                    "Unable to delete workout class."
            );
        }
    }

    /**
     * Displays available memberships and allows a purchase.
     */
    private void purchaseMembership(User user) {

        if (user == null) {
            System.out.println(
                    "Authentication required."
            );
            return;
        }

        List<Membership> memberships =
                membershipService.getAvailableMemberships();

        System.out.println();
        System.out.println(
                "----------- MEMBERSHIPS -----------"
        );

        if (memberships.isEmpty()) {
            System.out.println(
                    "No membership plans found."
            );
            return;
        }

        for (Membership membership : memberships) {

            System.out.println(
                    "ID: "
                            + membership.getMembershipId()
                            + " | Type: "
                            + membership.getMembershipType()
                            + " | Price: $"
                            + membership.getPrice()
            );
        }

        System.out.println(
                "-----------------------------------"
        );

        System.out.print(
                "Enter membership ID to purchase "
                        + "or 0 to cancel: "
        );

        Integer membershipId = readInteger();

        if (membershipId == null) {
            System.out.println("Invalid membership ID.");
            return;
        }

        if (membershipId == 0) {
            System.out.println(
                    "Purchase cancelled."
            );
            return;
        }

        Membership membership =
                membershipService.findMembershipById(
                        membershipId
                );

        if (membership == null) {
            System.out.println(
                    "Membership not found."
            );
            return;
        }

        boolean purchased =
                membershipService.purchaseMembership(
                        user.getUserId(),
                        membershipId
                );

        if (purchased) {

            System.out.println(
                    "Membership purchased successfully."
            );

            System.out.println(
                    "Membership: "
                            + membership.getMembershipType()
                            + " | Price: $"
                            + membership.getPrice()
            );

        } else {
            System.out.println(
                    "Unable to purchase membership."
            );
        }
    }

    /**
     * Displays the authenticated user's membership purchases.
     */
    private void viewMyMembership(User user) {

        if (user == null) {
            return;
        }

        List<MembershipPurchase> purchases =
                membershipService.getUserPurchases(
                        user.getUserId()
                );

        System.out.println();
        System.out.println(
                "--------- MY MEMBERSHIP ---------"
        );

        if (purchases.isEmpty()) {
            System.out.println(
                    "No membership purchases found."
            );
            return;
        }

        for (MembershipPurchase purchase : purchases) {

            System.out.println(
                    "Purchase ID: "
                            + purchase.getPurchaseId()
                            + " | Membership ID: "
                            + purchase.getMembershipId()
                            + " | Price: $"
                            + purchase.getPrice()
                            + " | Purchased At: "
                            + purchase.getPurchasedAt()
            );
        }
    }

    /**
     * Displays the authenticated user's expenses.
     */
    private void viewMyExpenses(User user) {

        if (user == null) {
            return;
        }

        List<MembershipPurchase> membershipPurchases =
                membershipService.getUserPurchases(
                        user.getUserId()
                );

        List<MerchandisePurchase> merchandisePurchases =
                merchandiseService.getUserPurchases(
                        user.getUserId()
                );

        BigDecimal total = BigDecimal.ZERO;

        System.out.println();
        System.out.println(
                "----------- MY EXPENSES -----------"
        );

        System.out.println("Membership Purchases:");

        for (MembershipPurchase purchase :
                membershipPurchases) {

            BigDecimal price = purchase.getPrice();

            if (price == null) {
                price = BigDecimal.ZERO;
            }

            total = total.add(price);

            System.out.println(
                    "Purchase ID: "
                            + purchase.getPurchaseId()
                            + " | Membership ID: "
                            + purchase.getMembershipId()
                            + " | Amount: $"
                            + price
            );
        }

        System.out.println("Merchandise Purchases:");

        for (MerchandisePurchase purchase :
                merchandisePurchases) {

            BigDecimal unitPrice =
                    purchase.getUnitPrice();

            if (unitPrice == null) {
                unitPrice = BigDecimal.ZERO;
            }

            BigDecimal itemTotal =
                    unitPrice.multiply(
                            BigDecimal.valueOf(
                                    purchase.getQuantity()
                            )
                    );

            total = total.add(itemTotal);

            System.out.println(
                    "Purchase ID: "
                            + purchase.getPurchaseId()
                            + " | Merchandise ID: "
                            + purchase.getMerchId()
                            + " | Quantity: "
                            + purchase.getQuantity()
                            + " | Amount: $"
                            + itemTotal
            );
        }

        System.out.println(
                "Total Expenses: $"
                        + total
        );

        System.out.println(
                "----------------------------------"
        );
    }

    /**
     * Purchases merchandise for a member or trainer.
     */
    private void purchaseMerchandise(User user) {

        if (user == null) {
            return;
        }

        List<Merchandise> merchandise =
                merchandiseService.getAvailableMerchandise();

        if (merchandise.isEmpty()) {
            System.out.println(
                    "No merchandise available."
            );
            return;
        }

        System.out.print(
                "Enter merchandise ID to purchase "
                        + "or 0 to cancel: "
        );

        Integer merchId = readInteger();

        if (merchId == null) {
            System.out.println(
                    "Invalid merchandise ID."
            );
            return;
        }

        if (merchId == 0) {
            System.out.println(
                    "Purchase cancelled."
            );
            return;
        }

        Merchandise item =
                merchandiseService.findMerchandiseById(
                        merchId
                );

        if (item == null) {
            System.out.println(
                    "Merchandise not found."
            );
            return;
        }

        System.out.println(
                "Selected: "
                        + item.getProductName()
                        + " | Price: $"
                        + item.getPrice()
                        + " | Stock: "
                        + item.getCurrentStock()
        );

        System.out.print("Quantity: ");

        Integer quantity = readInteger();

        if (quantity == null || quantity <= 0) {
            System.out.println(
                    "Invalid quantity."
            );
            return;
        }

        if (quantity > item.getCurrentStock()) {
            System.out.println(
                    "Insufficient stock."
            );
            return;
        }

        boolean purchased =
                merchandiseService.purchaseMerchandise(
                        user.getUserId(),
                        merchId,
                        quantity
                );

        if (purchased) {
            System.out.println(
                    "Merchandise purchased successfully."
            );
        } else {
            System.out.println(
                    "Unable to complete the purchase."
            );
        }
    }

    /**
     * Displays the Admin report selection menu.
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
        System.out.println("3. Merchandise Sales Report");
        System.out.println("4. Cancel");
        System.out.println("------------------------------");

        if (!scanner.hasNextLine()) {
            return;
        }

        System.out.print("Select report: ");

        Integer selectedReport = readInteger();

        if (selectedReport == null) {
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

                Path report =
                        reportExportService
                                .exportMerchandiseSalesReport();

                System.out.println();
                System.out.println(
                        "Merchandise Sales Report exported successfully."
                );

                System.out.println(
                        "File: "
                                + report.toAbsolutePath()
                );

                AppLogger.info(
                        "Admin exported merchandise sales report: "
                                + user.getUsername()
                );

                return;
            }

            if (selectedReport == 4) {

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

            AppLogger.error(
                    "Report export failed for Admin: "
                            + user.getUsername(),
                    e
            );
        }
    }

    /**
     * Reads an integer safely from console input.
     *
     * @return integer value, or null when invalid
     */
    private Integer readInteger() {

        String input = readLine();

        if (input == null || input.isBlank()) {
            return null;
        }

        try {
            return Integer.parseInt(input);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    /**
     * Reads a BigDecimal safely from console input.
     *
     * @return decimal value, or null when invalid
     */
    private BigDecimal readBigDecimal() {

        String input = readLine();

        if (input == null || input.isBlank()) {
            return null;
        }

        try {
            return new BigDecimal(input);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    /**
     * Reads a LocalDateTime safely from console input.
     *
     * @return date/time value, or null when invalid
     */
    private LocalDateTime readDateTime() {

        String input = readLine();

        if (input == null || input.isBlank()) {
            return null;
        }

        try {
            return LocalDateTime.parse(input);
        } catch (Exception e) {
            System.out.println(
                    "Invalid date/time format."
            );
            return null;
        }
    }

    /**
     * Reads a line from the console.
     *
     * @return input line, or null when no input remains
     */
    private String readLine() {

        if (!scanner.hasNextLine()) {
            return null;
        }

        return scanner.nextLine().trim();
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
