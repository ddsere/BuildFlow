package lk.ijse.buildflow.service.impl;

import lk.ijse.buildflow.dto.CustomOrderRequestDTO;
import lk.ijse.buildflow.dto.OrderDTO;
import lk.ijse.buildflow.entity.HouseOrder;
import lk.ijse.buildflow.entity.Inquiry;
import lk.ijse.buildflow.repository.InquiryRepository;
import lk.ijse.buildflow.repository.OrderRepository;
import lk.ijse.buildflow.service.OrderService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    public String processStandardPurchase(OrderDTO orderDTO) {
        // 1. DTO එක Entity එකට හැරවීම (ModelMapper මගින්)
        HouseOrder order = modelMapper.map(orderDTO, HouseOrder.class);

        // 2. අදාළ Payment Status එක Set කිරීම
        order.setPaymentStatus("COMPLETED");

        // 3. Database එකේ Save කිරීම
        orderRepository.save(order);

        // 4. (Optional) Customer ට Payment Receipt Email එකක් යැවීම
        sendPaymentReceiptEmail(order);

        return "Purchase Successful!";
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