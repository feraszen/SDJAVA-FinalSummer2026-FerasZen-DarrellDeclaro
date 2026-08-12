package com.keyingym.model;

import java.math.BigDecimal;

/**
 * Represents a membership plan available in the gym system.
 *
 * A Membership defines the type and price of a plan.
 * Actual purchases are represented separately by MembershipPurchase.
 */
public class Membership {

    private int membershipId;
    private String membershipType;
    private BigDecimal price;

    public Membership() {
    }

    public Membership(
            int membershipId,
            String membershipType,
            BigDecimal price
    ) {
        this.membershipId = membershipId;
        this.membershipType = membershipType;
        this.price = price;
    }

    public int getMembershipId() {
        return membershipId;
    }

    public void setMembershipId(int membershipId) {
        this.membershipId = membershipId;
    }

    public String getMembershipType() {
        return membershipType;
    }

    public void setMembershipType(String membershipType) {
        this.membershipType = membershipType;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }
}