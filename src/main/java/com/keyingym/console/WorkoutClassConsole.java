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

    /** Displays all workout classes for general browsing or Admin users. */
    public void browse() {
        displayClasses(workoutClassService.getSafeWorkoutClasses(),
                "---------- WORKOUT CLASSES ----------");
    }

    /** Displays only classes assigned to the logged-in Trainer. */
    public void browseAssigned(User user) {
        if (user == null || user.getRole() != UserRole.TRAINER) {
            System.out.println("Access denied. Trainer access required.");
            return;
        }

        List<WorkoutClass> classes =
                workoutClassService.getWorkoutClassesByTrainerId(user.getUserId());

        displayClasses(classes,
                "------- MY ASSIGNED CLASSES -------");
    }

    /**
     * Manages workout classes for Admin and Trainer users.
     * Trainers can manage only classes assigned to themselves.
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
                    if (user.getRole() == UserRole.TRAINER) {
                        browseAssigned(user);
                    } else {
                        browse();
                    }
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

    private void add(User user) {
        System.out.println();
        System.out.println("--------- ADD CLASS ---------");

        System.out.print("Class name: ");
        String className = input.readLine();

        System.out.print("Description: ");
        String description = input.readLine();

        Integer trainerId;
        if (user.getRole() == UserRole.TRAINER) {
            trainerId = user.getUserId();
            System.out.println("Trainer ID: " + trainerId);
        } else {
            System.out.print("Trainer ID: ");
            trainerId = input.readInteger();
        }

        System.out.print("Scheduled date/time (YYYY-MM-DDTHH:MM): ");
        LocalDateTime scheduledAt = input.readDateTime();

        if (className == null || description == null || trainerId == null
                || trainerId <= 0 || scheduledAt == null || className.isBlank()) {
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

    private void update(User user) {
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

        if (!canManageClass(user, workoutClass)) {
            return;
        }

        System.out.print("New class name: ");
        String className = input.readLine();

        System.out.print("New description: ");
        String description = input.readLine();

        Integer trainerId;
        if (user.getRole() == UserRole.TRAINER) {
            trainerId = user.getUserId();
            System.out.println("Trainer ID: " + trainerId);
        } else {
            System.out.print("New trainer ID: ");
            trainerId = input.readInteger();
        }

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

    private void delete(User user) {
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

        if (!canManageClass(user, workoutClass)) {
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

    private boolean canManageClass(User user, WorkoutClass workoutClass) {
        if (user.getRole() == UserRole.ADMIN) {
            return true;
        }

        if (workoutClass.getTrainerId() != user.getUserId()) {
            System.out.println("Access denied. This class is assigned to another trainer.");
            return false;
        }

        return true;
    }

    private void displayClasses(List<WorkoutClass> classes, String heading) {
        System.out.println();
        System.out.println(heading);

        if (classes == null || classes.isEmpty()) {
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
}
