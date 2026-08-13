package com.keyingym.service;

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
        List<Merchandise> merchandise =
                merchandiseDAO.getAllMerchandise();

        if (merchandise == null) {
            return Collections.emptyList();
        }

        return merchandise;
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
     * Adds a merchandise item.
     *
     * @param merchandise merchandise item
     * @return true when successfully added
     */
    public boolean addMerchandise(Merchandise merchandise) {
        if (!isValidMerchandise(merchandise)) {
            return false;
        }

        return merchandiseDAO.addMerchandise(merchandise);
    }

    /**
     * Updates an existing merchandise item.
     *
     * @param merchandise merchandise item
     * @return true when successfully updated
     */
    public boolean updateMerchandise(Merchandise merchandise) {
        if (!isValidMerchandise(merchandise)
                || merchandise.getMerchId() <= 0) {
            return false;
        }

        return merchandiseDAO.updateMerchandise(merchandise);
    }

    /**
     * Deletes merchandise by ID.
     *
     * @param merchId merchandise ID
     * @return true when successfully deleted
     */
    public boolean deleteMerchandise(int merchId) {
        if (merchId <= 0) {
            return false;
        }

        return merchandiseDAO.deleteMerchandise(merchId);
    }

    /**
     * Purchases merchandise for a user.
     *
     * @param userId user making the purchase
     * @param merchId merchandise being purchased
     * @param quantity quantity to purchase
     * @return true when successfully purchased
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

        List<MerchandisePurchase> purchases =
                purchaseDAO.getPurchasesByUserId(userId);

        if (purchases == null) {
            return Collections.emptyList();
        }

        return purchases;
    }

    /**
     * Validates merchandise information.
     */
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

    /**
     * Checks whether a string is null or blank.
     */
    private boolean isBlank(String value) {
        return value == null || value.isBlank();
    }
}
