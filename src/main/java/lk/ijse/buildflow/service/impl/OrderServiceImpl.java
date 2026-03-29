package lk.ijse.buildflow.service.impl;

import lk.ijse.buildflow.dto.CustomOrderRequestDTO;
import lk.ijse.buildflow.dto.OrderDTO;
import lk.ijse.buildflow.entity.ConstructionProject;
import lk.ijse.buildflow.entity.HouseOrder;
import lk.ijse.buildflow.entity.Inquiry;
import lk.ijse.buildflow.entity.Payment;
import lk.ijse.buildflow.repository.ConstructionProjectRepository;
import lk.ijse.buildflow.repository.InquiryRepository;
import lk.ijse.buildflow.repository.OrderRepository;
import lk.ijse.buildflow.repository.PaymentRepository;
import lk.ijse.buildflow.service.OrderService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.UUID;

@Service
@Transactional
public class OrderServiceImpl implements OrderService {

    @Autowired
    private InquiryRepository inquiryRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private ConstructionProjectRepository projectRepository;

    // --- 1. Custom Order Logic (කලින් අපි හැදූ කොටස) ---
    @Override
    public String createCustomOrder(CustomOrderRequestDTO requestDTO) {
        Inquiry inquiry = inquiryRepository.findById(requestDTO.getInquiryId())
                .orElseThrow(() -> new RuntimeException("Inquiry not found with ID: " + requestDTO.getInquiryId()));

        inquiry.setStatus("APPROVED");
        inquiryRepository.save(inquiry);

        HouseOrder order = modelMapper.map(inquiry, HouseOrder.class);
        order.setInquiryId(inquiry.getId());
        order.setCustomSpecs(requestDTO.getCustomSpecs());
        order.setTotalPrice(requestDTO.getFinalPrice());
        order.setAmountPaid(0.0);
        order.setPaymentStatus("UNPAID");

        orderRepository.save(order);
        sendApprovalEmail(inquiry, requestDTO.getFinalPrice(), requestDTO.getCustomSpecs());

        return "Custom Order Created Successfully!";
    }

    // --- 2. Standard Purchase Logic (ඔයා අලුතින් අහපු කොටස) ---
    @Override
    @Transactional
    public String processStandardPurchase(OrderDTO orderDTO) {

        // 1. Order එක ModelMapper හරහා Entity එකට හරවා Save කිරීම
        HouseOrder order = modelMapper.map(orderDTO, HouseOrder.class);
        order.setPaymentStatus("COMPLETED");
        HouseOrder savedOrder = orderRepository.save(order);

        // 2. Payment එක අතින් (Manual) හදා Save කිරීම (මෙහි Relationship සහ අහඹු ID ඇති බැවින් ModelMapper අනවශ්‍යයි)
        Payment payment = new Payment();
        payment.setOrder(savedOrder);
        payment.setAmount(orderDTO.getAmountPaid()); // ඔයාගේ DTO එකේ නම
        payment.setTransactionId("TXN-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase());
        payment.setPaymentStatus("COMPLETED");
        paymentRepository.save(payment);

        // 💡 3. Construction Project එක ModelMapper හරහා මැප් කර Save කිරීම
        // මෙහිදී DTO එකේ ඇති customerName සහ modelName ඉබේම Project Entity එකට Set වේ.
        ConstructionProject project = modelMapper.map(orderDTO, ConstructionProject.class);
        project.setStartDate(LocalDate.now()); // අද දිනය
        project.setCurrentStatus("STARTED");   // මුලික තත්වය
        project.setCurrentProgress(0);         // ආරම්භයේදී ප්‍රගතිය 0%

        projectRepository.save(project); // Project Table එකට Data යැවීම

        // 4. Customer ට Payment Receipt Email එකක් යැවීම (ඔයාගේ කලින් තිබ්බ Method එක)
        sendPaymentReceiptEmail(savedOrder);

        return "Purchase, Payment & Project Created Successfully!";
    }

    // --- Emails යවන ක්‍රියාවලීන් ---

    private void sendApprovalEmail(Inquiry inquiry, Double price, String specs) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(inquiry.getCustomerEmail());
        message.setSubject("BuildFlow - Custom Request Approved! (" + inquiry.getModelName() + ")");
        message.setText("Hi " + inquiry.getCustomerName() + ",\n\n" +
                "Great news! Your custom request for the " + inquiry.getModelName() + " has been reviewed and approved by our team.\n\n" +
                "Approved Specifications: " + specs + "\n" +
                "Final Estimated Cost: Rs. " + price + "\n\n" +
                "Please log in to your BuildFlow dashboard to complete the advance payment and begin the project.\n\n" +
                "Best Regards,\nBuildFlow Team");
        mailSender.send(message);
    }

    private void sendPaymentReceiptEmail(HouseOrder order) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(order.getCustomerEmail());
        message.setSubject("BuildFlow - Payment Receipt (" + order.getModelName() + ")");
        message.setText("Hi " + order.getCustomerName() + ",\n\n" +
                "Thank you for your payment! We have successfully received your advance payment of Rs. " + order.getAmountPaid() +
                " for the " + order.getModelName() + " blueprint plan.\n\n" +
                "Our team will review your order and get back to you shortly with the next steps for your construction project.\n\n" +
                "Best Regards,\nBuildFlow Team");
        mailSender.send(message);
    }
}