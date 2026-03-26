package lk.ijse.buildflow.service.impl;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lk.ijse.buildflow.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class EmailServiceImpl implements EmailService {
    @Autowired private JavaMailSender mailSender;

    @Override
    public void sendSimpleEmail(String to, String subject, String body) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(body);
        mailSender.send(message);
    }

    public void sendEmailWithAttachment(String toEmail, String customerName, byte[] pdfContent) {
        try {
            MimeMessage message = mailSender.createMimeMessage();

            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setTo(toEmail);
            helper.setSubject("BuildFlow - House Project Quotation for " + customerName);

            String emailBody = "Dear " + customerName + ",\n\n" +
                    "Thank you for your interest in BuildFlow. Please find the attached PDF quotation " +
                    "for the house model you requested.\n\n" +
                    "If you have any further questions, feel free to contact us.\n\n" +
                    "Best Regards,\n" +
                    "BuildFlow Team";

            helper.setText(emailBody);

            helper.addAttachment("BuildFlow_Quotation.pdf", new ByteArrayResource(pdfContent));

            mailSender.send(message);

        } catch (MessagingException e) {
            throw new RuntimeException("Failed to send email with attachment: " + e.getMessage());
        }
    }
}
