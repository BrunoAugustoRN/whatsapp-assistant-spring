package project.whatsassist.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import project.whatsassist.example.demo.model.Reminder;

import java.time.LocalDateTime;
import java.util.List;

public interface ReminderRepository extends JpaRepository<Reminder, Long> {
    List<Reminder> findByFiredFalseAndTriggerAtBefore(LocalDateTime now);
}
