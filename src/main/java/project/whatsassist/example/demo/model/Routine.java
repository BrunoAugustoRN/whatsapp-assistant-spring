package project.whatsassist.example.demo.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import project.whatsassist.example.demo.enuns.Status;

import java.time.LocalDateTime;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class Routine {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotBlank
    @Column(name = "Descrição")
    private String description;
    private LocalDateTime scheduledAt;
    private Status status;
    private LocalDateTime completedAt;
    private LocalDateTime createdAt;



}
