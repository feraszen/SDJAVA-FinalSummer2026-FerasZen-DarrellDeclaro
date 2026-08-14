package com.keyingym.dao;

import com.keyingym.config.AppLogger;
import com.keyingym.config.DatabaseConnection;
import com.keyingym.model.MembershipPurchase;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object for membership purchase records.
 */
public class MembershipPurchaseDAO {

    public boolean addMembershipPurchase(MembershipPurchase purchase) {
        String sql = """
                INSERT INTO membership_purchases
                    (user_id, membership_id, price, purchased_at)
                VALUES (?, ?, ?, ?)
                """;

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, purchase.getUserId());
            statement.setInt(2, purchase.getMembershipId());
            statement.setBigDecimal(3, purchase.getPrice());
            statement.setObject(4, purchase.getPurchasedAt());

            return statement.executeUpdate() > 0;

        } catch (SQLException e) {
            AppLogger.error("Database transaction error while adding membership purchase.", e);
            return false;
        }
    }

    public MembershipPurchase findById(int purchaseId) {
        String sql = """
                SELECT purchase_id, user_id, membership_id, price, purchased_at
                FROM membership_purchases
                WHERE purchase_id = ?
                """;

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, purchaseId);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return mapMembershipPurchase(resultSet);
                }
            }

        } catch (SQLException e) {
            AppLogger.error("Database transaction error while finding membership purchase: " + purchaseId, e);
        }

        return null;
    }

    public List<MembershipPurchase> getAllMembershipPurchases() {
        String sql = """
                SELECT purchase_id, user_id, membership_id, price, purchased_at
                FROM membership_purchases
                ORDER BY purchase_id ASC
                """;

        List<MembershipPurchase> purchases = new ArrayList<>();

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                purchases.add(mapMembershipPurchase(resultSet));
            }

        } catch (SQLException e) {
            AppLogger.error("Database transaction error while loading membership purchases.", e);
        }

        return purchases;
    }

    /**
     * Returns membership purchases made during the current calendar year.
     */
    public List<MembershipPurchase> getCurrentYearMembershipPurchases() {
        LocalDateTime startOfYear = LocalDate.of(LocalDate.now().getYear(), 1, 1).atStartOfDay();
        LocalDateTime startOfNextYear = startOfYear.plusYears(1);

        String sql = """
                SELECT purchase_id, user_id, membership_id, price, purchased_at
                FROM membership_purchases
                WHERE purchased_at >= ? AND purchased_at < ?
                ORDER BY purchased_at ASC
                """;

        List<MembershipPurchase> purchases = new ArrayList<>();

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setObject(1, startOfYear);
            statement.setObject(2, startOfNextYear);

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    purchases.add(mapMembershipPurchase(resultSet));
                }
            }

        } catch (SQLException e) {
            AppLogger.error("Database transaction error while loading current-year membership purchases.", e);
        }

        return purchases;
    }

    public List<MembershipPurchase> getPurchasesByUserId(int userId) {
        String sql = """
                SELECT purchase_id, user_id, membership_id, price, purchased_at
                FROM membership_purchases
                WHERE user_id = ?
                ORDER BY purchased_at DESC
                """;

        List<MembershipPurchase> purchases = new ArrayList<>();

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, userId);

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    purchases.add(mapMembershipPurchase(resultSet));
                }
            }

        } catch (SQLException e) {
            AppLogger.error("Database transaction error while loading purchases for user: " + userId, e);
        }

        return purchases;
    }

    public boolean deleteMembershipPurchase(int purchaseId) {
        String sql = "DELETE FROM membership_purchases WHERE purchase_id = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, purchaseId);

            return statement.executeUpdate() > 0;

        } catch (SQLException e) {
            AppLogger.error("Database transaction error while deleting membership purchase: " + purchaseId, e);
            return false;
        }
    }

    private MembershipPurchase mapMembershipPurchase(ResultSet resultSet)
            throws SQLException {

        MembershipPurchase purchase = new MembershipPurchase();

        purchase.setPurchaseId(resultSet.getInt("purchase_id"));
        purchase.setUserId(resultSet.getInt("user_id"));
        purchase.setMembershipId(resultSet.getInt("membership_id"));
        purchase.setPrice(resultSet.getBigDecimal("price"));
        purchase.setPurchasedAt(
                resultSet.getObject(
                        "purchased_at",
                        LocalDateTime.class
                )
        );

        return purchase;
    }
}