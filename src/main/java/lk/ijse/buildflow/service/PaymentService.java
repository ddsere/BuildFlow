package lk.ijse.buildflow.service;

import lk.ijse.buildflow.controller.PaymentController.PaymentSummaryDTO;
import lk.ijse.buildflow.dto.PaymentDTO;

import java.util.List;

public interface PaymentService {
    PaymentDTO addPayment(PaymentDTO paymentDTO);
    List<PaymentDTO> getPaymentsByProjectId(Long projectId);
    PaymentSummaryDTO getPaymentSummary(Long projectId);
}