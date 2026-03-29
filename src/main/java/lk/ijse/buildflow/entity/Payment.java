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

    // 💡 අලුතින් එකතු කළ කොටස: Plan එකක් ගන්නකොට හැදෙන Order එකට අදාළ ගෙවීම
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private HouseOrder order;

    // දැනට තියෙන කොටස: ගෙයක් හදද්දී (Ongoing Project) කරන ගෙවීම්
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id")
    private ConstructionProject project;

    private Double amount;
    private String transactionId;
    private String paymentStatus;

    @Column(updatable = false)
    private LocalDateTime paymentDate;

    // 💡 Database එකට Save වෙන්න කලින් මේක Run වෙලා වෙලාව ඉබේම සටහන් වෙනවා
    @PrePersist
    protected void onCreate() {
        this.paymentDate = LocalDateTime.now();
    }
}