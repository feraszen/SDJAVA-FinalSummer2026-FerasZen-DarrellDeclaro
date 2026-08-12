package com.keyingym.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Represents a completed merchandise purchase made by a gym user.
 *
 * The purchase records the user, product, quantity, unit price at the time
 * of purchase, and the timestamp so inventory and revenue can be tracked.
 */
public class MerchandisePurchase {

    private int purchaseId;
    private int userId;
    private int merchId;
    private int quantity;
    private BigDecimal unitPrice;
    private LocalDateTime purchasedAt;

    public MerchandisePurchase() {
    }

    public MerchandisePurchase(
            int purchaseId,
            int userId,
            int merchId,
            int quantity,
            BigDecimal unitPrice,
            LocalDateTime purchasedAt
    ) {
        this.purchaseId = purchaseId;
        this.userId = userId;
        this.merchId = merchId;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
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

    public int getMerchId() {
        return merchId;
    }

    public void setMerchId(int merchId) {
        this.merchId = merchId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
    }

    public LocalDateTime getPurchasedAt() {
        return purchasedAt;
    }

    public void setPurchasedAt(LocalDateTime purchasedAt) {
        this.purchasedAt = purchasedAt;
    }
}