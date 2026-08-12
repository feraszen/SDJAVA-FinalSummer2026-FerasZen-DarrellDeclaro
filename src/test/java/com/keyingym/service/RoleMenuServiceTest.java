package com.keyingym.service;

import com.keyingym.model.UserRole;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class RoleMenuServiceTest {

    private final RoleMenuService roleMenuService =
            new RoleMenuService();

    @Test
    void adminShouldSeeOnlyAdminMenuOptions() {

        List<String> options =
                roleMenuService.getMenuOptions(UserRole.ADMIN);

        assertTrue(options.contains("View All Users"));
        assertTrue(options.contains("Delete User"));
        assertTrue(options.contains("View Membership Revenue"));
        assertTrue(options.contains("Manage Merchandise Inventory"));
        assertTrue(options.contains("Manage Workout Classes"));
        assertTrue(options.contains("Logout"));

        assertFalse(options.contains("View My Membership"));
        assertFalse(options.contains("View My Expenses"));
    }

    @Test
    void trainerShouldSeeOnlyTrainerMenuOptions() {

        List<String> options =
                roleMenuService.getMenuOptions(UserRole.TRAINER);

        assertTrue(options.contains("View Assigned Classes"));
        assertTrue(options.contains("Manage Workout Classes"));
        assertTrue(options.contains("Purchase Membership"));
        assertTrue(options.contains("Browse Merchandise"));
        assertTrue(options.contains("Logout"));

        assertFalse(options.contains("View All Users"));
        assertFalse(options.contains("Delete User"));
        assertFalse(options.contains("View Membership Revenue"));
        assertFalse(options.contains("Manage Merchandise Inventory"));
    }

    @Test
    void memberShouldSeeOnlyMemberMenuOptions() {

        List<String> options =
                roleMenuService.getMenuOptions(UserRole.MEMBER);

        assertTrue(options.contains("View My Membership"));
        assertTrue(options.contains("View My Expenses"));
        assertTrue(options.contains("Browse Workout Classes"));
        assertTrue(options.contains("Browse Merchandise"));
        assertTrue(options.contains("Purchase Membership"));
        assertTrue(options.contains("Logout"));

        assertFalse(options.contains("View All Users"));
        assertFalse(options.contains("Delete User"));
        assertFalse(options.contains("View Membership Revenue"));
        assertFalse(options.contains("Manage Merchandise Inventory"));
        assertFalse(options.contains("Manage Workout Classes"));
    }

    @Test
    void nullRoleShouldReturnEmptyMenu() {

        List<String> options =
                roleMenuService.getMenuOptions(null);

        assertTrue(options.isEmpty());
    }
}