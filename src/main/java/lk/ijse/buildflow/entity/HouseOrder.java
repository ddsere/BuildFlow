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

    private Double amountPaid; // ගෙවූ මුදල (Advance)
    private Double totalPrice; // සම්පූර්ණ එකඟ වූ මුදල (අලුතින් එකතු කළා)

    private String paymentStatus;

    private LocalDateTime orderDate;

    private Long inquiryId; // අදාළ Inquiry එකේ ID එක
    private String customSpecs; // ඇඩ්මින් අතින් දාන විස්තර (උදා: extra bathroom)

    @PrePersist
    protected void onCreate() {
        orderDate = LocalDateTime.now();
        if (amountPaid == null) {
            amountPaid = 0.0; // Order එක හැදෙද්දී ගෙවපු ගාණ 0යි
        }
    }
}