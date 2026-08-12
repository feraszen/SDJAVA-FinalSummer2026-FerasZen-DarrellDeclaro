package com.keyingym.service;

import com.keyingym.dao.MerchandiseDAO;
import com.keyingym.dao.MerchandisePurchaseDAO;
import com.keyingym.model.Merchandise;
import com.keyingym.model.MerchandisePurchase;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

/**
 * Provides business operations for gym merchandise and purchases.
 */
public class MerchandiseService {

    private final MerchandiseDAO merchandiseDAO;
    private final MerchandisePurchaseDAO purchaseDAO;

    /**
     * Creates the service with the application's real DAOs.
     */
    public MerchandiseService() {
        this(
                new MerchandiseDAO(),
                new MerchandisePurchaseDAO()
        );
    }

    /**
     * Constructor used for testing with supplied DAO implementations.
     *
     * @param merchandiseDAO merchandise data access object
     * @param purchaseDAO merchandise purchase data access object
     */
    MerchandiseService(
            MerchandiseDAO merchandiseDAO,
            MerchandisePurchaseDAO purchaseDAO
    ) {
        this.merchandiseDAO = merchandiseDAO;
        this.purchaseDAO = purchaseDAO;
    }

    /**
     * Returns all merchandise items.
     *
     * @return list of merchandise
     */
    public List<Merchandise> getAvailableMerchandise() {
        return merchandiseDAO.getAllMerchandise();
    }

    /**
     * Finds merchandise by ID.
     *
     * @param merchId merchandise ID
     * @return matching merchandise, or null when not found
     */
    public Merchandise findMerchandiseById(int merchId) {
        if (merchId <= 0) {
            return null;
        }

        return merchandiseDAO.findById(merchId);
    }

    /**
     * Purchases merchandise for a user.
     *
     * The current merchandise price is copied into the purchase.
     * The inventory is reduced by the purchased quantity.
     *
     * @param userId user making the purchase
     * @param merchId merchandise being purchased
     * @param quantity quantity to purchase
     * @return true when the purchase is successfully recorded
     */
    public boolean purchaseMerchandise(
            int userId,
            int merchId,
            int quantity
    ) {
        if (userId <= 0
                || merchId <= 0
                || quantity <= 0) {
            return false;
        }

        Merchandise merchandise =
                merchandiseDAO.findById(merchId);

        if (merchandise == null
                || merchandise.getPrice() == null) {
            return false;
        }

        if (quantity > merchandise.getCurrentStock()) {
            return false;
        }

        MerchandisePurchase purchase =
                new MerchandisePurchase(
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

    /**
     * Returns all merchandise purchases belonging to a user.
     *
     * @param userId user ID
     * @return user's merchandise purchases
     */
    public List<MerchandisePurchase> getUserPurchases(int userId) {
        if (userId <= 0) {
            return Collections.emptyList();
        }

        return purchaseDAO.getPurchasesByUserId(userId);
    }
}