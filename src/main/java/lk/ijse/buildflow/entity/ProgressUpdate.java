package lk.ijse.buildflow.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "progress_updates")
public class ProgressUpdate {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long updateId;

    @ManyToOne
    @JoinColumn(name = "project_id")
    private ConstructionProject project;

    private Integer percentageComplete;
    private String description;
    private String photoUrl;
    private LocalDateTime updateTime = LocalDateTime.now();
}
