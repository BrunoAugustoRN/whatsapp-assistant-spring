package project.whatsassist.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import project.whatsassist.example.demo.model.Reminder;

public interface ReminderRepository extends JpaRepository<Reminder, Long> {
}
