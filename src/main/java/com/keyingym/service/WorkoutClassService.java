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

    public WorkoutClassService() {
        this(new WorkoutClassDAO());
    }

    WorkoutClassService(WorkoutClassDAO workoutClassDAO) {
        this.workoutClassDAO = workoutClassDAO;
    }

    public List<WorkoutClass> getAllWorkoutClasses() {
        return workoutClassDAO.getAllWorkoutClasses();
    }

    /**
     * Returns only classes assigned to the specified trainer.
     */
    public List<WorkoutClass> getWorkoutClassesByTrainerId(int trainerId) {
        if (trainerId <= 0) {
            return Collections.emptyList();
        }

        List<WorkoutClass> classes =
                workoutClassDAO.getWorkoutClassesByTrainerId(trainerId);

        if (classes == null) {
            return Collections.emptyList();
        }

        return classes;
    }

    public WorkoutClass findWorkoutClassById(int classId) {
        if (classId <= 0) {
            return null;
        }

        return workoutClassDAO.findById(classId);
    }

    public boolean addWorkoutClass(WorkoutClass workoutClass) {
        if (!isValidWorkoutClass(workoutClass)) {
            return false;
        }

        return workoutClassDAO.addWorkoutClass(workoutClass);
    }

    public boolean updateWorkoutClass(WorkoutClass workoutClass) {
        if (!isValidWorkoutClass(workoutClass)
                || workoutClass.getClassId() <= 0) {
            return false;
        }

        return workoutClassDAO.updateWorkoutClass(workoutClass);
    }

    public boolean deleteWorkoutClass(int classId) {
        if (classId <= 0) {
            return false;
        }

        return workoutClassDAO.deleteWorkoutClass(classId);
    }

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
