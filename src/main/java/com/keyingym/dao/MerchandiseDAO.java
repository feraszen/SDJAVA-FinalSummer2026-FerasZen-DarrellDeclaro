package com.keyingym.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.keyingym.config.AppLogger;
import com.keyingym.config.DatabaseConnection;
import com.keyingym.model.Merchandise;

/**
 * Data Access Object for gym merchandise.
 */
public class MerchandiseDAO {

    public boolean addMerchandise(Merchandise merchandise) {
        String sql = """
                INSERT INTO gym_merch
                    (product_name, type, price, current_stock)
                VALUES (?, ?, ?, ?)
                """;

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, merchandise.getProductName());
            statement.setString(2, merchandise.getType());
            statement.setBigDecimal(3, merchandise.getPrice());
            statement.setInt(4, merchandise.getCurrentStock());

            return statement.executeUpdate() > 0;

        } catch (SQLException e) {
            AppLogger.error(
                    "Database transaction error in MerchandiseDAO.addMerchandise.",
                    e
            );
            return false;
        }
    }

    public Merchandise findById(int merchId) {
        String sql = """
                SELECT merch_id, product_name, type, price, current_stock
                FROM gym_merch
                WHERE merch_id = ?
                """;

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, merchId);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return mapMerchandise(resultSet);
                }
            }

        } catch (SQLException e) {
            AppLogger.error(
                    "Database transaction error in MerchandiseDAO.findById.",
                    e
            );
        }

        return null;
    }

    public List<Merchandise> getAllMerchandise() {
        String sql = """
                SELECT merch_id, product_name, type, price, current_stock
                FROM gym_merch
                ORDER BY merch_id ASC
                """;

        List<Merchandise> merchandiseItems = new ArrayList<>();

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                merchandiseItems.add(mapMerchandise(resultSet));
            }

        } catch (SQLException e) {
            AppLogger.error(
                    "Database transaction error in MerchandiseDAO.getAllMerchandise.",
                    e
            );
        }

        return merchandiseItems;
    }

    public boolean updateMerchandise(Merchandise merchandise) {
        String sql = """
                UPDATE gym_merch
                SET product_name = ?,
                    type = ?,
                    price = ?,
                    current_stock = ?
                WHERE merch_id = ?
                """;

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, merchandise.getProductName());
            statement.setString(2, merchandise.getType());
            statement.setBigDecimal(3, merchandise.getPrice());
            statement.setInt(4, merchandise.getCurrentStock());
            statement.setInt(5, merchandise.getMerchId());

            return statement.executeUpdate() > 0;

        } catch (SQLException e) {
            AppLogger.error(
                    "Database transaction error in MerchandiseDAO.updateMerchandise.",
                    e
            );
            return false;
        }
    }

    public boolean deleteMerchandise(int merchId) {
        String sql = "DELETE FROM gym_merch WHERE merch_id = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, merchId);

            return statement.executeUpdate() > 0;

        } catch (SQLException e) {
            AppLogger.error(
                    "Database transaction error in MerchandiseDAO.deleteMerchandise.",
                    e
            );
            return false;
        }
    }

    private Merchandise mapMerchandise(ResultSet resultSet)
            throws SQLException {

        Merchandise merchandise = new Merchandise();

        merchandise.setMerchId(
                resultSet.getInt("merch_id")
        );

        merchandise.setProductName(
                resultSet.getString("product_name")
        );

        merchandise.setType(
                resultSet.getString("type")
        );

        merchandise.setPrice(
                resultSet.getBigDecimal("price")
        );

        merchandise.setCurrentStock(
                resultSet.getInt("current_stock")
        );

        return merchandise;
    }
}