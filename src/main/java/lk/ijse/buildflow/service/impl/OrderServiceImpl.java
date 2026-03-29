package lk.ijse.buildflow.service.impl;

import lk.ijse.buildflow.dto.CustomOrderRequestDTO;
import lk.ijse.buildflow.dto.OrderDTO;
import lk.ijse.buildflow.entity.ConstructionProject;
import lk.ijse.buildflow.entity.HouseOrder;
import lk.ijse.buildflow.entity.Inquiry;
import lk.ijse.buildflow.repository.ConstructionProjectRepository;
import lk.ijse.buildflow.repository.InquiryRepository;
import lk.ijse.buildflow.repository.OrderRepository;
import lk.ijse.buildflow.service.OrderService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class OrderServiceImpl implements OrderService {

    @Autowired private InquiryRepository inquiryRepository;
    @Autowired private OrderRepository orderRepository;
    @Autowired private ModelMapper modelMapper;
    @Autowired private JavaMailSender mailSender;
    @Autowired private ConstructionProjectRepository projectRepository;

    @Override
    public String processStandardPurchase(OrderDTO orderDTO) {
        HouseOrder order = new HouseOrder();
        order.setModelName(orderDTO.getModelName());
        order.setCustomerName(orderDTO.getCustomerName());
        order.setCustomerEmail(orderDTO.getCustomerEmail());
        order.setAmountPaid(0.0);
        order.setTotalPrice(0.0);
        order.setPaymentStatus("PENDING");
        orderRepository.save(order);
        sendNewOrderNotification(order);

        return "Order request submitted. Our team will contact you shortly.";
    }

    @Override
    public String createCustomOrder(CustomOrderRequestDTO requestDTO) {
        Inquiry inquiry = inquiryRepository.findById(requestDTO.getInquiryId())
                .orElseThrow(() -> new RuntimeException("Inquiry not found"));

        inquiry.setStatus("APPROVED");
        inquiryRepository.save(inquiry);

        HouseOrder order = modelMapper.map(inquiry, HouseOrder.class);
        order.setInquiryId(inquiry.getId());
        order.setTotalPrice(requestDTO.getFinalPrice());
        order.setPaymentStatus("UNPAID");
        orderRepository.save(order);

        ConstructionProject project = new ConstructionProject();
        project.setCustomerName(inquiry.getCustomerName());
        project.setCustomerEmail(inquiry.getCustomerEmail());
        project.setModelName(inquiry.getModelName());
        project.setFinalAgreedCost(requestDTO.getFinalPrice());
        project.setCurrentStatus("STARTED");
        project.setCurrentProgress(0);
        project.setStartDate(java.time.LocalDate.now());

        projectRepository.save(project);

        return "Project Started Successfully!";
    }

    private void sendNewOrderNotification(HouseOrder order) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo("admin@buildflow.lk");
            message.setSubject("BuildFlow - New Order Request: " + order.getModelName());
            message.setText("A new order request has been received.\n\n" +
                    "Customer: " + order.getCustomerName() + "\n" +
                    "Email: " + order.getCustomerEmail() + "\n" +
                    "Model: " + order.getModelName() + "\n\n" +
                    "Please log in to the admin dashboard to review and contact the client.");
            mailSender.send(message);
        } catch (Exception e) {
            System.err.println("Admin notification email failed: " + e.getMessage());
        }
    }

    private void sendApprovalEmail(Inquiry inquiry, Double price, String specs) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(inquiry.getCustomerEmail());
        message.setSubject("BuildFlow - Custom Request Approved! (" + inquiry.getModelName() + ")");
        message.setText("Hi " + inquiry.getCustomerName() + ",\n\n" +
                "Your custom request for the " + inquiry.getModelName() + " has been approved.\n\n" +
                "Approved Specifications: " + specs + "\n" +
                "Final Agreed Cost: Rs. " + price + "\n\n" +
                "Please log in to your BuildFlow dashboard to make your first payment.\n\n" +
                "Best Regards,\nBuildFlow Team");
        mailSender.send(message);
    }
    @Override
    public List<OrderDTO> getAllOrders() {
        return orderRepository.findAll().stream()
                .map(order -> modelMapper.map(order, OrderDTO.class))
                .collect(Collectors.toList());
    }
}