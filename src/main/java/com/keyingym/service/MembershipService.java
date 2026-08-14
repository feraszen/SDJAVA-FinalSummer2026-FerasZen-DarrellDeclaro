package com.keyingym.service;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import com.keyingym.dao.MembershipDAO;
import com.keyingym.dao.MembershipPurchaseDAO;
import com.keyingym.model.Membership;
import com.keyingym.model.MembershipPurchase;

/**
 * Provides business operations for gym memberships and membership purchases.
 */
public class MembershipService {

    private final MembershipDAO membershipDAO;
    private final MembershipPurchaseDAO purchaseDAO;

    public MembershipService() {
        this(new MembershipDAO(), new MembershipPurchaseDAO());
    }

    MembershipService(
            MembershipDAO membershipDAO,
            MembershipPurchaseDAO purchaseDAO
    ) {
        this.membershipDAO = membershipDAO;
        this.purchaseDAO = purchaseDAO;
    }

    public List<Membership> getAvailableMemberships() {
        List<Membership> memberships = membershipDAO.getAllMemberships();

        if (memberships == null) {
            return Collections.emptyList();
        }

        return memberships;
    }

    public Membership findMembershipById(int membershipId) {
        if (membershipId <= 0) {
            return null;
        }

        return membershipDAO.findById(membershipId);
    }

    public boolean purchaseMembership(int userId, int membershipId) {
        if (userId <= 0 || membershipId <= 0) {
            return false;
        }

        Membership membership = membershipDAO.findById(membershipId);

        if (membership == null || membership.getPrice() == null) {
            return false;
        }

        MembershipPurchase purchase = new MembershipPurchase(
                0,
                userId,
                membershipId,
                membership.getPrice(),
                LocalDateTime.now().withNano(0)
        );

        return purchaseDAO.addMembershipPurchase(purchase);
    }

    public List<MembershipPurchase> getUserPurchases(int userId) {
        if (userId <= 0) {
            return Collections.emptyList();
        }

        List<MembershipPurchase> purchases =
                purchaseDAO.getPurchasesByUserId(userId);

        if (purchases == null) {
            return Collections.emptyList();
        }

        return purchases;
    }

    public List<MembershipPurchase> getAllPurchases() {
        List<MembershipPurchase> purchases =
                purchaseDAO.getAllMembershipPurchases();

        if (purchases == null) {
            return Collections.emptyList();
        }

        return purchases;
    }

    /**
     * Returns only membership purchases from the requested calendar year.
     */
    public List<MembershipPurchase> getPurchasesForYear(int year) {
        if (year < 1 || year > 9998) {
            return Collections.emptyList();
        }

        List<MembershipPurchase> purchases =
                purchaseDAO.getMembershipPurchasesForYear(year);

        if (purchases == null) {
            return Collections.emptyList();
        }

        return purchases;
    }
}
