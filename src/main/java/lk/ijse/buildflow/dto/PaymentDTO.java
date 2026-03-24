package lk.ijse.buildflow.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class PaymentDTO {
    private Long paymentId;
    private Double amount;
    private String transactionId;
    private String status;
    private LocalDateTime paymentDate;
}
