package project.whatsassist.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import project.whatsassist.example.demo.model.Routine;

public interface RoutineRepository extends JpaRepository<Routine, Long> {
}
