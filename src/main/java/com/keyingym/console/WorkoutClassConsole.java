package com.keyingym.console;

import java.time.LocalDateTime;
import java.util.List;

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
     * Displays all workout classes.
     */
    public void browse() {
        List<WorkoutClass> classes =
                workoutClassService.getSafeWorkoutClasses();

        System.out.println();
        System.out.println("---------- WORKOUT CLASSES ----------");

        if (classes.isEmpty()) {
            System.out.println("No workout classes found.");
            return;
        }

        for (WorkoutClass workoutClass : classes) {
            System.out.println(
                    "ID: " + workoutClass.getClassId()
                            + " | Name: " + workoutClass.getClassName()
                            + " | Description: " + workoutClass.getDescription()
                            + " | Trainer ID: " + workoutClass.getTrainerId()
                            + " | Scheduled: " + workoutClass.getScheduledAt()
            );
        }

        System.out.println("-------------------------------------");
    }

    /**
     * Manages workout classes for Admin and Trainer users.
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
                    browse();
                    break;
                case 2:
                    add();
                    break;
                case 3:
                    update();
                    break;
                case 4:
                    delete();
                    break;
                case 5:
                    return;
                default:
                    System.out.println("Invalid option.");
                    break;
            }
        }
    }

    private void add() {
        System.out.println();
        System.out.println("--------- ADD CLASS ---------");

        System.out.print("Class name: ");
        String className = input.readLine();

        System.out.print("Description: ");
        String description = input.readLine();

        System.out.print("Trainer ID: ");
        Integer trainerId = input.readInteger();

        System.out.print("Scheduled date/time (YYYY-MM-DDTHH:MM): ");
        LocalDateTime scheduledAt = input.readDateTime();

        if (className == null || description == null || trainerId == null
                || trainerId <= 0 || scheduledAt == null) {
            System.out.println("Invalid workout class information.");
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
            System.out.println("Workout class added successfully.");
        } else {
            System.out.println("Unable to add workout class.");
        }
    }

    private void update() {
        System.out.print("Enter class ID to update: ");
        Integer classId = input.readInteger();

        if (classId == null || classId <= 0) {
            System.out.println("Invalid class ID.");
            return;
        }

        WorkoutClass workoutClass =
                workoutClassService.findWorkoutClassById(classId);

        if (workoutClass == null) {
            System.out.println("Workout class not found.");
            return;
        }

        System.out.print("New class name: ");
        String className = input.readLine();

        System.out.print("New description: ");
        String description = input.readLine();

        System.out.print("New trainer ID: ");
        Integer trainerId = input.readInteger();

        System.out.print("New scheduled date/time (YYYY-MM-DDTHH:MM): ");
        LocalDateTime scheduledAt = input.readDateTime();

        if (className == null || description == null || trainerId == null
                || trainerId <= 0 || scheduledAt == null || className.isBlank()) {
            System.out.println("Invalid workout class information.");
            return;
        }

        workoutClass.setClassName(className);
        workoutClass.setDescription(description);
        workoutClass.setTrainerId(trainerId);
        workoutClass.setScheduledAt(scheduledAt);

        if (workoutClassService.updateWorkoutClass(workoutClass)) {
            System.out.println("Workout class updated successfully.");
        } else {
            System.out.println("Unable to update workout class.");
        }
    }

    private void delete() {
        System.out.print("Enter class ID to delete: ");
        Integer classId = input.readInteger();

        if (classId == null || classId <= 0) {
            System.out.println("Invalid class ID.");
            return;
        }

        WorkoutClass workoutClass =
                workoutClassService.findWorkoutClassById(classId);

        if (workoutClass == null) {
            System.out.println("Workout class not found.");
            return;
        }

        System.out.print("Confirm deletion? Enter Y to continue: ");
        String confirmation = input.readLine();

        if (!"Y".equalsIgnoreCase(confirmation)) {
            System.out.println("Deletion cancelled.");
            return;
        }

        if (workoutClassService.deleteWorkoutClass(classId)) {
            System.out.println("Workout class deleted successfully.");
        } else {
            System.out.println("Unable to delete workout class.");
        }
    }
}
