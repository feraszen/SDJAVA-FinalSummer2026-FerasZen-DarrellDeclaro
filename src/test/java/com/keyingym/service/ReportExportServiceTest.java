package com.keyingym.service;

import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.Collections;

import org.junit.jupiter.api.AfterEach;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

import com.keyingym.dao.MembershipPurchaseDAO;
import com.keyingym.dao.MerchandiseDAO;
import com.keyingym.dao.MerchandisePurchaseDAO;
import com.keyingym.model.MembershipPurchase;
import com.keyingym.model.Merchandise;
import com.keyingym.model.MerchandisePurchase;

class ReportExportServiceTest {

    private final Path reportsDirectory =
            Path.of("reports");

    @AfterEach
    void cleanUpReports() throws Exception {
        if (Files.exists(reportsDirectory)) {
            try (var files = Files.list(reportsDirectory)) {
                files.forEach(path -> {
                    try {
                        Files.deleteIfExists(path);
                    } catch (Exception ignored) {
                        // Ignore cleanup failures after the test.
                    }
                });
            }

            Files.deleteIfExists(reportsDirectory);
        }
    }

    @Test
    void shouldExportMembershipRevenueReport() throws Exception {
        MembershipPurchaseDAO membershipPurchaseDAO =
                new MembershipPurchaseDAO() {
                    @Override
                    public java.util.List<MembershipPurchase>
                    getAllMembershipPurchases() {

                        MembershipPurchase purchase =
                                new MembershipPurchase(
                                        1,
                                        10,
                                        2,
                                        new BigDecimal("49.99"),
                                        LocalDateTime.of(
                                                2026,
                                                8,
                                                12,
                                                10,
                                                30
                                        )
                                );

                        return Collections.singletonList(purchase);
                    }
                };

        MerchandiseDAO merchandiseDAO =
                new MerchandiseDAO();

        MerchandisePurchaseDAO merchandisePurchaseDAO =
                new MerchandisePurchaseDAO();

        ReportExportService service =
                new ReportExportService(
                        membershipPurchaseDAO,
                        merchandiseDAO,
                        merchandisePurchaseDAO
                );

        Path report =
                service.exportMembershipRevenueReport();

        assertNotNull(report);
        assertTrue(Files.exists(report));

        String content = Files.readString(report);

        assertTrue(
                content.contains("GYM MANAGEMENT SYSTEM")
        );

        assertTrue(
                content.contains("MEMBERSHIP REVENUE REPORT")
        );

        assertTrue(
                content.contains("Purchase ID | User ID | Membership ID")
        );

        assertTrue(
                content.contains("1 | 10 | 2 | 49.99")
        );

        assertTrue(
                content.contains("Total Membership Revenue: 49.99")
        );
    }

    @Test
    void shouldExportMembershipRevenueReportWithNullPrice() throws Exception {
        MembershipPurchaseDAO membershipPurchaseDAO =
                new MembershipPurchaseDAO() {
                    @Override
                    public java.util.List<MembershipPurchase>
                    getAllMembershipPurchases() {

                        MembershipPurchase purchase =
                                new MembershipPurchase(
                                        2,
                                        20,
                                        3,
                                        null,
                                        LocalDateTime.of(
                                                2026,
                                                8,
                                                12,
                                                11,
                                                0
                                        )
                                );

                        return Collections.singletonList(purchase);
                    }
                };

        ReportExportService service =
                new ReportExportService(
                        membershipPurchaseDAO,
                        new MerchandiseDAO(),
                        new MerchandisePurchaseDAO()
                );

        Path report =
                service.exportMembershipRevenueReport();

        String content = Files.readString(report);

        assertTrue(
                content.contains("2 | 20 | 3 | 0")
        );

        assertTrue(
                content.contains("Total Membership Revenue: 0")
        );
    }

    @Test
    void shouldExportMerchandiseInventoryReport() throws Exception {
        MerchandiseDAO merchandiseDAO =
                new MerchandiseDAO() {
                    @Override
                    public java.util.List<Merchandise>
                    getAllMerchandise() {

                        Merchandise item =
                                new Merchandise(
                                        1,
                                        "Gym T-Shirt",
                                        "Clothing",
                                        new BigDecimal("25.00"),
                                        15
                                );

                        return Collections.singletonList(item);
                    }
                };

        ReportExportService service =
                new ReportExportService(
                        new MembershipPurchaseDAO(),
                        merchandiseDAO,
                        new MerchandisePurchaseDAO()
                );

        Path report =
                service.exportMerchandiseInventoryReport();

        assertNotNull(report);
        assertTrue(Files.exists(report));

        String content = Files.readString(report);

        assertTrue(
                content.contains("GYM MANAGEMENT SYSTEM")
        );

        assertTrue(
                content.contains("MERCHANDISE INVENTORY REPORT")
        );

        assertTrue(
                content.contains("Merch ID | Product Name | Type")
        );

        assertTrue(
                content.contains(
                        "1 | Gym T-Shirt | Clothing | 25.00 | 15"
                )
        );

        assertTrue(
                content.contains("Total Products: 1")
        );
    }

