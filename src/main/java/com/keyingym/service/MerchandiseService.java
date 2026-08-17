package com.keyingym.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import com.keyingym.dao.MerchandiseDAO;
import com.keyingym.dao.MerchandisePurchaseDAO;
import com.keyingym.model.Merchandise;
import com.keyingym.model.MerchandisePurchase;

/**
 * Provides business operations for gym merchandise and purchases.
 */
public class MerchandiseService {

    private final MerchandiseDAO merchandiseDAO;
    private final MerchandisePurchaseDAO purchaseDAO;

    public MerchandiseService() {
        this(new MerchandiseDAO(), new MerchandisePurchaseDAO());
    }

    MerchandiseService(
            MerchandiseDAO merchandiseDAO,
            MerchandisePurchaseDAO purchaseDAO
    ) {
        this.merchandiseDAO = merchandiseDAO;
        this.purchaseDAO = purchaseDAO;
    }

    public List<Merchandise> getAvailableMerchandise() {
        List<Merchandise> merchandise = merchandiseDAO.getAllMerchandise();

        if (merchandise == null) {
            return Collections.emptyList();
        }

        return merchandise;
    }

    /**
     * Calculates the total current inventory valuation as price multiplied by stock.
     */
    public BigDecimal calculateInventoryValuation(List<Merchandise> merchandise) {
        if (merchandise == null || merchandise.isEmpty()) {
            return BigDecimal.ZERO;
        }

        BigDecimal totalValuation = BigDecimal.ZERO;

        for (Merchandise item : merchandise) {
            if (item == null) {
                continue;
            }

            BigDecimal price = item.getPrice() == null
                    ? BigDecimal.ZERO
                    : item.getPrice();

            totalValuation = totalValuation.add(
                    price.multiply(BigDecimal.valueOf(item.getCurrentStock()))
            );
        }

        return totalValuation;
    }

    public Merchandise findMerchandiseById(int merchId) {
        if (merchId <= 0) {
            return null;
        }

        return merchandiseDAO.findById(merchId);
    }

    public boolean addMerchandise(Merchandise merchandise) {
        if (!isValidMerchandise(merchandise)) {
            return false;
        }

        return merchandiseDAO.addMerchandise(merchandise);
    }

    public boolean updateMerchandise(Merchandise merchandise) {
        if (!isValidMerchandise(merchandise)
                || merchandise.getMerchId() <= 0) {
            return false;
        }

        return merchandiseDAO.updateMerchandise(merchandise);
    }

    public boolean deleteMerchandise(int merchId) {
        if (merchId <= 0) {
            return false;
        }

        return merchandiseDAO.deleteMerchandise(merchId);
    }

    public boolean purchaseMerchandise(
            int userId,
            int merchId,
            int quantity
    ) {
        if (userId <= 0 || merchId <= 0 || quantity <= 0) {
            return false;
        }

        Merchandise merchandise = merchandiseDAO.findById(merchId);

        if (merchandise == null || merchandise.getPrice() == null) {
            return false;
        }

        if (quantity > merchandise.getCurrentStock()) {
            return false;
        }

        MerchandisePurchase purchase = new MerchandisePurchase(
                0,
                userId,
                merchId,
                quantity,
                merchandise.getPrice(),
                LocalDateTime.now().withNano(0)
        );

        if (!purchaseDAO.addMerchandisePurchase(purchase)) {
            return false;
        }

        merchandise.setCurrentStock(
                merchandise.getCurrentStock() - quantity
        );

        return merchandiseDAO.updateMerchandise(merchandise);
    }

    public List<MerchandisePurchase> getUserPurchases(int userId) {
        if (userId <= 0) {
            return Collections.emptyList();
        }

        List<MerchandisePurchase> purchases =
                purchaseDAO.getPurchasesByUserId(userId);

        if (purchases == null) {
            return Collections.emptyList();
        }

        return purchases;
    }

    private boolean isValidMerchandise(Merchandise merchandise) {
        if (merchandise == null) {
            return false;
        }

        if (isBlank(merchandise.getProductName())) {
            return false;
        }

        if (isBlank(merchandise.getType())) {
            return false;
        }

        if (merchandise.getPrice() == null
                || merchandise.getPrice().signum() < 0) {
            return false;
        }

        return merchandise.getCurrentStock() >= 0;
    }

    private boolean isBlank(String value) {
        return value == null || value.isBlank();
    }
}
