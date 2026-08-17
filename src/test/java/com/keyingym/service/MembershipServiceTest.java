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

<<<<<<< HEAD
        membershipDAO.memberships.add(
                new Membership(10, "Monthly", new BigDecimal("45.00"))
        );

        MembershipService service = new MembershipService(membershipDAO, purchaseDAO);
=======
        Membership membership = new Membership(
                10,
                "Monthly",
                new BigDecimal("45.00")
        );
        membershipDAO.memberships.add(membership);

        MembershipService service = new MembershipService(
                membershipDAO,
                purchaseDAO
        );
>>>>>>> fix/final-review-priority

        assertTrue(service.purchaseMembership(25, 10));
        assertNotNull(purchaseDAO.lastPurchase);
        assertEquals(25, purchaseDAO.lastPurchase.getUserId());
        assertEquals(10, purchaseDAO.lastPurchase.getMembershipId());
<<<<<<< HEAD
        assertEquals(new BigDecimal("45.00"), purchaseDAO.lastPurchase.getPrice());
=======
        assertEquals(
                new BigDecimal("45.00"),
                purchaseDAO.lastPurchase.getPrice()
        );
>>>>>>> fix/final-review-priority
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
<<<<<<< HEAD
                        1, 25, 10, new BigDecimal("45.00"), LocalDateTime.now()
=======
                        1,
                        25,
                        10,
                        new BigDecimal("45.00"),
                        LocalDateTime.now()
>>>>>>> fix/final-review-priority
                )
        );

        MembershipService service = new MembershipService(membershipDAO, purchaseDAO);
        List<MembershipPurchase> purchases = service.getUserPurchases(25);

        assertEquals(1, purchases.size());
        assertEquals(25, purchases.get(0).getUserId());
        assertEquals(10, purchases.get(0).getMembershipId());
<<<<<<< HEAD
=======
    }

    @Test
    void shouldReturnPurchasesForRequestedCalendarYear() {
        FakeMembershipDAO membershipDAO = new FakeMembershipDAO();
        FakeMembershipPurchaseDAO purchaseDAO = new FakeMembershipPurchaseDAO();

        int currentYear = LocalDateTime.now().getYear();

        MembershipPurchase currentYearPurchase = new MembershipPurchase(
                1,
                25,
                10,
                new BigDecimal("45.00"),
                LocalDateTime.of(currentYear, 6, 1, 10, 0)
        );

        MembershipPurchase previousYearPurchase = new MembershipPurchase(
                2,
                25,
                10,
                new BigDecimal("450.00"),
                LocalDateTime.of(currentYear - 1, 6, 1, 10, 0)
        );

        purchaseDAO.purchases.add(currentYearPurchase);
        purchaseDAO.purchases.add(previousYearPurchase);

        MembershipService service = new MembershipService(membershipDAO, purchaseDAO);
        List<MembershipPurchase> purchases =
                service.getPurchasesForYear(currentYear);

        assertEquals(1, purchases.size());
        assertEquals(1, purchases.get(0).getPurchaseId());
        assertEquals(currentYear, purchases.get(0).getPurchasedAt().getYear());
>>>>>>> fix/final-review-priority
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

<<<<<<< HEAD
    @Test
    void shouldReturnOnlyCurrentYearPurchasesForRevenue() {
        FakeMembershipDAO membershipDAO = new FakeMembershipDAO();
        FakeMembershipPurchaseDAO purchaseDAO = new FakeMembershipPurchaseDAO();

        int currentYear = java.time.LocalDate.now().getYear();
        purchaseDAO.purchases.add(
                new MembershipPurchase(
                        99,
                        24,
                        9,
                        new BigDecimal("999.00"),
                        LocalDateTime.of(currentYear - 1, 12, 31, 23, 59)
                )
        );
        purchaseDAO.purchases.add(
                new MembershipPurchase(
                        100,
                        25,
                        10,
                        new BigDecimal("45.00"),
                        LocalDateTime.of(currentYear, 2, 10, 10, 0)
                )
        );
        purchaseDAO.purchases.add(
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
        assertTrue(purchases.stream().noneMatch(
                purchase -> purchase.getPurchaseId() == 99
        ));
        assertTrue(purchases.stream().allMatch(
                purchase -> purchase.getPurchasedAt().getYear() == currentYear
        ));
    }

=======
>>>>>>> fix/final-review-priority
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
<<<<<<< HEAD
        public List<MembershipPurchase> getCurrentYearMembershipPurchases() {
            int currentYear = java.time.LocalDate.now().getYear();
            return purchases.stream()
                    .filter(purchase -> purchase.getPurchasedAt() != null)
                    .filter(purchase -> purchase.getPurchasedAt().getYear() == currentYear)
=======
        public List<MembershipPurchase> getMembershipPurchasesForYear(int year) {
            return purchases.stream()
                    .filter(item -> item.getPurchasedAt() != null)
                    .filter(item -> item.getPurchasedAt().getYear() == year)
>>>>>>> fix/final-review-priority
                    .toList();
        }
    }
}
