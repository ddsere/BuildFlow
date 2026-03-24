package lk.ijse.buildflow.service;

public interface EmailService {
    void sendSimpleEmail(String to, String subject, String body);
}
