package project.whatsassist.example.demo.scheduler;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import project.whatsassist.example.demo.model.Reminder;
import project.whatsassist.example.demo.repository.ReminderRepository;
import project.whatsassist.example.demo.services.NotifierService;

import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
public class ReminderScheduler {

    private final ReminderRepository reminderRepo;
    private final NotifierService notifier;

    @Scheduled(fixedDelay = 60000)
    public void checkReminders() {
        List<Reminder> pending = reminderRepo
                .findByFiredFalseAndTriggerAtBefore(LocalDateTime.now());

        for (Reminder reminder : pending) {
            notifier.send(
                    reminder.getPhoneNumber(),
                    "Lembrete: " + reminder.getDescription()
            );
            reminder.setFired(true);
            reminderRepo.save(reminder);
        }
    }
}