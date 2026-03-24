package lk.ijse.buildflow.service;

import lk.ijse.buildflow.dto.ConstructionProjectDTO;
import lk.ijse.buildflow.dto.ProjectRequestDTO;

public interface ConstructionProjectService {
    ConstructionProjectDTO startProject(ProjectRequestDTO requestDTO);
}
