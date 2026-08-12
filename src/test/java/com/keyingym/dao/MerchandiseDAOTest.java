package com.keyingym.dao;

import com.keyingym.model.Merchandise;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class MerchandiseDAOTest {

    @Test
    void shouldPerformMerchandiseCrudOperations() {
        MerchandiseDAO merchandiseDAO = new MerchandiseDAO();
        String productName = "Test Product " +
                UUID.randomUUID().toString().substring(0, 8);
        int merchId = 0;

        try {
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

            assertEquals(new BigDecimal("20.00"),
                    createdMerchandise.getPrice());
            assertEquals(10, createdMerchandise.getCurrentStock());

            assertNotNull(merchandiseDAO.findById(merchId));

            createdMerchandise.setType("Updated Type");
            createdMerchandise.setPrice(new BigDecimal("30.00"));
            createdMerchandise.setCurrentStock(15);

            assertTrue(merchandiseDAO.updateMerchandise(createdMerchandise));

            Merchandise updatedMerchandise =
                    merchandiseDAO.findById(merchId);

            assertNotNull(updatedMerchandise);
            assertEquals("Updated Type", updatedMerchandise.getType());
            assertEquals(new BigDecimal("30.00"),
                    updatedMerchandise.getPrice());
            assertEquals(15, updatedMerchandise.getCurrentStock());

            assertTrue(merchandiseDAO.deleteMerchandise(merchId));
            assertNull(merchandiseDAO.findById(merchId));

        } finally {
            if (merchId != 0) {
                merchandiseDAO.deleteMerchandise(merchId);
            } else {
                merchandiseDAO.getAllMerchandise()
                        .stream()
                        .filter(item ->
                                productName.equals(item.getProductName()))
                        .findFirst()
                        .ifPresent(item ->
                                merchandiseDAO.deleteMerchandise(
                                        item.getMerchId()
                                ));
            }
        }
    }
}