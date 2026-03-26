package lk.ijse.buildflow.service;

public interface EmailService {
    void sendSimpleEmail(String to, String subject, String body);
    void sendEmailWithAttachment(String toEmail, String customerName, byte[] pdfContent);
}