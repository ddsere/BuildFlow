package lk.ijse.buildflow.service.impl;

import jakarta.transaction.Transactional;
import lk.ijse.buildflow.dto.ProjectRequestDTO;
import lk.ijse.buildflow.entity.ConstructionProject;
import lk.ijse.buildflow.entity.HouseModel;
import lk.ijse.buildflow.entity.ProjectRequest;
import lk.ijse.buildflow.entity.User;
import lk.ijse.buildflow.repository.ConstructionProjectRepository;
import lk.ijse.buildflow.repository.HouseModelRepository;
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
    @Autowired private HouseModelRepository houseModelRepository;

    @Override
    public ProjectRequestDTO createRequest(ProjectRequestDTO requestDTO) {

        ProjectRequest request = modelMapper.map(requestDTO, ProjectRequest.class);

        User client = userRepository.findById(requestDTO.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));
        request.setClient(client);

        HouseModel model = houseModelRepository.findById(requestDTO.getModelId())
                .orElseThrow(() -> new RuntimeException("House Model not found"));
        request.setHouseModel(model);

        request.setStatus("PENDING");

        ProjectRequest saved = requestRepository.save(request);

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
    @Transactional
    public void approveRequest(Long requestId, Long contractorId) {

        ProjectRequest request = requestRepository.findById(requestId)
                .orElseThrow(() -> new RuntimeException("Request not found"));
        request.setStatus("APPROVED");
        requestRepository.save(request);

        User contractor = userRepository.findById(contractorId)
                .orElseThrow(() -> new RuntimeException("Contractor not found"));

        ConstructionProject project = new ConstructionProject();
        project.setProjectRequest(request);
        project.setContractor(contractor);
        project.setCurrentStatus("IN_PROGRESS");
        project.setStartDate(LocalDate.now());

        projectRepository.save(project);

        emailService.sendSimpleEmail(request.getClient().getEmail(),
                "Project Approved!", "Congratulations! Your house construction project has been approved and started.");

        emailService.sendSimpleEmail(contractor.getEmail(),
                "New Project Assigned", "You have been assigned as the contractor for a new project: " + request.getHouseModel().getModelName());
    }
}
