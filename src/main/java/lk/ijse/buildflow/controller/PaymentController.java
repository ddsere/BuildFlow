package lk.ijse.buildflow.controller;

import lk.ijse.buildflow.dto.PaymentDTO;
import lk.ijse.buildflow.service.PaymentService;
import lk.ijse.buildflow.util.APIResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/payments")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class PaymentController {

    @Autowired
    private PaymentService paymentService;

    @PostMapping("/add")
    public ResponseEntity<APIResponse<PaymentDTO>> addPayment(@RequestBody PaymentDTO paymentDTO) {
        try {
            PaymentDTO saved = paymentService.addPayment(paymentDTO);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(new APIResponse<>(201, "Payment recorded successfully!", saved));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new APIResponse<>(400, "Payment failed: " + e.getMessage(), null));
        }
    }

    @GetMapping("/project/{projectId}")
    public ResponseEntity<APIResponse<List<PaymentDTO>>> getPaymentsByProject(@PathVariable Long projectId) {
        try {
            List<PaymentDTO> payments = paymentService.getPaymentsByProjectId(projectId);
            return ResponseEntity.ok(new APIResponse<>(200, "Payments retrieved", payments));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new APIResponse<>(404, e.getMessage(), null));
        }
    }

    @GetMapping("/summary/{projectId}")
    public ResponseEntity<APIResponse<PaymentSummaryDTO>> getPaymentSummary(@PathVariable Long projectId) {
        try {
            PaymentSummaryDTO summary = paymentService.getPaymentSummary(projectId);
            return ResponseEntity.ok(new APIResponse<>(200, "Summary retrieved", summary));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new APIResponse<>(404, e.getMessage(), null));
        }
    }

    public static class PaymentSummaryDTO {
        public Double totalCost;
        public Double totalPaid;
        public Double totalDue;
        public String paymentStatus;

        public PaymentSummaryDTO(Double totalCost, Double totalPaid) {
            this.totalCost = totalCost != null ? totalCost : 0.0;
            this.totalPaid = totalPaid != null ? totalPaid : 0.0;
            this.totalDue = this.totalCost - this.totalPaid;
            this.paymentStatus = this.totalDue <= 0 ? "FULLY_PAID" :
                    this.totalPaid > 0 ? "PARTIAL" : "UNPAID";
        }
    }
}