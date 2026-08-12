package com.keyingym.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

import com.keyingym.dao.UserDAO;
import com.keyingym.model.User;
import com.keyingym.model.UserRole;
import com.keyingym.model.WorkoutClass;

class WorkoutClassServiceTest {

    @Test
    void shouldPerformWorkoutClassCrudOperations() {

        WorkoutClassService service =
                new WorkoutClassService();

        UserDAO userDAO = new UserDAO();

        String uniqueId =
                UUID.randomUUID()
                        .toString()
                        .substring(0, 8);

        String username =
                "trainer_test_" + uniqueId;

        String className =
                "Service Class " + uniqueId;

        int trainerId = 0;
        int classId = 0;

        try {
            // Create a real trainer user because workout_classes.trainer_id
            // references users.user_id.
            User trainer = new User(
                    0,
                    username,
                    "test-password-hash",
                    username + "@example.com",
                    "709-555-0100",
                    "Test Trainer Address",
                    UserRole.TRAINER
            );

            assertTrue(
                    userDAO.addUser(trainer)
            );

            User createdTrainer =
                    userDAO.findByUsername(username);

            assertNotNull(createdTrainer);

            trainerId =
                    createdTrainer.getUserId();

            assertTrue(trainerId > 0);

            // Create workout class
            WorkoutClass workoutClass =
                    new WorkoutClass(
                            0,
                            className,
                            "Service test class",
                            trainerId,
                            LocalDateTime.now()
                                    .plusDays(1)
                                    .withNano(0)
                    );

            // Add
            assertTrue(
                    service.addWorkoutClass(workoutClass)
            );

            // Find created class
            WorkoutClass createdClass =
                    service.getAllWorkoutClasses()
                            .stream()
                            .filter(item ->
                                    className.equals(
                                            item.getClassName()
                                    ))
                            .findFirst()
                            .orElse(null);

            assertNotNull(createdClass);

            classId =
                    createdClass.getClassId();

            assertTrue(classId > 0);

            assertEquals(
                    className,
                    createdClass.getClassName()
            );

            assertEquals(
                    "Service test class",
                    createdClass.getDescription()
            );

            assertEquals(
                    trainerId,
                    createdClass.getTrainerId()
            );

            assertNotNull(
                    createdClass.getScheduledAt()
            );

            // Find by ID
            WorkoutClass foundClass =
                    service.findWorkoutClassById(classId);

            assertNotNull(foundClass);

            assertEquals(
                    classId,
                    foundClass.getClassId()
            );

            // Update
            foundClass.setClassName(
                    className + " Updated"
            );

            foundClass.setDescription(
                    "Updated service test class"
            );

            foundClass.setTrainerId(trainerId);

            foundClass.setScheduledAt(
                    LocalDateTime.now()
                            .plusDays(2)
                            .withNano(0)
            );

            assertTrue(
                    service.updateWorkoutClass(foundClass)
            );

            WorkoutClass updatedClass =
                    service.findWorkoutClassById(classId);

            assertNotNull(updatedClass);

            assertEquals(
                    className + " Updated",
                    updatedClass.getClassName()
            );

            assertEquals(
                    "Updated service test class",
                    updatedClass.getDescription()
            );

            assertEquals(
                    trainerId,
                    updatedClass.getTrainerId()
            );

            // Safe list
            List<WorkoutClass> classes =
                    service.getSafeWorkoutClasses();

            assertNotNull(classes);

            boolean classExists = false;

            for (WorkoutClass item : classes) {
                if (item.getClassId() == classId) {
                    classExists = true;
                    break;
                }
            }

            assertTrue(classExists);

            // Delete
            assertTrue(
                    service.deleteWorkoutClass(classId)
            );

            assertNull(
                    service.findWorkoutClassById(classId)
            );

        } finally {

            if (classId != 0) {
                service.deleteWorkoutClass(classId);
            }

            if (trainerId != 0) {
                userDAO.deleteUser(trainerId);
            }
        }
    }

    @Test
    void shouldRejectInvalidWorkoutClasses() {

        WorkoutClassService service =
                new WorkoutClassService();

        LocalDateTime scheduledAt =
                LocalDateTime.now()
                        .plusDays(1)
                        .withNano(0);

        // Null object
        assertFalse(
                service.addWorkoutClass(null)
        );

        // Blank class name
        assertFalse(
                service.addWorkoutClass(
                        new WorkoutClass(
                                0,
                                " ",
                                "Description",
                                1,
                                scheduledAt
                        )
                )
        );

        // Missing trainer
        assertFalse(
                service.addWorkoutClass(
                        new WorkoutClass(
                                0,
                                "Valid Class",
                                "Description",
                                0,
                                scheduledAt
                        )
                )
        );

        // Missing scheduled date
        assertFalse(
                service.addWorkoutClass(
                        new WorkoutClass(
                                0,
                                "Valid Class",
                                "Description",
                                1,
                                null
                        )
                )
        );
    }

    @Test
    void shouldRejectInvalidIds() {

        WorkoutClassService service =
                new WorkoutClassService();

        assertNull(
                service.findWorkoutClassById(0)
        );

        assertNull(
                service.findWorkoutClassById(-1)
        );

        assertFalse(
                service.deleteWorkoutClass(0)
        );

        assertFalse(
                service.deleteWorkoutClass(-1)
        );
    }

    @Test
    void shouldRejectUpdateWithInvalidWorkoutClass() {

        WorkoutClassService service =
                new WorkoutClassService();

        LocalDateTime scheduledAt =
                LocalDateTime.now()
                        .plusDays(1)
                        .withNano(0);

        // Null object
        assertFalse(
                service.updateWorkoutClass(null)
        );

        // Invalid ID
        assertFalse(
                service.updateWorkoutClass(
                        new WorkoutClass(
                                0,
                                "Valid Class",
                                "Description",
                                1,
                                scheduledAt
                        )
                )
        );

        // Blank name
        assertFalse(
                service.updateWorkoutClass(
                        new WorkoutClass(
                                1,
                                " ",
                                "Description",
                                1,
                                scheduledAt
                        )
                )
        );

        // Invalid trainer
        assertFalse(
                service.updateWorkoutClass(
                        new WorkoutClass(
                                1,
                                "Valid Class",
                                "Description",
                                0,
                                scheduledAt
                        )
                )
        );

        // Missing scheduled time
        assertFalse(
                service.updateWorkoutClass(
                        new WorkoutClass(
                                1,
                                "Valid Class",
                                "Description",
                                1,
                                null
                        )
                )
        );
    }
}