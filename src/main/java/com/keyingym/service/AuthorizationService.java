package com.keyingym.service;

import com.keyingym.model.UserRole;

/**
 * Provides role-based access control for the Gym Management System.
 *
 * The permissions are based on the functional requirements for
 * Admin, Trainer, and Member roles.
 */
public class AuthorizationService {

    /**
     * Checks whether the role can view all users.
     *
     * @param role the user's role
     * @return true only for Admin
     */
    public boolean canViewAllUsers(UserRole role) {
        return role == UserRole.ADMIN;
    }

    /**
     * Checks whether the role can delete users.
     *
     * @param role the user's role
     * @return true only for Admin
     */
    public boolean canDeleteUsers(UserRole role) {
        return role == UserRole.ADMIN;
    }

    /**
     * Checks whether the role can track membership revenue.
     *
     * @param role the user's role
     * @return true only for Admin
     */
    public boolean canViewMembershipRevenue(UserRole role) {
        return role == UserRole.ADMIN;
    }

    /**
     * Checks whether the role can manage merchandise inventory.
     *
     * @param role the user's role
     * @return true only for Admin
     */
    public boolean canManageMerchandise(UserRole role) {
        return role == UserRole.ADMIN;
    }

    /**
     * Checks whether the role can create, update, and delete workoutclasses.
     *
     * @param role the user's role
     * @return true for Admin and Trainer
     */
    public boolean canManageWorkoutClasses(UserRole role) {
        return role == UserRole.ADMIN
                || role == UserRole.TRAINER;
    }

    /**
     * Checks whether the role can view classes assigned to the trainer.
     *
     * @param role the user's role
     * @return true only for Trainer
     */
    public boolean canViewAssignedClasses(UserRole role) {
        return role == UserRole.TRAINER;
    }

    /**
     * Checks whether the role can browse available workout classes.
     *
     * @param role the user's role
     * @return true only for Member
     */
    public boolean canBrowseWorkoutClasses(UserRole role) {
        return role == UserRole.MEMBER;
    }

    /**
     * Checks whether the role can purchase a membership.
     *
     * @param role the user's role
     * @return true for Trainer and Member
     */
    public boolean canPurchaseMembership(UserRole role) {
        return role == UserRole.TRAINER
                || role == UserRole.MEMBER;
    }

    /**
     * Checks whether the role can view its own membership.
     *
     * @param role the user's role
     * @return true for Trainer and Member
     */
    public boolean canViewOwnMembership(UserRole role) {
        return role == UserRole.TRAINER
                || role == UserRole.MEMBER;
    }

    /**
     * Checks whether the role can view its own membership expenses.
     *
     * @param role the user's role
     * @return true for Trainer and Member
     */
    public boolean canViewOwnExpenses(UserRole role) {
        return role == UserRole.TRAINER
                || role == UserRole.MEMBER;
    }

    /**
     * Checks whether the role can browse merchandise available
     * for purchase.
     *
     * @param role the user's role
     * @return true for Trainer and Member
     */
    public boolean canBrowseMerchandise(UserRole role) {
        return role == UserRole.TRAINER
                || role == UserRole.MEMBER;
    }

    /**
     * Checks whether the role can export reports.
     *
     * @param role the user's role
     * @return true only for Admin
     */
    public boolean canExportReports(UserRole role) {
        return role == UserRole.ADMIN;
    }
}
