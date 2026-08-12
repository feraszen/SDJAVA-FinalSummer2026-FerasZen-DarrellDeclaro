package com.keyingym.dao;

import com.keyingym.config.DatabaseConnection;
import com.keyingym.model.MembershipPurchase;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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
            e.printStackTrace();
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
            e.printStackTrace();
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
            e.printStackTrace();
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
            e.printStackTrace();
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
            e.printStackTrace();
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
                        java.time.LocalDateTime.class
                )
        );

        return purchase;
    }
}