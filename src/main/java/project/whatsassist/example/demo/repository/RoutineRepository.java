package project.whatsassist.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import project.whatsassist.example.demo.enuns.Status;
import project.whatsassist.example.demo.model.Routine;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface RoutineRepository extends JpaRepository<Routine, Long> {
    List<Routine> findByStatusAndPhoneNumber(Status status, String phoneNumber);
    List<Routine> findByScheduledAtBetweenAndStatusAndPhoneNumber(
            LocalDateTime start, LocalDateTime end, Status status, String phoneNumber
    );
    List<Routine> findByStatusAndCompletedAtAfterAndPhoneNumberOrderByCompletedAtDesc(
            Status status, LocalDateTime start, String phoneNumber
    );

    boolean existsByIdAndPhoneNumber(Long id, String phoneNumber);
    Optional<Routine> findByIdAndPhoneNumber(Long id, String phoneNumber);

}
