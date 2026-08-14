package com.keyingym.service;

import com.keyingym.dao.MerchandiseDAO;
import com.keyingym.dao.MerchandisePurchaseDAO;
import com.keyingym.model.Merchandise;
import com.keyingym.model.MerchandisePurchase;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class MerchandiseServiceTest {

    @Test
    void shouldPurchaseMerchandiseAndReduceStock() {
        FakeMerchandiseDAO merchandiseDAO = new FakeMerchandiseDAO();
        FakeMerchandisePurchaseDAO purchaseDAO = new FakeMerchandisePurchaseDAO();

        Merchandise merchandise = new Merchandise(
                10,
                "Gym T-Shirt",
                "Clothing",
                new BigDecimal("20.00"),
                10
        );
        merchandiseDAO.merchandiseItems.add(merchandise);

        MerchandiseService service = new MerchandiseService(
                merchandiseDAO,
                purchaseDAO
        );

        assertTrue(service.purchaseMerchandise(25, 10, 2));
        assertNotNull(purchaseDAO.lastPurchase);
        assertEquals(25, purchaseDAO.lastPurchase.getUserId());
        assertEquals(10, purchaseDAO.lastPurchase.getMerchId());
        assertEquals(2, purchaseDAO.lastPurchase.getQuantity());
        assertEquals(
                new BigDecimal("20.00"),
                purchaseDAO.lastPurchase.getUnitPrice()
        );
        assertNotNull(purchaseDAO.lastPurchase.getPurchasedAt());
        assertEquals(8, merchandise.getCurrentStock());
    }

    @Test
    void shouldCalculateTotalInventoryValuation() {
        MerchandiseService service = new MerchandiseService(
                new FakeMerchandiseDAO(),
                new FakeMerchandisePurchaseDAO()
        );

        List<Merchandise> merchandise = List.of(
                new Merchandise(
                        1,
                        "Protein Bar",
                        "Food & Drink",
                        new BigDecimal("3.50"),
                        40
                ),
                new Merchandise(
                        2,
                        "Water Bottle",
                        "Workout Gear",
                        new BigDecimal("15.00"),
                        20
                )
        );

        assertEquals(
                new BigDecimal("440.00"),
                service.calculateInventoryValuation(merchandise)
        );
    }

    @Test
    void shouldReturnZeroInventoryValuationForEmptyInput() {
        MerchandiseService service = new MerchandiseService(
                new FakeMerchandiseDAO(),
                new FakeMerchandisePurchaseDAO()
        );

        assertEquals(
                BigDecimal.ZERO,
                service.calculateInventoryValuation(List.of())
        );
        assertEquals(
                BigDecimal.ZERO,
                service.calculateInventoryValuation(null)
        );
    }

    @Test
    void shouldRejectQuantityGreaterThanStock() {
        FakeMerchandiseDAO merchandiseDAO = new FakeMerchandiseDAO();
        FakeMerchandisePurchaseDAO purchaseDAO = new FakeMerchandisePurchaseDAO();

        merchandiseDAO.merchandiseItems.add(
                new Merchandise(
                        10,
                        "Gym T-Shirt",
                        "Clothing",
                        new BigDecimal("20.00"),
                        3
                )
        );

        MerchandiseService service = new MerchandiseService(
                merchandiseDAO,
                purchaseDAO
        );

        assertFalse(service.purchaseMerchandise(25, 10, 4));
        assertEquals(0, purchaseDAO.addCallCount);
        assertEquals(3, merchandiseDAO.merchandiseItems.get(0).getCurrentStock());
    }

    @Test
    void shouldRejectInvalidPurchaseData() {
        FakeMerchandiseDAO merchandiseDAO = new FakeMerchandiseDAO();
        FakeMerchandisePurchaseDAO purchaseDAO = new FakeMerchandisePurchaseDAO();
        MerchandiseService service = new MerchandiseService(merchandiseDAO, purchaseDAO);

        assertFalse(service.purchaseMerchandise(0, 10, 1));
        assertFalse(service.purchaseMerchandise(25, 0, 1));
        assertFalse(service.purchaseMerchandise(25, 10, 0));
        assertFalse(service.purchaseMerchandise(25, 10, -1));
        assertEquals(0, purchaseDAO.addCallCount);
    }

    @Test
    void shouldReturnAvailableMerchandise() {
        FakeMerchandiseDAO merchandiseDAO = new FakeMerchandiseDAO();
        FakeMerchandisePurchaseDAO purchaseDAO = new FakeMerchandisePurchaseDAO();

        merchandiseDAO.merchandiseItems.add(
                new Merchandise(1, "Gym T-Shirt", "Clothing", new BigDecimal("20.00"), 10)
        );
        merchandiseDAO.merchandiseItems.add(
                new Merchandise(2, "Water Bottle", "Accessories", new BigDecimal("10.00"), 15)
        );

        MerchandiseService service = new MerchandiseService(merchandiseDAO, purchaseDAO);
        List<Merchandise> merchandise = service.getAvailableMerchandise();

        assertEquals(2, merchandise.size());
        assertEquals("Gym T-Shirt", merchandise.get(0).getProductName());
        assertEquals("Water Bottle", merchandise.get(1).getProductName());
    }

    @Test
    void shouldReturnUserPurchases() {
        FakeMerchandiseDAO merchandiseDAO = new FakeMerchandiseDAO();
        FakeMerchandisePurchaseDAO purchaseDAO = new FakeMerchandisePurchaseDAO();

        purchaseDAO.purchases.add(
                new MerchandisePurchase(
                        1,
                        25,
                        10,
                        2,
                        new BigDecimal("20.00"),
                        java.time.LocalDateTime.now()
                )
        );

        MerchandiseService service = new MerchandiseService(merchandiseDAO, purchaseDAO);
        List<MerchandisePurchase> purchases = service.getUserPurchases(25);

        assertEquals(1, purchases.size());
        assertEquals(25, purchases.get(0).getUserId());
        assertEquals(10, purchases.get(0).getMerchId());
        assertEquals(2, purchases.get(0).getQuantity());
    }

    @Test
    void shouldRejectInvalidUserIdWhenGettingPurchases() {
        FakeMerchandiseDAO merchandiseDAO = new FakeMerchandiseDAO();
        FakeMerchandisePurchaseDAO purchaseDAO = new FakeMerchandisePurchaseDAO();
        MerchandiseService service = new MerchandiseService(merchandiseDAO, purchaseDAO);

        List<MerchandisePurchase> purchases = service.getUserPurchases(0);

        assertTrue(purchases.isEmpty());
        assertEquals(0, purchaseDAO.getPurchasesCallCount);
    }

    private static class FakeMerchandiseDAO extends MerchandiseDAO {
        private final List<Merchandise> merchandiseItems = new ArrayList<>();

        @Override
        public Merchandise findById(int merchId) {
            return merchandiseItems.stream()
                    .filter(item -> item.getMerchId() == merchId)
                    .findFirst()
                    .orElse(null);
        }

        @Override
        public List<Merchandise> getAllMerchandise() {
            return new ArrayList<>(merchandiseItems);
        }

        @Override
        public boolean updateMerchandise(Merchandise merchandise) {
            Merchandise existing = findById(merchandise.getMerchId());

            if (existing == null) {
                return false;
            }

            existing.setProductName(merchandise.getProductName());
            existing.setType(merchandise.getType());
            existing.setPrice(merchandise.getPrice());
            existing.setCurrentStock(merchandise.getCurrentStock());
            return true;
        }
    }

    private static class FakeMerchandisePurchaseDAO extends MerchandisePurchaseDAO {
        private MerchandisePurchase lastPurchase;
        private final List<MerchandisePurchase> purchases = new ArrayList<>();
        private int addCallCount;
        private int getPurchasesCallCount;

        @Override
        public boolean addMerchandisePurchase(MerchandisePurchase purchase) {
            addCallCount++;
            lastPurchase = purchase;
            purchases.add(purchase);
            return true;
        }

        @Override
        public List<MerchandisePurchase> getPurchasesByUserId(int userId) {
            getPurchasesCallCount++;
            return purchases.stream()
                    .filter(item -> item.getUserId() == userId)
                    .toList();
        }
    }
}
