package com.keyingym.console;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import com.keyingym.model.User;
import com.keyingym.model.UserRole;
import com.keyingym.model.WorkoutClass;
import com.keyingym.service.WorkoutClassService;

/**
 * Console workflows for browsing and managing workout classes.
 */
public class WorkoutClassConsole {

    private final WorkoutClassService workoutClassService;
    private final ConsoleInput input;

    public WorkoutClassConsole(
            WorkoutClassService workoutClassService,
            ConsoleInput input
    ) {
        this.workoutClassService = workoutClassService;
        this.input = input;
    }

    /**
     * Displays workout classes according to the user's role.
     *
     * ADMIN:
     * Displays all workout classes.
     *
     * TRAINER:
     * Displays only workout classes assigned to the logged-in trainer.
     *
     * MEMBER:
     * Displays all available workout classes.
     *
     * @param user currently authenticated user
     */
    public void browse(User user) {
        if (user == null || user.getRole() == null) {
            System.out.println("Authentication required.");
            return;
        }

        List<WorkoutClass> classes =
                workoutClassService.getSafeWorkoutClasses();

        if (user.getRole() == UserRole.TRAINER) {
            classes = classes.stream()
                    .filter(workoutClass ->
                            workoutClass.getTrainerId() == user.getUserId())
                    .collect(Collectors.toList());
        }

        System.out.println();
        System.out.println("---------- WORKOUT CLASSES ----------");

        if (classes.isEmpty()) {
            if (user.getRole() == UserRole.TRAINER) {
                System.out.println(
                        "No workout classes are assigned to you."
                );
            } else {
                System.out.println("No workout classes found.");
            }

            System.out.println("-------------------------------------");
            return;
        }

        for (WorkoutClass workoutClass : classes) {
            System.out.println(
                    "ID: " + workoutClass.getClassId()
                            + " | Name: " + workoutClass.getClassName()
                            + " | Description: "
                            + workoutClass.getDescription()
                            + " | Trainer ID: "
                            + workoutClass.getTrainerId()
                            + " | Scheduled: "
                            + workoutClass.getScheduledAt()
            );
        }

        System.out.println("-------------------------------------");
    }

    /**
     * Backward-compatible browse method.
     *
     * This method displays all workout classes.
     * Role-specific menu workflows should use browse(User).
     */
    public void browse() {
        List<WorkoutClass> classes =
                workoutClassService.getSafeWorkoutClasses();

        System.out.println();
        System.out.println("---------- WORKOUT CLASSES ----------");

        if (classes.isEmpty()) {
            System.out.println("No workout classes found.");
            System.out.println("-------------------------------------");
            return;
        }

        for (WorkoutClass workoutClass : classes) {
            System.out.println(
                    "ID: " + workoutClass.getClassId()
                            + " | Name: " + workoutClass.getClassName()
                            + " | Description: "
                            + workoutClass.getDescription()
                            + " | Trainer ID: "
                            + workoutClass.getTrainerId()
                            + " | Scheduled: "
                            + workoutClass.getScheduledAt()
            );
        }

        System.out.println("-------------------------------------");
    }

    /**
     * Manages workout classes for Admin and Trainer users.
     *
     * ADMIN:
     * Can create, update, and delete any workout class.
     *
     * TRAINER:
     * Can create workout classes assigned to themselves and
     * can update or delete only their own assigned classes.
     */
    public void manage(User user) {
        if (user == null
                || (user.getRole() != UserRole.ADMIN
                && user.getRole() != UserRole.TRAINER)) {

            System.out.println("Access denied.");
            return;
        }

        while (true) {
            System.out.println();
            System.out.println("------- WORKOUT CLASSES -------");
            System.out.println("1. View Classes");
            System.out.println("2. Add Class");
            System.out.println("3. Update Class");
            System.out.println("4. Delete Class");
            System.out.println("5. Return");
            System.out.println("-------------------------------");

            System.out.print("Select an option: ");

            if (!input.hasNextLine()) {
                return;
            }

            Integer option = input.readInteger();

            if (option == null) {
                System.out.println("Invalid option.");
                continue;
            }

            switch (option) {

                case 1:
                    browse(user);
                    break;

                case 2:
                    add(user);
                    break;

                case 3:
                    update(user);
                    break;

                case 4:
                    delete(user);
                    break;

                case 5:
                    return;

                default:
                    System.out.println("Invalid option.");
                    break;
            }
        }
    }

