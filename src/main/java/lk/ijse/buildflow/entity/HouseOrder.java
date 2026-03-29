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
    private Double totalPrice;

    private String paymentStatus;

    private LocalDateTime orderDate;

    private Long inquiryId;
    private String customSpecs;

    @PrePersist
    protected void onCreate() {
        orderDate = LocalDateTime.now();
        if (amountPaid == null) {
            amountPaid = 0.0;
        }
    }
}