    @Test
    void shouldExportMerchandiseInventoryReportWhenEmpty() throws Exception {
        MerchandiseDAO merchandiseDAO =
                new MerchandiseDAO() {
                    @Override
                    public java.util.List<Merchandise>
                    getAllMerchandise() {
                        return Collections.emptyList();
                    }
                };

        ReportExportService service =
                new ReportExportService(
                        new MembershipPurchaseDAO(),
                        merchandiseDAO,
                        new MerchandisePurchaseDAO()
                );

        Path report =
                service.exportMerchandiseInventoryReport();

        String content = Files.readString(report);

        assertTrue(
                content.contains("MERCHANDISE INVENTORY REPORT")
        );

        assertTrue(
                content.contains("Total Products: 0")
        );
    }

    @Test
    void shouldExportMerchandiseSalesReport() throws Exception {
        MerchandisePurchaseDAO merchandisePurchaseDAO =
                new MerchandisePurchaseDAO() {
                    @Override
                    public java.util.List<MerchandisePurchase>
                    getAllMerchandisePurchases() {

                        MerchandisePurchase purchase =
                                new MerchandisePurchase(
                                        5,
                                        10,
                                        3,
                                        4,
                                        new BigDecimal("12.50"),
                                        LocalDateTime.of(
                                                2026,
                                                8,
                                                12,
                                                12,
                                                0
                                        )
                                );

                        return Collections.singletonList(purchase);
                    }
                };

        ReportExportService service =
                new ReportExportService(
                        new MembershipPurchaseDAO(),
                        new MerchandiseDAO(),
                        merchandisePurchaseDAO
                );

        Path report =
                service.exportMerchandiseSalesReport();

        assertNotNull(report);
        assertTrue(Files.exists(report));

        String content = Files.readString(report);

        assertTrue(
                content.contains("GYM MANAGEMENT SYSTEM")
        );

        assertTrue(
                content.contains("MERCHANDISE SALES REPORT")
        );

        assertTrue(
                content.contains(
                        "Purchase ID | User ID | Merch ID"
                )
        );

        assertTrue(
                content.contains(
                        "5 | 10 | 3 | 4 | 12.50 | 50.00"
                )
        );

        assertTrue(
                content.contains(
                        "Total Merchandise Revenue: 50.00"
                )
        );
    }

    @Test
    void shouldExportMerchandiseSalesReportWithNullUnitPrice()
            throws Exception {

        MerchandisePurchaseDAO merchandisePurchaseDAO =
                new MerchandisePurchaseDAO() {
                    @Override
                    public java.util.List<MerchandisePurchase>
                    getAllMerchandisePurchases() {

                        MerchandisePurchase purchase =
                                new MerchandisePurchase(
                                        6,
                                        11,
                                        4,
                                        3,
                                        null,
                                        LocalDateTime.of(
                                                2026,
                                                8,
                                                12,
                                                13,
                                                0
                                        )
                                );

                        return Collections.singletonList(purchase);
                    }
                };

        ReportExportService service =
                new ReportExportService(
                        new MembershipPurchaseDAO(),
                        new MerchandiseDAO(),
                        merchandisePurchaseDAO
                );

        Path report =
                service.exportMerchandiseSalesReport();

        String content = Files.readString(report);

        assertTrue(
                content.contains(
                        "6 | 11 | 4 | 3 | 0 | 0"
                )
        );

        assertTrue(
                content.contains(
                        "Total Merchandise Revenue: 0"
                )
        );
    }

    @Test
    void shouldExportEmptyMerchandiseSalesReport()
            throws Exception {

        MerchandisePurchaseDAO merchandisePurchaseDAO =
                new MerchandisePurchaseDAO() {
                    @Override
                    public java.util.List<MerchandisePurchase>
                    getAllMerchandisePurchases() {
                        return Collections.emptyList();
                    }
                };

        ReportExportService service =
                new ReportExportService(
                        new MembershipPurchaseDAO(),
                        new MerchandiseDAO(),
                        merchandisePurchaseDAO
                );

        Path report =
                service.exportMerchandiseSalesReport();

        String content = Files.readString(report);

        assertTrue(
                content.contains("MERCHANDISE SALES REPORT")
        );

        assertTrue(
                content.contains(
                        "Total Merchandise Revenue: 0"
                )
        );
    }
}
