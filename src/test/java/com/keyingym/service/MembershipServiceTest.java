package com.keyingym.service;

import com.keyingym.dao.MembershipDAO;
import com.keyingym.dao.MembershipPurchaseDAO;
import com.keyingym.model.Membership;
import com.keyingym.model.MembershipPurchase;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class MembershipServiceTest {

    @Test
    void shouldPurchaseMembershipUsingCurrentMembershipPrice() {
        FakeMembershipDAO membershipDAO = new FakeMembershipDAO();
        FakeMembershipPurchaseDAO purchaseDAO = new FakeMembershipPurchaseDAO();

        membershipDAO.memberships.add(
                new Membership(10, "Monthly", new BigDecimal("45.00"))
        );

        MembershipService service = new MembershipService(membershipDAO, purchaseDAO);

        assertTrue(service.purchaseMembership(25, 10));
        assertNotNull(purchaseDAO.lastPurchase);
        assertEquals(25, purchaseDAO.lastPurchase.getUserId());
        assertEquals(10, purchaseDAO.lastPurchase.getMembershipId());
        assertEquals(new BigDecimal("45.00"), purchaseDAO.lastPurchase.getPrice());
        assertNotNull(purchaseDAO.lastPurchase.getPurchasedAt());
    }

    @Test
    void shouldRejectInvalidPurchaseData() {
        FakeMembershipDAO membershipDAO = new FakeMembershipDAO();
        FakeMembershipPurchaseDAO purchaseDAO = new FakeMembershipPurchaseDAO();
        MembershipService service = new MembershipService(membershipDAO, purchaseDAO);

        assertFalse(service.purchaseMembership(0, 10));
        assertFalse(service.purchaseMembership(25, 0));
        assertFalse(service.purchaseMembership(25, 10));
        assertEquals(0, purchaseDAO.addCallCount);
    }

    @Test
    void shouldReturnAvailableMemberships() {
        FakeMembershipDAO membershipDAO = new FakeMembershipDAO();
        FakeMembershipPurchaseDAO purchaseDAO = new FakeMembershipPurchaseDAO();

        membershipDAO.memberships.add(
                new Membership(1, "Monthly", new BigDecimal("45.00"))
        );
        membershipDAO.memberships.add(
                new Membership(2, "Annual", new BigDecimal("450.00"))
        );

        MembershipService service = new MembershipService(membershipDAO, purchaseDAO);
        List<Membership> memberships = service.getAvailableMemberships();

        assertEquals(2, memberships.size());
        assertEquals("Monthly", memberships.get(0).getMembershipType());
        assertEquals("Annual", memberships.get(1).getMembershipType());
    }

    @Test
    void shouldReturnUserPurchases() {
        FakeMembershipDAO membershipDAO = new FakeMembershipDAO();
        FakeMembershipPurchaseDAO purchaseDAO = new FakeMembershipPurchaseDAO();

        purchaseDAO.purchases.add(
                new MembershipPurchase(
                        1, 25, 10, new BigDecimal("45.00"), LocalDateTime.now()
                )
        );

        MembershipService service = new MembershipService(membershipDAO, purchaseDAO);
        List<MembershipPurchase> purchases = service.getUserPurchases(25);

        assertEquals(1, purchases.size());
        assertEquals(25, purchases.get(0).getUserId());
        assertEquals(10, purchases.get(0).getMembershipId());
    }

    @Test
    void shouldRejectInvalidUserIdWhenGettingPurchases() {
        FakeMembershipDAO membershipDAO = new FakeMembershipDAO();
        FakeMembershipPurchaseDAO purchaseDAO = new FakeMembershipPurchaseDAO();
        MembershipService service = new MembershipService(membershipDAO, purchaseDAO);

        List<MembershipPurchase> purchases = service.getUserPurchases(0);

        assertTrue(purchases.isEmpty());
        assertEquals(0, purchaseDAO.getPurchasesCallCount);
    }

    @Test
    void shouldReturnOnlyCurrentYearPurchasesForRevenue() {
        FakeMembershipDAO membershipDAO = new FakeMembershipDAO();
        FakeMembershipPurchaseDAO purchaseDAO = new FakeMembershipPurchaseDAO();

        int currentYear = java.time.LocalDate.now().getYear();
        purchaseDAO.currentYearPurchases.add(
                new MembershipPurchase(
                        100,
                        25,
                        10,
                        new BigDecimal("45.00"),
                        LocalDateTime.of(currentYear, 2, 10, 10, 0)
                )
        );
        purchaseDAO.currentYearPurchases.add(
                new MembershipPurchase(
                        101,
                        26,
                        11,
                        new BigDecimal("450.00"),
                        LocalDateTime.of(currentYear, 8, 1, 12, 0)
                )
        );

        MembershipService service = new MembershipService(membershipDAO, purchaseDAO);
        List<MembershipPurchase> purchases = service.getCurrentYearPurchases();

        assertEquals(2, purchases.size());
        assertEquals(new BigDecimal("495.00"),
                purchases.stream()
                        .map(MembershipPurchase::getPrice)
                        .reduce(BigDecimal.ZERO, BigDecimal::add));
        assertTrue(purchases.stream().allMatch(
                purchase -> purchase.getPurchasedAt().getYear() == currentYear
        ));
    }

    private static class FakeMembershipDAO extends MembershipDAO {
        private final List<Membership> memberships = new ArrayList<>();

        @Override
        public Membership findById(int membershipId) {
            return memberships.stream()
                    .filter(item -> item.getMembershipId() == membershipId)
                    .findFirst()
                    .orElse(null);
        }

        @Override
        public List<Membership> getAllMemberships() {
            return new ArrayList<>(memberships);
        }
    }

    private static class FakeMembershipPurchaseDAO extends MembershipPurchaseDAO {
        private MembershipPurchase lastPurchase;
        private final List<MembershipPurchase> purchases = new ArrayList<>();
        private final List<MembershipPurchase> currentYearPurchases = new ArrayList<>();
        private int addCallCount;
        private int getPurchasesCallCount;

        @Override
        public boolean addMembershipPurchase(MembershipPurchase purchase) {
            addCallCount++;
            lastPurchase = purchase;
            purchases.add(purchase);
            return true;
        }

        @Override
        public List<MembershipPurchase> getPurchasesByUserId(int userId) {
            getPurchasesCallCount++;
            return purchases.stream()
                    .filter(item -> item.getUserId() == userId)
                    .toList();
        }

        @Override
        public List<MembershipPurchase> getCurrentYearMembershipPurchases() {
            return new ArrayList<>(currentYearPurchases);
        }
    }
}