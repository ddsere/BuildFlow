package lk.ijse.buildflow.service;

import lk.ijse.buildflow.dto.ConstructionProjectDTO;
import lk.ijse.buildflow.dto.ProjectRequestDTO;

import java.util.List;

public interface ConstructionProjectService {
    ConstructionProjectDTO startProject(ProjectRequestDTO requestDTO);
    List<ConstructionProjectDTO> getAllOngoingProjects();

}