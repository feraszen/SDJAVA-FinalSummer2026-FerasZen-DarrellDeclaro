package com.keyingym.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.keyingym.config.AppLogger;
import com.keyingym.config.DatabaseConnection;
import com.keyingym.model.MerchandisePurchase;

/**
 * Data Access Object for merchandise purchase records.
 */
public class MerchandisePurchaseDAO {

    public boolean addMerchandisePurchase(MerchandisePurchase purchase) {
        String sql = """
                INSERT INTO merchandise_purchases
                    (user_id, merch_id, quantity, unit_price, purchased_at)
                VALUES (?, ?, ?, ?, ?)
                """;

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, purchase.getUserId());
            statement.setInt(2, purchase.getMerchId());
            statement.setInt(3, purchase.getQuantity());
            statement.setBigDecimal(4, purchase.getUnitPrice());
            statement.setObject(5, purchase.getPurchasedAt());

            return statement.executeUpdate() > 0;

        } catch (SQLException e) {
            AppLogger.error(
                    "Database transaction error in MerchandisePurchaseDAO.addMerchandisePurchase.",
                    e
            );
            return false;
        }
    }

    public MerchandisePurchase findById(int purchaseId) {
        String sql = """
                SELECT purchase_id, user_id, merch_id, quantity,
                       unit_price, purchased_at
                FROM merchandise_purchases
                WHERE purchase_id = ?
                """;

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, purchaseId);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return mapMerchandisePurchase(resultSet);
                }
            }

        } catch (SQLException e) {
            AppLogger.error(
                    "Database transaction error in MerchandisePurchaseDAO.findById.",
                    e
            );
        }

        return null;
    }

    public List<MerchandisePurchase> getAllMerchandisePurchases() {
        String sql = """
                SELECT purchase_id, user_id, merch_id, quantity,
                       unit_price, purchased_at
                FROM merchandise_purchases
                ORDER BY purchase_id ASC
                """;

        List<MerchandisePurchase> purchases = new ArrayList<>();

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                purchases.add(mapMerchandisePurchase(resultSet));
            }

        } catch (SQLException e) {
            AppLogger.error(
                    "Database transaction error in MerchandisePurchaseDAO.getAllMerchandisePurchases.",
                    e
            );
        }

        return purchases;
    }

    public List<MerchandisePurchase> getPurchasesByUserId(int userId) {
        String sql = """
                SELECT purchase_id, user_id, merch_id, quantity,
                       unit_price, purchased_at
                FROM merchandise_purchases
                WHERE user_id = ?
                ORDER BY purchased_at DESC
                """;

        List<MerchandisePurchase> purchases = new ArrayList<>();

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, userId);

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    purchases.add(mapMerchandisePurchase(resultSet));
                }
            }

        } catch (SQLException e) {
            AppLogger.error(
                    "Database transaction error in MerchandisePurchaseDAO.getPurchasesByUserId.",
                    e
            );
        }

        return purchases;
    }

    public boolean deleteMerchandisePurchase(int purchaseId) {
        String sql = """
                DELETE FROM merchandise_purchases
                WHERE purchase_id = ?
                """;

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, purchaseId);
            return statement.executeUpdate() > 0;

        } catch (SQLException e) {
            AppLogger.error(
                    "Database transaction error in MerchandisePurchaseDAO.deleteMerchandisePurchase.",
                    e
            );
            return false;
        }
    }

    private MerchandisePurchase mapMerchandisePurchase(
            ResultSet resultSet
    ) throws SQLException {

        MerchandisePurchase purchase = new MerchandisePurchase();

        purchase.setPurchaseId(resultSet.getInt("purchase_id"));
        purchase.setUserId(resultSet.getInt("user_id"));
        purchase.setMerchId(resultSet.getInt("merch_id"));
        purchase.setQuantity(resultSet.getInt("quantity"));
        purchase.setUnitPrice(resultSet.getBigDecimal("unit_price"));
        purchase.setPurchasedAt(
                resultSet.getObject(
                        "purchased_at",
                        java.time.LocalDateTime.class
                )
        );

        return purchase;
    }
}