    /**
     * Adds a workout class.
     *
     * ADMIN:
     * Admin chooses the trainer ID.
     *
     * TRAINER:
     * The logged-in trainer is automatically assigned to the class.
     */
    private void add(User user) {
        System.out.println();
        System.out.println("--------- ADD CLASS ---------");

        System.out.print("Class name: ");
        String className = input.readLine();

        System.out.print("Description: ");
        String description = input.readLine();

        Integer trainerId;

        if (user.getRole() == UserRole.ADMIN) {
            System.out.print("Trainer ID: ");
            trainerId = input.readInteger();
        } else {
            trainerId = user.getUserId();
            System.out.println(
                    "Trainer ID: " + trainerId
                            + " (logged-in trainer)"
            );
        }

        System.out.print(
                "Scheduled date/time (YYYY-MM-DDTHH:MM): "
        );

        LocalDateTime scheduledAt = input.readDateTime();

        if (className == null
                || className.isBlank()
                || description == null
                || trainerId == null
                || trainerId <= 0
                || scheduledAt == null) {

            System.out.println(
                    "Invalid workout class information."
            );
            return;
        }

        WorkoutClass workoutClass = new WorkoutClass(
                0,
                className,
                description,
                trainerId,
                scheduledAt
        );

        if (workoutClassService.addWorkoutClass(workoutClass)) {
            System.out.println(
                    "Workout class added successfully."
            );
        } else {
            System.out.println(
                    "Unable to add workout class."
            );
        }
    }

    /**
     * Updates a workout class.
     *
     * ADMIN:
     * Can update any workout class.
     *
     * TRAINER:
     * Can update only a class assigned to the logged-in trainer.
     */
    private void update(User user) {
        System.out.print(
                "Enter class ID to update: "
        );

        Integer classId = input.readInteger();

        if (classId == null || classId <= 0) {
            System.out.println("Invalid class ID.");
            return;
        }

        WorkoutClass workoutClass =
                workoutClassService.findWorkoutClassById(classId);

        if (workoutClass == null) {
            System.out.println(
                    "Workout class not found."
            );
            return;
        }

        if (user.getRole() == UserRole.TRAINER
                && workoutClass.getTrainerId() != user.getUserId()) {

            System.out.println(
                    "Access denied. You can only update your own classes."
            );
            return;
        }

        System.out.print("New class name: ");
        String className = input.readLine();

        System.out.print("New description: ");
        String description = input.readLine();

        Integer trainerId;

        if (user.getRole() == UserRole.ADMIN) {
            System.out.print("New trainer ID: ");
            trainerId = input.readInteger();
        } else {
            trainerId = user.getUserId();
            System.out.println(
                    "Trainer ID: " + trainerId
                            + " (logged-in trainer)"
            );
        }

        System.out.print(
                "New scheduled date/time (YYYY-MM-DDTHH:MM): "
        );

        LocalDateTime scheduledAt =
                input.readDateTime();

        if (className == null
                || className.isBlank()
                || description == null
                || trainerId == null
                || trainerId <= 0
                || scheduledAt == null) {

            System.out.println(
                    "Invalid workout class information."
            );
            return;
        }

        workoutClass.setClassName(className);
        workoutClass.setDescription(description);
        workoutClass.setTrainerId(trainerId);
        workoutClass.setScheduledAt(scheduledAt);

        if (workoutClassService.updateWorkoutClass(
                workoutClass)) {

            System.out.println(
                    "Workout class updated successfully."
            );

        } else {
            System.out.println(
                    "Unable to update workout class."
            );
        }
    }

    /**
     * Deletes a workout class.
     *
     * ADMIN:
     * Can delete any workout class.
     *
     * TRAINER:
     * Can delete only a class assigned to the logged-in trainer.
     */
    private void delete(User user) {
        System.out.print(
                "Enter class ID to delete: "
        );

        Integer classId = input.readInteger();

        if (classId == null || classId <= 0) {
            System.out.println("Invalid class ID.");
            return;
        }

        WorkoutClass workoutClass =
                workoutClassService.findWorkoutClassById(classId);

        if (workoutClass == null) {
            System.out.println(
                    "Workout class not found."
            );
            return;
        }

        if (user.getRole() == UserRole.TRAINER
                && workoutClass.getTrainerId() != user.getUserId()) {

            System.out.println(
                    "Access denied. You can only delete your own classes."
            );
            return;
        }

        System.out.print(
                "Confirm deletion? Enter Y to continue: "
        );

        String confirmation = input.readLine();

        if (!"Y".equalsIgnoreCase(confirmation)) {
            System.out.println(
                    "Deletion cancelled."
            );
            return;
        }

        if (workoutClassService.deleteWorkoutClass(classId)) {
            System.out.println(
                    "Workout class deleted successfully."
            );
        } else {
            System.out.println(
                    "Unable to delete workout class."
            );
        }
    }
}