package com.keyingym.model;

import java.math.BigDecimal;

/**
 * Represents a merchandise item available in the gym.
 *
 * The model stores product information and current inventory.
 * Inventory operations and purchase processing are handled by other layers.
 */
public class Merchandise {

    private int merchId;
    private String productName;
    private String type;
    private BigDecimal price;
    private int currentStock;

    public Merchandise() {
    }

    public Merchandise(
            int merchId,
            String productName,
            String type,
            BigDecimal price,
            int currentStock
    ) {
        this.merchId = merchId;
        this.productName = productName;
        this.type = type;
        this.price = price;
        this.currentStock = currentStock;
    }

    public int getMerchId() {
        return merchId;
    }

    public void setMerchId(int merchId) {
        this.merchId = merchId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public int getCurrentStock() {
        return currentStock;
    }

    public void setCurrentStock(int currentStock) {
        this.currentStock = currentStock;
    }

    /**
     * Calculates the current inventory value for this item.
     *
     * @return price multiplied by current stock, or zero when price is null
     */
    public BigDecimal getInventoryValue() {
        if (price == null || currentStock <= 0) {
            return BigDecimal.ZERO;
        }

        return price.multiply(BigDecimal.valueOf(currentStock));
    }
}