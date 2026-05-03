package project.whatsassist.example.demo.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Reminder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "Descrição")
    private String description;
    private LocalDateTime triggerAt;

    @Column(name = "Numero_telefone")
    private String phoneNumber;
    private boolean fired = false;
    private LocalDateTime createdAt;
}
