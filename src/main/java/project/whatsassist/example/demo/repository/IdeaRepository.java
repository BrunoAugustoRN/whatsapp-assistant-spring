package project.whatsassist.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import project.whatsassist.example.demo.model.Idea;

import java.util.List;

public interface IdeaRepository extends JpaRepository <Idea, Long> {
    List<Idea> findByPhoneNumber(String phoneNumber);
    boolean existsByAndPhoneNumber(Long id, String phoneNumber);
}
