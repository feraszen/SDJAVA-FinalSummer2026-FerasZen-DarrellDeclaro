package com.keyingym.dao;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

import com.keyingym.model.User;
import com.keyingym.model.UserRole;
import com.keyingym.model.WorkoutClass;

class WorkoutClassDAOTest {

    @Test
    void shouldPerformWorkoutClassCrudOperations() {
        UserDAO userDAO = new UserDAO();
        WorkoutClassDAO workoutClassDAO = new WorkoutClassDAO();

        String uniqueId = UUID.randomUUID().toString().substring(0, 8);
        String username = "trainer_" + uniqueId;
        String className = "Test Class " + uniqueId;

        int trainerId = 0;
        int classId = 0;

        try {
            User trainer = new User(
                    0,
                    username,
                    "test-password-hash",
                    username + "@example.com",
                    "709-555-0100",
                    "Test Address",
                    UserRole.TRAINER
            );

            assertTrue(userDAO.addUser(trainer));

            User createdTrainer = userDAO.findByUsername(username);
            assertNotNull(createdTrainer);

            trainerId = createdTrainer.getUserId();
            final int createdTrainerId = trainerId;

            LocalDateTime scheduledAt =
                    LocalDateTime.now().withNano(0).plusDays(7);

            WorkoutClass workoutClass = new WorkoutClass(
                    0,
                    className,
                    "Test workout class",
                    trainerId,
                    scheduledAt
            );

            assertTrue(workoutClassDAO.addWorkoutClass(workoutClass));

            WorkoutClass createdWorkoutClass = workoutClassDAO
                    .getAllWorkoutClasses()
                    .stream()
                    .filter(item ->
                            className.equals(item.getClassName())
                                    && createdTrainerId
                                    == item.getTrainerId())
                    .findFirst()
                    .orElse(null);

            assertNotNull(createdWorkoutClass);
            classId = createdWorkoutClass.getClassId();

            assertEquals(
                    "Test workout class",
                    createdWorkoutClass.getDescription()
            );
            assertEquals(
                    scheduledAt,
                    createdWorkoutClass.getScheduledAt()
            );

            assertNotNull(workoutClassDAO.findById(classId));

            LocalDateTime updatedScheduledAt = scheduledAt.plusHours(1);
            createdWorkoutClass.setDescription("Updated workout class");
            createdWorkoutClass.setScheduledAt(updatedScheduledAt);

            assertTrue(workoutClassDAO.updateWorkoutClass(
                    createdWorkoutClass
            ));

            WorkoutClass updatedWorkoutClass =
                    workoutClassDAO.findById(classId);

            assertNotNull(updatedWorkoutClass);
            assertEquals(
                    "Updated workout class",
                    updatedWorkoutClass.getDescription()
            );
            assertEquals(
                    updatedScheduledAt,
                    updatedWorkoutClass.getScheduledAt()
            );

            assertTrue(workoutClassDAO.deleteWorkoutClass(classId));
            assertNull(workoutClassDAO.findById(classId));

        } finally {
            if (classId != 0) {
                workoutClassDAO.deleteWorkoutClass(classId);

            } else if (trainerId != 0) {
                final int cleanupTrainerId = trainerId;

                workoutClassDAO.getAllWorkoutClasses()
                        .stream()
                        .filter(item ->
                                className.equals(item.getClassName())
                                        && cleanupTrainerId
                                        == item.getTrainerId())
                        .findFirst()
                        .ifPresent(item ->
                                workoutClassDAO.deleteWorkoutClass(
                                        item.getClassId()
                                ));
            }

            if (trainerId != 0) {
                userDAO.deleteUser(trainerId);

            } else {
                User remainingTrainer = userDAO.findByUsername(username);

                if (remainingTrainer != null) {
                    userDAO.deleteUser(remainingTrainer.getUserId());
                }
            }
        }
    }
}