package lk.ijse.buildflow.service;

import lk.ijse.buildflow.dto.ProjectRequestDTO;

import java.util.List;

public interface ProjectRequestService {
    ProjectRequestDTO createRequest(ProjectRequestDTO requestDTO);

    List<ProjectRequestDTO> getAllRequests();

    void approveRequest(Long requestId, Long contractorId);
}
