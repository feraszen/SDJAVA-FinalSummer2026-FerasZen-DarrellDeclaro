package com.keyingym.dao;

import com.keyingym.config.AppLogger;
import com.keyingym.config.DatabaseConnection;
import com.keyingym.model.Membership;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object for membership plans.
 */
public class MembershipDAO {

    public boolean addMembership(Membership membership) {
        String sql = """
                INSERT INTO memberships (membership_type, price)
                VALUES (?, ?)
                """;

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, membership.getMembershipType());
            statement.setBigDecimal(2, membership.getPrice());
            return statement.executeUpdate() > 0;

        } catch (SQLException e) {
            AppLogger.error("Database transaction error in MembershipDAO.addMembership.", e);
            return false;
        }
    }

    public Membership findById(int membershipId) {
        String sql = """
                SELECT membership_id, membership_type, price
                FROM memberships
                WHERE membership_id = ?
                """;

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, membershipId);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return mapMembership(resultSet);
                }
            }

        } catch (SQLException e) {
            AppLogger.error("Database transaction error in MembershipDAO.findById.", e);
        }

        return null;
    }

    public Membership findByMembershipType(String membershipType) {
        String sql = """
                SELECT membership_id, membership_type, price
                FROM memberships
                WHERE membership_type = ?
                """;

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, membershipType);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return mapMembership(resultSet);
                }
            }

        } catch (SQLException e) {
            AppLogger.error(
                    "Database transaction error in MembershipDAO.findByMembershipType.",
                    e
            );
        }

        return null;
    }

    public List<Membership> getAllMemberships() {
        String sql = """
                SELECT membership_id, membership_type, price
                FROM memberships
                ORDER BY membership_id ASC
                """;

        List<Membership> memberships = new ArrayList<>();

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                memberships.add(mapMembership(resultSet));
            }

        } catch (SQLException e) {
            AppLogger.error("Database transaction error in MembershipDAO.getAllMemberships.", e);
        }

        return memberships;
    }

    public boolean updateMembership(Membership membership) {
        String sql = """
                UPDATE memberships
                SET membership_type = ?,
                    price = ?
                WHERE membership_id = ?
                """;

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, membership.getMembershipType());
            statement.setBigDecimal(2, membership.getPrice());
            statement.setInt(3, membership.getMembershipId());
            return statement.executeUpdate() > 0;

        } catch (SQLException e) {
            AppLogger.error("Database transaction error in MembershipDAO.updateMembership.", e);
            return false;
        }
    }

    public boolean deleteMembership(int membershipId) {
        String sql = "DELETE FROM memberships WHERE membership_id = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, membershipId);
            return statement.executeUpdate() > 0;

        } catch (SQLException e) {
            AppLogger.error("Database transaction error in MembershipDAO.deleteMembership.", e);
            return false;
        }
    }

    private Membership mapMembership(ResultSet resultSet) throws SQLException {
        Membership membership = new Membership();

        membership.setMembershipId(resultSet.getInt("membership_id"));
        membership.setMembershipType(resultSet.getString("membership_type"));
        membership.setPrice(resultSet.getBigDecimal("price"));

        return membership;
    }
}
