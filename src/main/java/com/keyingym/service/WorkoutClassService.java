package com.keyingym.service;

import com.keyingym.dao.WorkoutClassDAO;
import com.keyingym.model.WorkoutClass;

import java.util.Collections;
import java.util.List;

/**
 * Provides business operations for workout classes.
 */
public class WorkoutClassService {

    private final WorkoutClassDAO workoutClassDAO;

    /**
     * Creates the service using the application's real DAO.
     */
    public WorkoutClassService() {
        this(new WorkoutClassDAO());
    }

    /**
     * Constructor used for testing with a supplied DAO.
     *
     * @param workoutClassDAO workout class data access object
     */
    WorkoutClassService(WorkoutClassDAO workoutClassDAO) {
        this.workoutClassDAO = workoutClassDAO;
    }

    /**
     * Returns all workout classes ordered by scheduled time.
     *
     * @return list of workout classes
     */
    public List<WorkoutClass> getAllWorkoutClasses() {
        return workoutClassDAO.getAllWorkoutClasses();
    }

    /**
     * Finds a workout class by ID.
     *
     * @param classId workout class ID
     * @return matching workout class, or null when not found
     */
    public WorkoutClass findWorkoutClassById(int classId) {
        if (classId <= 0) {
            return null;
        }

        return workoutClassDAO.findById(classId);
    }

    /**
     * Adds a workout class after validating required information.
     *
     * @param workoutClass workout class to add
     * @return true when the class is successfully added
     */
    public boolean addWorkoutClass(WorkoutClass workoutClass) {
        if (!isValidWorkoutClass(workoutClass)) {
            return false;
        }

        return workoutClassDAO.addWorkoutClass(workoutClass);
    }

    /**
     * Updates an existing workout class after validation.
     *
     * @param workoutClass workout class to update
     * @return true when the class is successfully updated
     */
    public boolean updateWorkoutClass(WorkoutClass workoutClass) {
        if (!isValidWorkoutClass(workoutClass)
                || workoutClass.getClassId() <= 0) {
            return false;
        }

        return workoutClassDAO.updateWorkoutClass(workoutClass);
    }

    /**
     * Deletes a workout class by ID.
     *
     * @param classId workout class ID
     * @return true when the class is successfully deleted
     */
    public boolean deleteWorkoutClass(int classId) {
        if (classId <= 0) {
            return false;
        }

        return workoutClassDAO.deleteWorkoutClass(classId);
    }

    /**
     * Returns an empty list for an invalid user-facing request.
     * This method is kept separate so invalid list requests do not return null.
     */
    public List<WorkoutClass> getSafeWorkoutClasses() {
        List<WorkoutClass> classes = workoutClassDAO.getAllWorkoutClasses();

        if (classes == null) {
            return Collections.emptyList();
        }

        return classes;
    }

    private boolean isValidWorkoutClass(WorkoutClass workoutClass) {
        if (workoutClass == null) {
            return false;
        }

        if (isBlank(workoutClass.getClassName())) {
            return false;
        }

        if (workoutClass.getTrainerId() <= 0) {
            return false;
        }

        return workoutClass.getScheduledAt() != null;
    }

    private boolean isBlank(String value) {
        return value == null || value.isBlank();
    }
}