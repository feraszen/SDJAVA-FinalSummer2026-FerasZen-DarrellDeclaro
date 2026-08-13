package com.keyingym.service;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

import com.keyingym.model.UserRole;

class AuthorizationServiceTest {

    private final AuthorizationService authorizationService =
            new AuthorizationService();

    @Test
    void adminShouldHaveAdminPermissions() {
        assertTrue(authorizationService.canViewAllUsers(UserRole.ADMIN));
        assertTrue(authorizationService.canDeleteUsers(UserRole.ADMIN));
        assertTrue(authorizationService.canViewMembershipRevenue(UserRole.ADMIN));
        assertTrue(authorizationService.canManageMerchandise(UserRole.ADMIN));
        assertTrue(authorizationService.canManageWorkoutClasses(UserRole.ADMIN));

        assertTrue(authorizationService.canExportReports(UserRole.ADMIN));
    }

    @Test
    void trainerShouldHaveTrainerPermissions() {
        assertFalse(authorizationService.canViewAllUsers(UserRole.TRAINER));
        assertFalse(authorizationService.canDeleteUsers(UserRole.TRAINER));
        assertFalse(authorizationService.canViewMembershipRevenue(UserRole.TRAINER));
        assertFalse(authorizationService.canManageMerchandise(UserRole.TRAINER));
        assertFalse(authorizationService.canExportReports(UserRole.TRAINER));

        assertTrue(authorizationService.canManageWorkoutClasses(UserRole.TRAINER));
        assertTrue(authorizationService.canViewAssignedClasses(UserRole.TRAINER));
        assertTrue(authorizationService.canPurchaseMembership(UserRole.TRAINER));
        assertTrue(authorizationService.canViewOwnMembership(UserRole.TRAINER));
        assertTrue(authorizationService.canViewOwnExpenses(UserRole.TRAINER));
        assertTrue(authorizationService.canBrowseMerchandise(UserRole.TRAINER));
    }

    @Test
    void memberShouldHaveMemberPermissions() {
        assertFalse(authorizationService.canViewAllUsers(UserRole.MEMBER));
        assertFalse(authorizationService.canDeleteUsers(UserRole.MEMBER));
        assertFalse(authorizationService.canViewMembershipRevenue(UserRole.MEMBER));
        assertFalse(authorizationService.canManageMerchandise(UserRole.MEMBER));
        assertFalse(authorizationService.canManageWorkoutClasses(UserRole.MEMBER));
        assertFalse(authorizationService.canViewAssignedClasses(UserRole.MEMBER));
        assertFalse(authorizationService.canExportReports(UserRole.MEMBER));

        assertTrue(authorizationService.canBrowseWorkoutClasses(UserRole.MEMBER));
        assertTrue(authorizationService.canPurchaseMembership(UserRole.MEMBER));
        assertTrue(authorizationService.canViewOwnMembership(UserRole.MEMBER));
        assertTrue(authorizationService.canViewOwnExpenses(UserRole.MEMBER));
        assertTrue(authorizationService.canBrowseMerchandise(UserRole.MEMBER));
    }

    @Test
    void rolesShouldHaveCorrectWorkoutClassBrowsePermissions() {
        assertFalse(
                authorizationService.canBrowseWorkoutClasses(UserRole.ADMIN)
        );

        assertFalse(
                authorizationService.canBrowseWorkoutClasses(UserRole.TRAINER)
        );

        assertTrue(
                authorizationService.canBrowseWorkoutClasses(UserRole.MEMBER)
        );
    }

    @Test
    void rolesShouldHaveCorrectMerchandiseBrowsePermissions() {
        assertFalse(
                authorizationService.canBrowseMerchandise(UserRole.ADMIN)
        );

        assertTrue(
                authorizationService.canBrowseMerchandise(UserRole.TRAINER)
        );

        assertTrue(
                authorizationService.canBrowseMerchandise(UserRole.MEMBER)
        );
    }
}