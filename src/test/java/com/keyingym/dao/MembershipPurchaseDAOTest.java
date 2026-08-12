package com.keyingym.dao;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

import com.keyingym.model.Membership;
import com.keyingym.model.MembershipPurchase;
import com.keyingym.model.User;
import com.keyingym.model.UserRole;

class MembershipPurchaseDAOTest {

    @Test
    void shouldCreateAndReadMembershipPurchase() {
        UserDAO userDAO = new UserDAO();
        MembershipDAO membershipDAO = new MembershipDAO();
        MembershipPurchaseDAO purchaseDAO = new MembershipPurchaseDAO();

        String uniqueId = UUID.randomUUID().toString().substring(0, 8);
        String username = "purchase_user_" + uniqueId;
        String membershipType = "Purchase Plan " + uniqueId;

        int userId = 0;
        int membershipId = 0;
        int purchaseId = 0;

        try {
            User user = new User(
                    0,
                    username,
                    "test-password-hash",
                    username + "@example.com",
                    "709-555-0100",
                    "Test Address",
                    UserRole.MEMBER
            );
            assertTrue(userDAO.addUser(user));

            User createdUser = userDAO.findByUsername(username);
            assertNotNull(createdUser);
            userId = createdUser.getUserId();

            Membership membership = new Membership(
                    0,
                    membershipType,
                    new BigDecimal("45.00")
            );
            assertTrue(membershipDAO.addMembership(membership));

            Membership createdMembership =
                    membershipDAO.findByMembershipType(membershipType);
            assertNotNull(createdMembership);
            membershipId = createdMembership.getMembershipId();

            final int createdMembershipId = membershipId;

            LocalDateTime purchasedAt =
                    LocalDateTime.now().withNano(0);

            MembershipPurchase purchase = new MembershipPurchase(
                    0,
                    userId,
                    membershipId,
                    new BigDecimal("45.00"),
                    purchasedAt
            );
            assertTrue(purchaseDAO.addMembershipPurchase(purchase));

            MembershipPurchase createdPurchase = purchaseDAO
                    .getPurchasesByUserId(userId)
                    .stream()
                    .filter(item ->
                            item.getMembershipId() == createdMembershipId)
                    .findFirst()
                    .orElse(null);

            assertNotNull(createdPurchase);
            purchaseId = createdPurchase.getPurchaseId();

            assertEquals(userId, createdPurchase.getUserId());
            assertEquals(membershipId, createdPurchase.getMembershipId());
            assertEquals(new BigDecimal("45.00"), createdPurchase.getPrice());
            assertEquals(purchasedAt, createdPurchase.getPurchasedAt());

            assertNotNull(purchaseDAO.findById(purchaseId));
            assertTrue(purchaseDAO.deleteMembershipPurchase(purchaseId));
            assertNull(purchaseDAO.findById(purchaseId));

        } finally {
            if (purchaseId != 0) {
                purchaseDAO.deleteMembershipPurchase(purchaseId);
            }

            if (membershipId != 0) {
                membershipDAO.deleteMembership(membershipId);
            }

            if (userId != 0) {
                userDAO.deleteUser(userId);
            }
        }
    }
}