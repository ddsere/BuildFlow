package lk.ijse.buildflow.service.impl;

import jakarta.transaction.Transactional;
import lk.ijse.buildflow.dto.ProjectRequestDTO;
import lk.ijse.buildflow.entity.ConstructionProject;
import lk.ijse.buildflow.entity.ProjectRequest;
import lk.ijse.buildflow.entity.User;
import lk.ijse.buildflow.repository.ConstructionProjectRepository;
import lk.ijse.buildflow.repository.ProjectRequestRepository;
import lk.ijse.buildflow.repository.UserRepository;
import lk.ijse.buildflow.service.EmailService;
import lk.ijse.buildflow.service.ProjectRequestService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProjectRequestServiceImpl implements ProjectRequestService {
    @Autowired private ProjectRequestRepository requestRepository;
    @Autowired private ConstructionProjectRepository projectRepository;
    @Autowired private UserRepository userRepository;
    @Autowired private EmailService emailService;
    @Autowired private ModelMapper modelMapper;

    @Override
    public ProjectRequestDTO createRequest(ProjectRequestDTO requestDTO) {
        ProjectRequest request = modelMapper.map(requestDTO, ProjectRequest.class);
        ProjectRequest saved = requestRepository.save(request);

        // Automated Email notification
        emailService.sendSimpleEmail(saved.getClient().getEmail(),
                "BuildFlow - Request Received",
                "We have received your " + saved.getRequestType() + " request for the model: " + saved.getHouseModel().getModelName());

        return modelMapper.map(saved, ProjectRequestDTO.class);
    }

    @Override
    public List<ProjectRequestDTO> getAllRequests() {
        return requestRepository.findAll().stream()
                .map(r -> modelMapper.map(r, ProjectRequestDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional // මෙහිදී Tables දෙකක වැඩ සිදුවන බැවින් මෙය අනිවාර්ය වේ
    public void approveRequest(Long requestId, Long contractorId) {
        // 1. Request එක සොයාගෙන එහි Status එක Update කිරීම
        ProjectRequest request = requestRepository.findById(requestId)
                .orElseThrow(() -> new RuntimeException("Request not found"));
        request.setStatus("APPROVED");
        requestRepository.save(request);

        // 2. Contractor කෙනෙකුව සොයාගෙන නව Project එකක් නිර්මාණය කිරීම
        User contractor = userRepository.findById(contractorId)
                .orElseThrow(() -> new RuntimeException("Contractor not found"));

        ConstructionProject project = new ConstructionProject();
        project.setProjectRequest(request);
        project.setContractor(contractor);
        project.setCurrentStatus("IN_PROGRESS");
        project.setStartDate(LocalDate.now());

        projectRepository.save(project);

        // 3. දෙපාර්ශවයටම Email යැවීම
        emailService.sendSimpleEmail(request.getClient().getEmail(),
                "Project Approved!", "Congratulations! Your house construction project has been approved and started.");

        emailService.sendSimpleEmail(contractor.getEmail(),
                "New Project Assigned", "You have been assigned as the contractor for a new project: " + request.getHouseModel().getModelName());
    }
}
