package project.whatsassist.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import project.whatsassist.example.demo.enuns.Status;
import project.whatsassist.example.demo.model.Routine;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface RoutineRepository extends JpaRepository<Routine, Long> {
    List<Routine> findByStatus(Status status);
    List<Routine> findByScheduledAtBetweenAndStatus(
            LocalDateTime start,
            LocalDateTime end,
            Status status
    );
    List<Routine> findByStatusAndCompletedAtAfterOrderByCompletedAtDesc(
            Status status,
            LocalDateTime start

    );

}
