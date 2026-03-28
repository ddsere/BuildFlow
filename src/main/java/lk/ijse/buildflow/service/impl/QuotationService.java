package lk.ijse.buildflow.service.impl;

import lk.ijse.buildflow.service.DocumentGenerator;
import lk.ijse.buildflow.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class QuotationService {

    private final DocumentGenerator documentGenerator;
    private final EmailService emailService;

    @Autowired
    public QuotationService(@Qualifier("openPdfGenerator") DocumentGenerator documentGenerator,
                            EmailService emailService) {
        this.documentGenerator = documentGenerator;
        this.emailService = emailService;
    }

    public void handleQuotationRequest(String customerEmail, String customerName, String modelName, Double price, Integer bedrooms, Double area) {
        try {

            byte[] pdfContent = documentGenerator.generateQuotationPdf(customerName, modelName, price, bedrooms, area);

            emailService.sendEmailWithAttachment(customerEmail, customerName, pdfContent);

            System.out.println("Quotation sent successfully to: " + customerEmail);
        } catch (Exception e) {
            throw new RuntimeException("Failed to process quotation: " + e.getMessage());
        }
    }

    public byte[] getQuotationPdfBytes(String customerName, String modelName, Double price, Integer bedrooms, Double area) {
        // මේකෙන් කෙලින්ම PDF byte array එක Return කරනවා
        try {
            return documentGenerator.generateQuotationPdf(customerName, modelName, price, bedrooms, area);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
