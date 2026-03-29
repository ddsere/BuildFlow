package lk.ijse.buildflow.service.impl;

import lk.ijse.buildflow.dto.InquiryDTO;
import lk.ijse.buildflow.entity.Inquiry;
import lk.ijse.buildflow.repository.InquiryRepository;
import lk.ijse.buildflow.service.InquiryService;
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
public class InquiryServiceImpl implements InquiryService {

    @Autowired
    private InquiryRepository inquiryRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private JavaMailSender mailSender;

    @Override
    public void saveInquiry(InquiryDTO inquiryDTO) {
        Inquiry inquiry = modelMapper.map(inquiryDTO, Inquiry.class);
        inquiryRepository.save(inquiry);
        sendConfirmationEmailToCustomer(inquiryDTO.getCustomerEmail(), inquiryDTO.getCustomerName(), inquiryDTO.getModelName());
    }

    @Override
    public List<InquiryDTO> getAllInquiries() {
        List<Inquiry> inquiries = inquiryRepository.findAll();

        return inquiries.stream()
                .map(inquiry -> modelMapper.map(inquiry, InquiryDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public void sendReplyEmail(String toEmail, String subject, String messageText) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(toEmail);
        message.setSubject(subject);
        message.setText(messageText);

        mailSender.send(message);
    }

    private void sendConfirmationEmailToCustomer(String toEmail, String customerName, String modelName) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(toEmail);
        message.setSubject("BuildFlow - Request Received (" + modelName + ")");
        message.setText("Hi " + customerName + ",\n\n" +
                "Thank you for your inquiry regarding the " + modelName + " model.\n" +
                "We have successfully received your request, and our team will review your message and get back to you shortly.\n\n" +
                "Best Regards,\nBuildFlow Team");

        mailSender.send(message);
    }
}