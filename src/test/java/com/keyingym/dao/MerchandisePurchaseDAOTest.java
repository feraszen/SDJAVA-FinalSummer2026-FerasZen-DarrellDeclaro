package com.keyingym.dao;

import com.keyingym.model.Merchandise;
import com.keyingym.model.MerchandisePurchase;
import com.keyingym.model.User;
import com.keyingym.model.UserRole;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class MerchandisePurchaseDAOTest {

    @Test
    void shouldCreateAndReadMerchandisePurchase() {
        UserDAO userDAO = new UserDAO();
        MerchandiseDAO merchandiseDAO = new MerchandiseDAO();
        MerchandisePurchaseDAO purchaseDAO =
                new MerchandisePurchaseDAO();

        String uniqueId = UUID.randomUUID().toString().substring(0, 8);
        String username = "merch_user_" + uniqueId;
        String productName = "Purchase Product " + uniqueId;

        int userId = 0;
        int merchId = 0;
        int purchaseId = 0;

        try {
            User user = new User(
                    0,
                    username,
                    "test-password-hash",
                    username + "@example.com",
                    "709-555-0100",
                    "Test Address",
                    UserRole.MEMBER
            );
            assertTrue(userDAO.addUser(user));

            User createdUser = userDAO.findByUsername(username);
            assertNotNull(createdUser);
            userId = createdUser.getUserId();

            Merchandise merchandise = new Merchandise(
                    0,
                    productName,
                    "Test Type",
                    new BigDecimal("20.00"),
                    10
            );
            assertTrue(merchandiseDAO.addMerchandise(merchandise));

            Merchandise createdMerchandise = merchandiseDAO
                    .getAllMerchandise()
                    .stream()
                    .filter(item ->
                            productName.equals(item.getProductName()))
                    .findFirst()
                    .orElse(null);

            assertNotNull(createdMerchandise);
            merchId = createdMerchandise.getMerchId();
            final int createdMerchId = merchId;

            LocalDateTime purchasedAt =
                    LocalDateTime.now().withNano(0);

            MerchandisePurchase purchase = new MerchandisePurchase(
                    0,
                    userId,
                    merchId,
                    2,
                    new BigDecimal("20.00"),
                    purchasedAt
            );
            assertTrue(purchaseDAO.addMerchandisePurchase(purchase));

            MerchandisePurchase createdPurchase = purchaseDAO
                    .getPurchasesByUserId(userId)
                    .stream()
                    .filter(item ->
                            item.getMerchId() == createdMerchId)
                    .findFirst()
                    .orElse(null);

            assertNotNull(createdPurchase);
            purchaseId = createdPurchase.getPurchaseId();

            assertEquals(userId, createdPurchase.getUserId());
            assertEquals(merchId, createdPurchase.getMerchId());
            assertEquals(2, createdPurchase.getQuantity());
            assertEquals(
                    new BigDecimal("20.00"),
                    createdPurchase.getUnitPrice()
            );
            assertEquals(purchasedAt, createdPurchase.getPurchasedAt());

            assertNotNull(purchaseDAO.findById(purchaseId));
            assertTrue(purchaseDAO.deleteMerchandisePurchase(purchaseId));
            assertNull(purchaseDAO.findById(purchaseId));

        } finally {
            if (purchaseId != 0) {
                purchaseDAO.deleteMerchandisePurchase(purchaseId);
            }

            if (merchId != 0) {
                merchandiseDAO.deleteMerchandise(merchId);
            }

            if (userId != 0) {
                userDAO.deleteUser(userId);
            }
        }
    }
}