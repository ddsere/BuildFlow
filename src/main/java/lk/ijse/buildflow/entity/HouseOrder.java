package lk.ijse.buildflow.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "house_order")
public class HouseOrder {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long orderId;

    private String modelName;
    private String customerName;
    private String customerEmail;
    private Double amountPaid;

    private String paymentStatus;

    private LocalDateTime orderDate;

    @PrePersist
    protected void onCreate() {
        orderDate = LocalDateTime.now();
    }
}
