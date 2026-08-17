package com.keyingym.console;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import com.keyingym.config.AppLogger;
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

<<<<<<< HEAD
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
                System.out.println("No workout classes are assigned to you.");
            } else {
                System.out.println("No workout classes found.");
            }

            System.out.println("-------------------------------------");
=======
    /** Displays all workout classes for general browsing or Admin users. */
    public void browse() {
        displayClasses(workoutClassService.getSafeWorkoutClasses(),
                "---------- WORKOUT CLASSES ----------");
    }

    /** Displays only classes assigned to the logged-in Trainer. */
    public void browseAssigned(User user) {
        if (user == null || user.getRole() != UserRole.TRAINER) {
            System.out.println("Access denied. Trainer access required.");
>>>>>>> fix/final-review-priority
            return;
        }

        List<WorkoutClass> classes =
                workoutClassService.getWorkoutClassesByTrainerId(user.getUserId());

        displayClasses(classes,
                "------- MY ASSIGNED CLASSES -------");
    }

    /**
<<<<<<< HEAD
     * Backward-compatible browse method. Role-specific workflows should use browse(User).
=======
     * Manages workout classes for Admin and Trainer users.
     * Trainers can manage only classes assigned to themselves.
>>>>>>> fix/final-review-priority
     */
    public void browse() {
        List<WorkoutClass> classes = workoutClassService.getSafeWorkoutClasses();

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
                            + " | Description: " + workoutClass.getDescription()
                            + " | Trainer ID: " + workoutClass.getTrainerId()
                            + " | Scheduled: " + workoutClass.getScheduledAt()
            );
        }

        System.out.println("-------------------------------------");
    }

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
<<<<<<< HEAD
                    browse(user);
=======
                    if (user.getRole() == UserRole.TRAINER) {
                        browseAssigned(user);
                    } else {
                        browse();
                    }
>>>>>>> fix/final-review-priority
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
<<<<<<< HEAD

        if (user.getRole() == UserRole.ADMIN) {
            System.out.print("Trainer ID: ");
            trainerId = input.readInteger();
        } else {
            trainerId = user.getUserId();
            System.out.println("Trainer ID: " + trainerId + " (logged-in trainer)");
=======
        if (user.getRole() == UserRole.TRAINER) {
            trainerId = user.getUserId();
            System.out.println("Trainer ID: " + trainerId);
        } else {
            System.out.print("Trainer ID: ");
            trainerId = input.readInteger();
>>>>>>> fix/final-review-priority
        }

        System.out.print("Scheduled date/time (YYYY-MM-DDTHH:MM): ");
        LocalDateTime scheduledAt = input.readDateTime();

<<<<<<< HEAD
        if (className == null || className.isBlank()
                || description == null
                || trainerId == null || trainerId <= 0
                || scheduledAt == null) {
=======
        if (className == null || description == null || trainerId == null
                || trainerId <= 0 || scheduledAt == null || className.isBlank()) {
>>>>>>> fix/final-review-priority
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
            if (user.getRole() == UserRole.ADMIN) {
                AppLogger.info("Admin override: added workout class " + className);
            }
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

        WorkoutClass workoutClass = workoutClassService.findWorkoutClassById(classId);

        if (workoutClass == null) {
            System.out.println("Workout class not found.");
            return;
        }

<<<<<<< HEAD
        if (user.getRole() == UserRole.TRAINER
                && workoutClass.getTrainerId() != user.getUserId()) {
            System.out.println("Access denied. You can only update your own classes.");
=======
        if (!canManageClass(user, workoutClass)) {
>>>>>>> fix/final-review-priority
            return;
        }

        System.out.print("New class name: ");
        String className = input.readLine();

        System.out.print("New description: ");
        String description = input.readLine();

        Integer trainerId;
<<<<<<< HEAD

        if (user.getRole() == UserRole.ADMIN) {
            System.out.print("New trainer ID: ");
            trainerId = input.readInteger();
        } else {
            trainerId = user.getUserId();
            System.out.println("Trainer ID: " + trainerId + " (logged-in trainer)");
=======
        if (user.getRole() == UserRole.TRAINER) {
            trainerId = user.getUserId();
            System.out.println("Trainer ID: " + trainerId);
        } else {
            System.out.print("New trainer ID: ");
            trainerId = input.readInteger();
>>>>>>> fix/final-review-priority
        }

        System.out.print("New scheduled date/time (YYYY-MM-DDTHH:MM): ");
        LocalDateTime scheduledAt = input.readDateTime();

        if (className == null || className.isBlank()
                || description == null
                || trainerId == null || trainerId <= 0
                || scheduledAt == null) {
            System.out.println("Invalid workout class information.");
            return;
        }

        workoutClass.setClassName(className);
        workoutClass.setDescription(description);
        workoutClass.setTrainerId(trainerId);
        workoutClass.setScheduledAt(scheduledAt);

        if (workoutClassService.updateWorkoutClass(workoutClass)) {
            System.out.println("Workout class updated successfully.");
            if (user.getRole() == UserRole.ADMIN) {
                AppLogger.info("Admin override: updated workout class ID " + classId);
            }
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

        WorkoutClass workoutClass = workoutClassService.findWorkoutClassById(classId);

        if (workoutClass == null) {
            System.out.println("Workout class not found.");
            return;
        }

<<<<<<< HEAD
        if (user.getRole() == UserRole.TRAINER
                && workoutClass.getTrainerId() != user.getUserId()) {
            System.out.println("Access denied. You can only delete your own classes.");
=======
        if (!canManageClass(user, workoutClass)) {
>>>>>>> fix/final-review-priority
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
            if (user.getRole() == UserRole.ADMIN) {
                AppLogger.info("Admin override: deleted workout class ID " + classId);
            }
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
