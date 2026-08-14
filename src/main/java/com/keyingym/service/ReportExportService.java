package com.keyingym.service;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import com.keyingym.config.AppLogger;
import com.keyingym.dao.MembershipPurchaseDAO;
import com.keyingym.dao.MerchandiseDAO;
import com.keyingym.dao.MerchandisePurchaseDAO;
import com.keyingym.model.MembershipPurchase;
import com.keyingym.model.Merchandise;
import com.keyingym.model.MerchandisePurchase;

/**
 * Provides report generation and export operations for the gym system.
 */
public class ReportExportService {

    private static final String REPORT_DIRECTORY = "reports";

    private final MembershipPurchaseDAO membershipPurchaseDAO;
    private final MerchandiseDAO merchandiseDAO;
    private final MerchandisePurchaseDAO merchandisePurchaseDAO;

    public ReportExportService() {
        this(
                new MembershipPurchaseDAO(),
                new MerchandiseDAO(),
                new MerchandisePurchaseDAO()
        );
    }

    ReportExportService(
            MembershipPurchaseDAO membershipPurchaseDAO,
            MerchandiseDAO merchandiseDAO,
            MerchandisePurchaseDAO merchandisePurchaseDAO
    ) {
        this.membershipPurchaseDAO = membershipPurchaseDAO;
        this.merchandiseDAO = merchandiseDAO;
        this.merchandisePurchaseDAO = merchandisePurchaseDAO;
    }

    /**
     * Exports the membership revenue report for the current calendar year.
     */
    public Path exportMembershipRevenueReport() throws IOException {
        List<MembershipPurchase> purchases =
                membershipPurchaseDAO.getCurrentYearMembershipPurchases();

        Path reportPath = createReportPath("membership-revenue-report.txt");

        BigDecimal totalRevenue = BigDecimal.ZERO;
        StringBuilder report = new StringBuilder();

        report.append("GYM MANAGEMENT SYSTEM")
                .append(System.lineSeparator());
        report.append("CURRENT YEAR MEMBERSHIP REVENUE REPORT")
                .append(System.lineSeparator());
        report.append("========================================")
                .append(System.lineSeparator());

        report.append("Purchase ID | User ID | Membership ID | Price | Purchased At")
                .append(System.lineSeparator());
        report.append("------------|---------|---------------|-------|-------------")
                .append(System.lineSeparator());

        for (MembershipPurchase purchase : purchases) {
            BigDecimal price = purchase.getPrice();

            if (price == null) {
                price = BigDecimal.ZERO;
            }

            totalRevenue = totalRevenue.add(price);

            report.append(purchase.getPurchaseId())
                    .append(" | ")
                    .append(purchase.getUserId())
                    .append(" | ")
                    .append(purchase.getMembershipId())
                    .append(" | ")
                    .append(price)
                    .append(" | ")
                    .append(purchase.getPurchasedAt())
                    .append(System.lineSeparator());
        }

        report.append(System.lineSeparator());
        report.append("Current Year Membership Revenue: ")
                .append(totalRevenue)
                .append(System.lineSeparator());

        writeReport(reportPath, report.toString());

        AppLogger.info(
                "Current-year membership revenue report exported: "
                        + reportPath
        );

        return reportPath;
    }

    /**
     * Exports the current merchandise inventory, including item and total valuation.
     */
    public Path exportMerchandiseInventoryReport() throws IOException {
        List<Merchandise> merchandise =
                merchandiseDAO.getAllMerchandise();

        Path reportPath = createReportPath("merchandise-inventory-report.txt");

        BigDecimal totalInventoryValue = BigDecimal.ZERO;
        StringBuilder report = new StringBuilder();

        report.append("GYM MANAGEMENT SYSTEM")
                .append(System.lineSeparator());
        report.append("MERCHANDISE INVENTORY REPORT")
                .append(System.lineSeparator());
        report.append("========================================")
                .append(System.lineSeparator());

        report.append(
                "Merch ID | Product Name | Type | Price | Current Stock | Inventory Value"
        ).append(System.lineSeparator());

        report.append(
                "---------|--------------|------|-------|---------------|-----------------"
        ).append(System.lineSeparator());

        for (Merchandise item : merchandise) {
            BigDecimal inventoryValue = item.getInventoryValue();
            totalInventoryValue = totalInventoryValue.add(inventoryValue);

            report.append(item.getMerchId())
                    .append(" | ")
                    .append(item.getProductName())
                    .append(" | ")
                    .append(item.getType())
                    .append(" | ")
                    .append(item.getPrice())
                    .append(" | ")
                    .append(item.getCurrentStock())
                    .append(" | ")
                    .append(inventoryValue)
                    .append(System.lineSeparator());
        }

        report.append(System.lineSeparator());
        report.append("Total Products: ")
                .append(merchandise.size())
                .append(System.lineSeparator());
        report.append("Total Inventory Valuation: ")
                .append(totalInventoryValue)
                .append(System.lineSeparator());

        writeReport(reportPath, report.toString());

        AppLogger.info(
                "Merchandise inventory report exported: "
                        + reportPath
        );

        return reportPath;
    }

    public Path exportMerchandiseSalesReport() throws IOException {
        List<MerchandisePurchase> purchases =
                merchandisePurchaseDAO.getAllMerchandisePurchases();

        Path reportPath = createReportPath("merchandise-sales-report.txt");

        BigDecimal totalRevenue = BigDecimal.ZERO;
        StringBuilder report = new StringBuilder();

        report.append("GYM MANAGEMENT SYSTEM")
                .append(System.lineSeparator());
        report.append("MERCHANDISE SALES REPORT")
                .append(System.lineSeparator());
        report.append("========================================")
                .append(System.lineSeparator());

        report.append(
                "Purchase ID | User ID | Merch ID | Quantity | Unit Price | Total | Purchased At"
        ).append(System.lineSeparator());

        report.append(
                "------------|---------|----------|----------|------------|-------|-------------"
        ).append(System.lineSeparator());

        for (MerchandisePurchase purchase : purchases) {
            BigDecimal unitPrice = purchase.getUnitPrice();

            if (unitPrice == null) {
                unitPrice = BigDecimal.ZERO;
            }

            BigDecimal itemTotal = unitPrice.multiply(
                    BigDecimal.valueOf(purchase.getQuantity())
            );

            totalRevenue = totalRevenue.add(itemTotal);

            report.append(purchase.getPurchaseId())
                    .append(" | ")
                    .append(purchase.getUserId())
                    .append(" | ")
                    .append(purchase.getMerchId())
                    .append(" | ")
                    .append(purchase.getQuantity())
                    .append(" | ")
                    .append(unitPrice)
                    .append(" | ")
                    .append(itemTotal)
                    .append(" | ")
                    .append(purchase.getPurchasedAt())
                    .append(System.lineSeparator());
        }

        report.append(System.lineSeparator());
        report.append("Total Merchandise Revenue: ")
                .append(totalRevenue)
                .append(System.lineSeparator());

        writeReport(reportPath, report.toString());

        AppLogger.info(
                "Merchandise sales report exported: "
                        + reportPath
        );

        return reportPath;
    }

    private Path createReportPath(String fileName) throws IOException {
        Path directory = Paths.get(REPORT_DIRECTORY);
        Files.createDirectories(directory);
        return directory.resolve(fileName);
    }

    private void writeReport(Path path, String content)
            throws IOException {
        Files.writeString(path, content);
    }
}
