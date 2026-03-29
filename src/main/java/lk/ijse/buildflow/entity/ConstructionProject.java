package lk.ijse.buildflow.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;

@Entity
@Data
@Table(name = "construction_projects")
public class ConstructionProject {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long projectId;

    @OneToOne
    @JoinColumn(name = "request_id")
    private ProjectRequest projectRequest;

    @ManyToOne
    @JoinColumn(name = "contractor_id")
    private User contractor;

    private String customerName;
    private String modelName;

    private LocalDate startDate;
    private LocalDate estimatedEndDate;
    private Double finalAgreedCost;
    private String currentStatus;

    @Column(columnDefinition = "integer default 0")
    private Integer currentProgress;

    private String customerEmail;
}
