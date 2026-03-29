package lk.ijse.buildflow.controller;

import lk.ijse.buildflow.dto.InquiryDTO;
import lk.ijse.buildflow.service.InquiryService;
import lk.ijse.buildflow.util.APIResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/inquiries")
@CrossOrigin
public class InquiryController {

    @Autowired
    private InquiryService inquiryService;

    @PostMapping("/send")
    public ResponseEntity<APIResponse<String>> receiveInquiry(@RequestBody InquiryDTO inquiryDTO) {
        try {
            // Service එකට වැඩේ බාර දෙනවා
            inquiryService.saveInquiry(inquiryDTO);

            return ResponseEntity.ok(new APIResponse<>(200, "Inquiry saved and Email sent successfully!", "Success"));

        } catch (Exception e) {
            e.printStackTrace(); // Console එකේ Error එක බලාගන්න
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new APIResponse<>(500, "Failed to process inquiry: " + e.getMessage(), null));
        }
    }

    @GetMapping("/all")
    public ResponseEntity<APIResponse<List<InquiryDTO>>> getAllInquiries() {
        try {
            List<InquiryDTO> inquiries = inquiryService.getAllInquiries();
            return ResponseEntity.ok(new APIResponse<>(200, "Inquiries retrieved successfully", inquiries));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new APIResponse<>(500, "Error fetching inquiries: " + e.getMessage(), null));
        }
    }

    @PostMapping("/reply")
    public ResponseEntity<APIResponse<String>> replyToInquiry(@RequestBody java.util.Map<String, String> payload) {
        try {
            // Map එකෙන් අදාළ දත්ත ටික අරගෙන Service එකට යවනවා
            String email = payload.get("customerEmail");
            String subject = payload.get("subject");
            String message = payload.get("message");

            inquiryService.sendReplyEmail(email, subject, message);

            return ResponseEntity.ok(new APIResponse<>(200, "Reply sent successfully!", "Success"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new APIResponse<>(500, "Failed to send reply: " + e.getMessage(), null));
        }
    }
}