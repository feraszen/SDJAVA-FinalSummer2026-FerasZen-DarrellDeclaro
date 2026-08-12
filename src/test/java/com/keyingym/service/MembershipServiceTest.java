package com.keyingym.service;

import com.keyingym.dao.MembershipDAO;
import com.keyingym.dao.MembershipPurchaseDAO;
import com.keyingym.model.Membership;
import com.keyingym.model.MembershipPurchase;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class MembershipServiceTest {

    @Test
    void shouldPurchaseMembershipUsingCurrentMembershipPrice() {

        FakeMembershipDAO membershipDAO =
                new FakeMembershipDAO();

        FakeMembershipPurchaseDAO purchaseDAO =
                new FakeMembershipPurchaseDAO();

        Membership membership =
                new Membership(
                        10,
                        "Monthly",
                        new BigDecimal("45.00")
                );

        membershipDAO.memberships.add(membership);

        MembershipService service =
                new MembershipService(
                        membershipDAO,
                        purchaseDAO
                );

        assertTrue(service.purchaseMembership(25, 10));

        assertNotNull(purchaseDAO.lastPurchase);

        assertEquals(
                25,
                purchaseDAO.lastPurchase.getUserId()
        );

        assertEquals(
                10,
                purchaseDAO.lastPurchase.getMembershipId()
        );

        assertEquals(
                new BigDecimal("45.00"),
                purchaseDAO.lastPurchase.getPrice()
        );

        assertNotNull(
                purchaseDAO.lastPurchase.getPurchasedAt()
        );
    }

    @Test
    void shouldRejectInvalidPurchaseData() {

        FakeMembershipDAO membershipDAO =
                new FakeMembershipDAO();

        FakeMembershipPurchaseDAO purchaseDAO =
                new FakeMembershipPurchaseDAO();

        MembershipService service =
                new MembershipService(
                        membershipDAO,
                        purchaseDAO
                );

        assertFalse(service.purchaseMembership(0, 10));
        assertFalse(service.purchaseMembership(25, 0));

        assertFalse(
                service.purchaseMembership(25, 10)
        );

        assertEquals(
                0,
                purchaseDAO.addCallCount
        );
    }

    @Test
    void shouldReturnAvailableMemberships() {

        FakeMembershipDAO membershipDAO =
                new FakeMembershipDAO();

        FakeMembershipPurchaseDAO purchaseDAO =
                new FakeMembershipPurchaseDAO();

        membershipDAO.memberships.add(
                new Membership(
                        1,
                        "Monthly",
                        new BigDecimal("45.00")
                )
        );

        membershipDAO.memberships.add(
                new Membership(
                        2,
                        "Annual",
                        new BigDecimal("450.00")
                )
        );

        MembershipService service =
                new MembershipService(
                        membershipDAO,
                        purchaseDAO
                );

        List<Membership> memberships =
                service.getAvailableMemberships();

        assertEquals(2, memberships.size());
        assertEquals(
                "Monthly",
                memberships.get(0).getMembershipType()
        );
        assertEquals(
                "Annual",
                memberships.get(1).getMembershipType()
        );
    }

    @Test
    void shouldReturnUserPurchases() {

        FakeMembershipDAO membershipDAO =
                new FakeMembershipDAO();

        FakeMembershipPurchaseDAO purchaseDAO =
                new FakeMembershipPurchaseDAO();

        purchaseDAO.purchases.add(
                new MembershipPurchase(
                        1,
                        25,
                        10,
                        new BigDecimal("45.00"),
                        java.time.LocalDateTime.now()
                )
        );

        MembershipService service =
                new MembershipService(
                        membershipDAO,
                        purchaseDAO
                );

        List<MembershipPurchase> purchases =
                service.getUserPurchases(25);

        assertEquals(1, purchases.size());
        assertEquals(
                25,
                purchases.get(0).getUserId()
        );
        assertEquals(
                10,
                purchases.get(0).getMembershipId()
        );
    }

    @Test
    void shouldRejectInvalidUserIdWhenGettingPurchases() {

        FakeMembershipDAO membershipDAO =
                new FakeMembershipDAO();

        FakeMembershipPurchaseDAO purchaseDAO =
                new FakeMembershipPurchaseDAO();

        MembershipService service =
                new MembershipService(
                        membershipDAO,
                        purchaseDAO
                );

        List<MembershipPurchase> purchases =
                service.getUserPurchases(0);

        assertTrue(purchases.isEmpty());
        assertEquals(
                0,
                purchaseDAO.getPurchasesCallCount
        );
    }

    /**
     * Simple in-memory DAO used to test the service without PostgreSQL.
     */
    private static class FakeMembershipDAO
            extends MembershipDAO {

        private final List<Membership> memberships =
                new ArrayList<>();

        @Override
        public Membership findById(int membershipId) {
            return memberships.stream()
                    .filter(item ->
                            item.getMembershipId() == membershipId)
                    .findFirst()
                    .orElse(null);
        }

        @Override
        public List<Membership> getAllMemberships() {
            return new ArrayList<>(memberships);
        }
    }

    /**
     * Simple in-memory DAO used to test purchase operations.
     */
    private static class FakeMembershipPurchaseDAO
            extends MembershipPurchaseDAO {

        private MembershipPurchase lastPurchase;
        private final List<MembershipPurchase> purchases =
                new ArrayList<>();

        private int addCallCount;
        private int getPurchasesCallCount;

        @Override
        public boolean addMembershipPurchase(
                MembershipPurchase purchase
        ) {
            addCallCount++;
            lastPurchase = purchase;
            purchases.add(purchase);
            return true;
        }

        @Override
        public List<MembershipPurchase> getPurchasesByUserId(
                int userId
        ) {
            getPurchasesCallCount++;

            return purchases.stream()
                    .filter(item ->
                            item.getUserId() == userId)
                    .toList();
        }
    }
}