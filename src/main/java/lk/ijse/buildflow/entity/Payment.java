package lk.ijse.buildflow.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "payments")
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long paymentId;

    @ManyToOne
    @JoinColumn(name = "project_id")
    private ConstructionProject project;

    private Double amount;
    private String transactionId;
    private String paymentStatus;
    private LocalDateTime paymentDate = LocalDateTime.now();
}
