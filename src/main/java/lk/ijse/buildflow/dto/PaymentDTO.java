package lk.ijse.buildflow.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class PaymentDTO {
    private Long paymentId;

    private Long orderId;   // Order එකක් නම් මේකේ අගයක් තියෙයි
    private Long projectId; // Project එකක් නම් මේකේ අගයක් තියෙයි

    private Double amount;
    private String transactionId;
    private String paymentStatus; // ඔයාගේ Entity එකේ status නෙමෙයි paymentStatus තිබ්බේ. ඒ නිසා ඒකම පාවිච්චි කරමු.
    private LocalDateTime paymentDate;
}