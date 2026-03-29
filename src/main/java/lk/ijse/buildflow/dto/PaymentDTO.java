package lk.ijse.buildflow.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class PaymentDTO {
    private Long paymentId;

    private Long orderId;
    private Long projectId;

    private Double amount;
    private String transactionId;
    private String paymentStatus;
    private LocalDateTime paymentDate;
}