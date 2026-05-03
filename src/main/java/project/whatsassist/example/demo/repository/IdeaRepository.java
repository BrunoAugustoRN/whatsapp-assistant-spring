package project.whatsassist.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import project.whatsassist.example.demo.model.Idea;

public interface IdeaRepository extends JpaRepository <Idea, Long> {
}
