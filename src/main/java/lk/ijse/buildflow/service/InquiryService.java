package lk.ijse.buildflow.service;

import lk.ijse.buildflow.dto.InquiryDTO;

import java.util.List;

public interface InquiryService {
    void saveInquiry(InquiryDTO inquiryDTO);
    List<InquiryDTO> getAllInquiries();
    void sendReplyEmail(String toEmail, String subject, String message);
}
