package lk.ijse.buildflow.service.impl;

import lk.ijse.buildflow.controller.PaymentController.PaymentSummaryDTO;
import lk.ijse.buildflow.dto.PaymentDTO;
import lk.ijse.buildflow.entity.ConstructionProject;
import lk.ijse.buildflow.entity.Payment;
import lk.ijse.buildflow.repository.ConstructionProjectRepository;
import lk.ijse.buildflow.repository.PaymentRepository;
import lk.ijse.buildflow.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
public class PaymentServiceImpl implements PaymentService {

    @Autowired
    private PaymentRepository paymentRepository;
    @Autowired
    private ConstructionProjectRepository projectRepository;
    @Autowired
    private JavaMailSender mailSender;

    @Override
    public PaymentDTO addPayment(PaymentDTO paymentDTO) {
        ConstructionProject project = projectRepository.findById(paymentDTO.getProjectId())
                .orElseThrow(() -> new RuntimeException("Project not found with ID: " + paymentDTO.getProjectId()));

        Payment payment = new Payment();
        payment.setProject(project);
        payment.setAmount(paymentDTO.getAmount());
        payment.setTransactionId("TXN-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase());
        payment.setPaymentStatus("COMPLETED");

        Payment saved = paymentRepository.save(payment);

        sendPaymentReceiptEmail(project, saved);

        return toDTO(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PaymentDTO> getPaymentsByProjectId(Long projectId) {
        ConstructionProject project = projectRepository.findById(projectId)
                .orElseThrow(() -> new RuntimeException("Project not found with ID: " + projectId));
        return paymentRepository.findByProject(project)
                .stream().map(this::toDTO).collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public PaymentSummaryDTO getPaymentSummary(Long projectId) {
        ConstructionProject project = projectRepository.findById(projectId)
                .orElseThrow(() -> new RuntimeException("Project not found with ID: " + projectId));

        Double totalPaid = paymentRepository.findByProject(project)
                .stream()
                .filter(p -> "COMPLETED".equals(p.getPaymentStatus()))
                .mapToDouble(Payment::getAmount)
                .sum();

        Double totalCost = project.getFinalAgreedCost();
        return new PaymentSummaryDTO(totalCost, totalPaid);
    }

    private void sendPaymentReceiptEmail(ConstructionProject project, Payment payment) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setSubject("BuildFlow - Payment Receipt (Rs. " + payment.getAmount() + ")");
            message.setText("Dear Customer,\n\n" +
                    "We have received your payment of Rs. " + payment.getAmount() + " for project: " +
                    project.getModelName() + ".\n\n" +
                    "Transaction ID: " + payment.getTransactionId() + "\n" +
                    "Status: COMPLETED\n\n" +
                    "Thank you!\nBuildFlow Team");
            mailSender.send(message);
        } catch (Exception e) {
            System.err.println("Receipt email failed: " + e.getMessage());
        }
    }

    private PaymentDTO toDTO(Payment p) {
        PaymentDTO dto = new PaymentDTO();
        dto.setPaymentId(p.getPaymentId());
        dto.setProjectId(p.getProject() != null ? p.getProject().getProjectId() : null);
        dto.setOrderId(p.getOrder() != null ? p.getOrder().getOrderId() : null);
        dto.setAmount(p.getAmount());
        dto.setTransactionId(p.getTransactionId());
        dto.setPaymentStatus(p.getPaymentStatus());
        dto.setPaymentDate(p.getPaymentDate());
        return dto;
    }
}