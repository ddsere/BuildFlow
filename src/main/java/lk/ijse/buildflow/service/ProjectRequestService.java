package lk.ijse.buildflow.service;

import lk.ijse.buildflow.entity.ProjectRequest;
import lk.ijse.buildflow.repository.ProjectRequestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProjectRequestService {
    @Autowired
    private ProjectRequestRepository requestRepository;

    @Autowired
    private EmailService emailService;

    public ProjectRequest createRequest(ProjectRequest request) {
        ProjectRequest saved = requestRepository.save(request);
        // Automated Email Notification
        emailService.sendSimpleEmail(saved.getClient().getEmail(),
                "Request Received", "Your request for " + request.getRequestType() + " is pending.");
        return saved;
    }
}
