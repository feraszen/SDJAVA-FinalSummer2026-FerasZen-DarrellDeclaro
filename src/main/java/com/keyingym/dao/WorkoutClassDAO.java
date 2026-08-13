package com.keyingym.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.keyingym.config.AppLogger;
import com.keyingym.config.DatabaseConnection;
import com.keyingym.model.WorkoutClass;

/**
 * Data Access Object for workout classes.
 */
public class WorkoutClassDAO {

    public boolean addWorkoutClass(WorkoutClass workoutClass) {
        String sql = """
                INSERT INTO workout_classes
                    (class_name, description, trainer_id, scheduled_at)
                VALUES (?, ?, ?, ?)
                """;

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(
                    1,
                    workoutClass.getClassName()
            );

            statement.setString(
                    2,
                    workoutClass.getDescription()
            );

            statement.setInt(
                    3,
                    workoutClass.getTrainerId()
            );

            statement.setObject(
                    4,
                    workoutClass.getScheduledAt()
            );

            return statement.executeUpdate() > 0;

        } catch (SQLException e) {
            AppLogger.error(
                    "Database transaction error in WorkoutClassDAO.addWorkoutClass.",
                    e
            );
            return false;
        }
    }

    public WorkoutClass findById(int classId) {
        String sql = """
                SELECT class_id, class_name, description, trainer_id, scheduled_at
                FROM workout_classes
                WHERE class_id = ?
                """;

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, classId);

            try (ResultSet resultSet = statement.executeQuery()) {

                if (resultSet.next()) {
                    return mapWorkoutClass(resultSet);
                }
            }

        } catch (SQLException e) {
            AppLogger.error(
                    "Database transaction error in WorkoutClassDAO.findById.",
                    e
            );
        }

        return null;
    }

    public List<WorkoutClass> getAllWorkoutClasses() {
        String sql = """
                SELECT class_id, class_name, description, trainer_id, scheduled_at
                FROM workout_classes
                ORDER BY scheduled_at ASC
                """;

        List<WorkoutClass> workoutClasses =
                new ArrayList<>();

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql);
             ResultSet resultSet =
                     statement.executeQuery()) {

            while (resultSet.next()) {
                workoutClasses.add(
                        mapWorkoutClass(resultSet)
                );
            }

        } catch (SQLException e) {
            AppLogger.error(
                    "Database transaction error in WorkoutClassDAO.getAllWorkoutClasses.",
                    e
            );
        }

        return workoutClasses;
    }

    public boolean updateWorkoutClass(
            WorkoutClass workoutClass) {

        String sql = """
                UPDATE workout_classes
                SET class_name = ?,
                    description = ?,
                    trainer_id = ?,
                    scheduled_at = ?
                WHERE class_id = ?
                """;

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setString(
                    1,
                    workoutClass.getClassName()
            );

            statement.setString(
                    2,
                    workoutClass.getDescription()
            );

            statement.setInt(
                    3,
                    workoutClass.getTrainerId()
            );

            statement.setObject(
                    4,
                    workoutClass.getScheduledAt()
            );

            statement.setInt(
                    5,
                    workoutClass.getClassId()
            );

            return statement.executeUpdate() > 0;

        } catch (SQLException e) {
            AppLogger.error(
                    "Database transaction error in WorkoutClassDAO.updateWorkoutClass.",
                    e
            );
            return false;
        }
    }

    public boolean deleteWorkoutClass(int classId) {

        String sql =
                "DELETE FROM workout_classes WHERE class_id = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setInt(1, classId);

            return statement.executeUpdate() > 0;

        } catch (SQLException e) {
            AppLogger.error(
                    "Database transaction error in WorkoutClassDAO.deleteWorkoutClass.",
                    e
            );
            return false;
        }
    }

    private WorkoutClass mapWorkoutClass(
            ResultSet resultSet)
            throws SQLException {

        WorkoutClass workoutClass =
                new WorkoutClass();

        workoutClass.setClassId(
                resultSet.getInt("class_id")
        );

        workoutClass.setClassName(
                resultSet.getString("class_name")
        );

        workoutClass.setDescription(
                resultSet.getString("description")
        );

        workoutClass.setTrainerId(
                resultSet.getInt("trainer_id")
        );

        workoutClass.setScheduledAt(
                resultSet.getObject(
                        "scheduled_at",
                        java.time.LocalDateTime.class
                )
        );

        return workoutClass;
    }
}