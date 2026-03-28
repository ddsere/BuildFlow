package lk.ijse.buildflow.controller;

import lk.ijse.buildflow.dto.InquiryDTO;
import lk.ijse.buildflow.entity.Inquiry;
import lk.ijse.buildflow.repository.InquiryRepository;
import lk.ijse.buildflow.util.APIResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/inquiries")
@CrossOrigin
public class InquiryController {
    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private InquiryRepository inquiryRepository;

    @PostMapping("/send")
    public ResponseEntity<APIResponse<String>> receiveInquiry(@RequestBody InquiryDTO inquiryDTO) {

        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom("dahamserasinghe542@gmail.com");
            message.setTo("dahamserasinghe542@gmail.com");
            message.setSubject("New BuildFlow Inquiry: " + inquiryDTO.getModelName());

            String emailContent = "You have received a new inquiry from " + inquiryDTO.getCustomerName() + "\n\n" +
                    "Model: " + inquiryDTO.getModelName() + "\n" +
                    "Phone: " + inquiryDTO.getCustomerPhone() + "\n" +
                    "Email: " + inquiryDTO.getCustomerEmail() + "\n\n" +
                    "Message: " + inquiryDTO.getMessage();

            message.setText(emailContent);
            mailSender.send(message);

            return ResponseEntity.ok(new APIResponse<>(200, "Email sent successfully!", "Success"));

        } catch (Exception e) {
            return ResponseEntity.status(500).body(new APIResponse<>(500, "Error sending email: " + e.getMessage(), null));
        }
    }

    @GetMapping("/all")
    public ResponseEntity<APIResponse<List<Inquiry>>> getAllInquiries() {
        List<Inquiry> inquiries = inquiryRepository.findAllByOrderBySubmittedAtDesc();
        return ResponseEntity.ok(new APIResponse<>(200, "Inquiries retrieved", inquiries));
    }
}
