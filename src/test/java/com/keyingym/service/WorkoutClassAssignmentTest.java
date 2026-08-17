package com.keyingym.service;

import com.keyingym.dao.WorkoutClassDAO;
import com.keyingym.model.WorkoutClass;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class WorkoutClassAssignmentTest {

    @Test
    void shouldReturnOnlyClassesAssignedToRequestedTrainer() {
        FakeWorkoutClassDAO dao = new FakeWorkoutClassDAO();

        dao.classes.add(new WorkoutClass(
                1,
                "Trainer One Class",
                "Class for trainer one",
                101,
                LocalDateTime.of(2026, 8, 20, 10, 0)
        ));

        dao.classes.add(new WorkoutClass(
                2,
                "Trainer Two Class",
                "Class for trainer two",
                202,
                LocalDateTime.of(2026, 8, 20, 11, 0)
        ));

        WorkoutClassService service = new WorkoutClassService(dao);
        List<WorkoutClass> assigned =
                service.getWorkoutClassesByTrainerId(101);

        assertEquals(1, assigned.size());
        assertEquals(101, assigned.get(0).getTrainerId());
        assertEquals("Trainer One Class", assigned.get(0).getClassName());
    }

    @Test
    void shouldReturnEmptyListForInvalidTrainerId() {
        WorkoutClassService service =
                new WorkoutClassService(new FakeWorkoutClassDAO());

        assertTrue(service.getWorkoutClassesByTrainerId(0).isEmpty());
        assertTrue(service.getWorkoutClassesByTrainerId(-1).isEmpty());
    }

    private static class FakeWorkoutClassDAO extends WorkoutClassDAO {
        private final List<WorkoutClass> classes = new ArrayList<>();

        @Override
        public List<WorkoutClass> getWorkoutClassesByTrainerId(int trainerId) {
            return classes.stream()
                    .filter(item -> item.getTrainerId() == trainerId)
                    .toList();
        }
    }
}
