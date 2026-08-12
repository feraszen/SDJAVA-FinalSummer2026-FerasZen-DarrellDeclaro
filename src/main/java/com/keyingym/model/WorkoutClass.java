package com.keyingym.model;

import java.time.LocalDateTime;

/**
 * Represents a scheduled workout class in the gym system.
 *
 * A workout class is associated with a trainer and contains
 * the information members need to browse available classes.
 */
public class WorkoutClass {

    private int classId;
    private String className;
    private String description;
    private int trainerId;
    private LocalDateTime scheduledAt;

    public WorkoutClass() {
    }

    public WorkoutClass(
            int classId,
            String className,
            String description,
            int trainerId,
            LocalDateTime scheduledAt
    ) {
        this.classId = classId;
        this.className = className;
        this.description = description;
        this.trainerId = trainerId;
        this.scheduledAt = scheduledAt;
    }

    public int getClassId() {
        return classId;
    }

    public void setClassId(int classId) {
        this.classId = classId;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getTrainerId() {
        return trainerId;
    }

    public void setTrainerId(int trainerId) {
        this.trainerId = trainerId;
    }

    public LocalDateTime getScheduledAt() {
        return scheduledAt;
    }

    public void setScheduledAt(LocalDateTime scheduledAt) {
        this.scheduledAt = scheduledAt;
    }
}