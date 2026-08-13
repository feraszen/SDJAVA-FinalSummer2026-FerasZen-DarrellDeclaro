package com.keyingym.console;

import java.math.BigDecimal;
import java.util.List;

import com.keyingym.config.AppLogger;
import com.keyingym.model.MembershipPurchase;
import com.keyingym.model.User;
import com.keyingym.model.UserRole;
import com.keyingym.service.MembershipService;
import com.keyingym.service.UserService;

/** Console workflows available to Admin users. */
public class AdminConsole {
    private final UserService userService;
    private final MembershipService membershipService;
    private final ConsoleInput input;
    private final MerchandiseConsole merchandiseConsole;
    private final WorkoutClassConsole workoutClassConsole;

    public AdminConsole(UserService userService, MembershipService membershipService,
                        MerchandiseConsole merchandiseConsole, WorkoutClassConsole workoutClassConsole,
                        ConsoleInput input) {
        this.userService = userService;
        this.membershipService = membershipService;
        this.merchandiseConsole = merchandiseConsole;
        this.workoutClassConsole = workoutClassConsole;
        this.input = input;
    }

    public void viewAllUsers(User user) {
        if (!isAdmin(user)) return;
        List<User> users = userService.getAllUsers();
        System.out.println();
        System.out.println("----------- ALL USERS -----------");
        if (users.isEmpty()) { System.out.println("No users found."); return; }
        for (User currentUser : users) {
            System.out.println("ID: " + currentUser.getUserId()
                    + " | Username: " + currentUser.getUsername()
                    + " | Email: " + currentUser.getEmail()
                    + " | Phone: " + currentUser.getPhone()
                    + " | Role: " + currentUser.getRole());
        }
        System.out.println("---------------------------------");
    }

    public void deleteUser(User user) {
        if (!isAdmin(user)) return;
        System.out.print("Enter user ID to delete: ");
        Integer userId = input.readInteger();
        if (userId == null || userId <= 0) { System.out.println("Invalid user ID."); return; }
        User targetUser = userService.findUserById(userId);
        if (targetUser == null) { System.out.println("User not found."); return; }
        if (targetUser.getUserId() == user.getUserId()) {
            System.out.println("You cannot delete the currently logged-in user.");
            return;
        }
        System.out.println("User found: " + targetUser.getUsername());
        System.out.print("Confirm deletion? Enter Y to continue: ");
        String confirmation = input.readLine();
        if (!"Y".equalsIgnoreCase(confirmation)) { System.out.println("Deletion cancelled."); return; }
        if (userService.deleteUser(userId)) {
            System.out.println("User deleted successfully.");
            AppLogger.info("Admin override: deleted user : " + targetUser.getUsername());
        } else {
            System.out.println("Unable to delete the user.");
        }
    }

    public void viewMembershipRevenue(User user) {
        if (!isAdmin(user)) return;
        List<MembershipPurchase> purchases = membershipService.getAllPurchases();
        BigDecimal totalRevenue = BigDecimal.ZERO;
        System.out.println();
        System.out.println("------- MEMBERSHIP REVENUE -------");
        if (purchases.isEmpty()) { System.out.println("No membership purchases found."); return; }
        for (MembershipPurchase purchase : purchases) {
            BigDecimal price = purchase.getPrice();
            if (price == null) price = BigDecimal.ZERO;
            totalRevenue = totalRevenue.add(price);
            System.out.println("Purchase ID: " + purchase.getPurchaseId()
                    + " | User ID: " + purchase.getUserId()
                    + " | Membership ID: " + purchase.getMembershipId()
                    + " | Price: $" + price
                    + " | Purchased At: " + purchase.getPurchasedAt());
        }
        System.out.println("Total Membership Revenue: $" + totalRevenue);
        System.out.println("----------------------------------");
    }

    public MerchandiseConsole merchandise() { return merchandiseConsole; }
    public WorkoutClassConsole workouts() { return workoutClassConsole; }

    private boolean isAdmin(User user) {
        if (user == null || user.getRole() != UserRole.ADMIN) {
            System.out.println("Access denied. Admin access required.");
            return false;
        }
        return true;
    }
}
