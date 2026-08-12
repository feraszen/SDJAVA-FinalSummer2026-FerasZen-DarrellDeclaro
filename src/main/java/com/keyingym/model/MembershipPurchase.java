package com.keyingym.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Represents a completed membership purchase made by a gym user.
 *
 * The purchase stores the membership selected, the user who purchased it,
 * the price paid at the time of purchase, and the purchase timestamp.
 */
public class MembershipPurchase {

    private int purchaseId;
    private int userId;
    private int membershipId;
    private BigDecimal price;
    private LocalDateTime purchasedAt;

    public MembershipPurchase() {
    }

    public MembershipPurchase(
            int purchaseId,
            int userId,
            int membershipId,
            BigDecimal price,
            LocalDateTime purchasedAt
    ) {
        this.purchaseId = purchaseId;
        this.userId = userId;
        this.membershipId = membershipId;
        this.price = price;
        this.purchasedAt = purchasedAt;
    }

    public int getPurchaseId() {
        return purchaseId;
    }

    public void setPurchaseId(int purchaseId) {
        this.purchaseId = purchaseId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getMembershipId() {
        return membershipId;
    }

    public void setMembershipId(int membershipId) {
        this.membershipId = membershipId;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public LocalDateTime getPurchasedAt() {
        return purchasedAt;
    }

    public void setPurchasedAt(LocalDateTime purchasedAt) {
        this.purchasedAt = purchasedAt;
    }
}