package com.keyingym.service;

import java.util.ArrayList;
import java.util.List;

import com.keyingym.model.UserRole;

/**
 * Builds the console menu options available to each user role.
 *
 * Menu options are derived from the permissions defined in
 * AuthorizationService.
 */
public class RoleMenuService {

    private final AuthorizationService authorizationService;

    /**
     * Creates the menu service with the application's
     * authorization service.
     */
    public RoleMenuService() {
        this.authorizationService = new AuthorizationService();
    }

    /**
     * Returns the menu options available to the specified role.
     *
     * @param role the user's role
     * @return list of permitted menu options
     */
    public List<String> getMenuOptions(UserRole role) {

        List<String> options = new ArrayList<>();

        if (role == null) {
            return options;
        }

        /*
         * Administrator menu options.
         */
        if (role == UserRole.ADMIN) {

            if (authorizationService.canViewAllUsers(role)) {
                options.add("View All Users");
            }

            if (authorizationService.canDeleteUsers(role)) {
                options.add("Delete User");
            }

            if (authorizationService.canViewMembershipRevenue(role)) {
                options.add("View Membership Revenue");
            }

            if (authorizationService.canManageMerchandise(role)) {
                options.add("Manage Merchandise Inventory");
            }

            if (authorizationService.canManageWorkoutClasses(role)) {
                options.add("Manage Workout Classes");
            }

            if (authorizationService.canExportReports(role)) {
                options.add("Export Reports");
            }
        }

        /*
         * Trainer menu options.
         */
        if (role == UserRole.TRAINER) {

            if (authorizationService.canViewAssignedClasses(role)) {
                options.add("View Assigned Classes");
            }

            if (authorizationService.canManageWorkoutClasses(role)) {
                options.add("Manage Workout Classes");
            }

            if (authorizationService.canPurchaseMembership(role)) {
                options.add("Purchase Membership");
            }

            if (authorizationService.canBrowseMerchandise(role)) {
                options.add("Browse Merchandise");
            }
        }

        /*
         * Member menu options.
         */
        if (role == UserRole.MEMBER) {

            if (authorizationService.canViewOwnMembership(role)) {
                options.add("View My Membership");
            }

            if (authorizationService.canViewOwnExpenses(role)) {
                options.add("View My Expenses");
            }

            if (authorizationService.canBrowseWorkoutClasses(role)) {
                options.add("Browse Workout Classes");
            }

            if (authorizationService.canBrowseMerchandise(role)) {
                options.add("Browse Merchandise");
            }

            if (authorizationService.canPurchaseMembership(role)) {
                options.add("Purchase Membership");
            }
        }

        /*
         * Logout is available to every authenticated role.
         */
        options.add("Logout");

        return options;
    }
}
