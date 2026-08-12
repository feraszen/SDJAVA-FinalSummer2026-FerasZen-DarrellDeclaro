package com.keyingym.service;

import com.keyingym.dao.MembershipDAO;
import com.keyingym.dao.MembershipPurchaseDAO;
import com.keyingym.model.Membership;
import com.keyingym.model.MembershipPurchase;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

/**
 * Provides business operations for gym memberships and membership purchases.
 */
public class MembershipService {

    private final MembershipDAO membershipDAO;
    private final MembershipPurchaseDAO purchaseDAO;

    /**
     * Creates the service with the application's real DAOs.
     */
    public MembershipService() {
        this(
                new MembershipDAO(),
                new MembershipPurchaseDAO()
        );
    }

    /**
     * Constructor used for testing with supplied DAO implementations.
     *
     * @param membershipDAO membership data access object
     * @param purchaseDAO membership purchase data access object
     */
    MembershipService(
            MembershipDAO membershipDAO,
            MembershipPurchaseDAO purchaseDAO
    ) {
        this.membershipDAO = membershipDAO;
        this.purchaseDAO = purchaseDAO;
    }

    /**
     * Returns all available membership plans.
     *
     * @return list of membership plans
     */
    public List<Membership> getAvailableMemberships() {
        return membershipDAO.getAllMemberships();
    }

    /**
     * Finds a membership plan by ID.
     *
     * @param membershipId membership ID
     * @return matching membership, or null when not found
     */
    public Membership findMembershipById(int membershipId) {
        if (membershipId <= 0) {
            return null;
        }

        return membershipDAO.findById(membershipId);
    }

    /**
     * Purchases a membership for a user.
     *
     * The membership price is copied into the purchase so the purchase
     * retains the price that was actually paid.
     *
     * @param userId user making the purchase
     * @param membershipId membership being purchased
     * @return true when the purchase is successfully recorded
     */
    public boolean purchaseMembership(
            int userId,
            int membershipId
    ) {
        if (userId <= 0 || membershipId <= 0) {
            return false;
        }

        Membership membership =
                membershipDAO.findById(membershipId);

        if (membership == null
                || membership.getPrice() == null) {
            return false;
        }

        MembershipPurchase purchase =
                new MembershipPurchase(
                        0,
                        userId,
                        membershipId,
                        membership.getPrice(),
                        LocalDateTime.now().withNano(0)
                );

        return purchaseDAO.addMembershipPurchase(purchase);
    }

    /**
     * Returns all membership purchases belonging to a user.
     *
     * @param userId user ID
     * @return user's purchases
     */
    public List<MembershipPurchase> getUserPurchases(int userId) {
        if (userId <= 0) {
            return Collections.emptyList();
        }

        return purchaseDAO.getPurchasesByUserId(userId);
    }
}