package lk.ijse.buildflow.service.impl;

import lk.ijse.buildflow.dto.ConstructionProjectDTO;
import lk.ijse.buildflow.dto.ProjectRequestDTO;
import lk.ijse.buildflow.entity.ConstructionProject;
import lk.ijse.buildflow.entity.ProjectRequest;
import lk.ijse.buildflow.entity.User;
import lk.ijse.buildflow.repository.ConstructionProjectRepository;
import lk.ijse.buildflow.service.ConstructionProjectService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ConstructionProjectServiceImpl implements ConstructionProjectService {
    @Autowired private ConstructionProjectRepository projectRepository;
    @Autowired private ModelMapper modelMapper;

    @Override
    public ConstructionProjectDTO startProject(ProjectRequestDTO requestDTO) {
        ConstructionProject project = new ConstructionProject();
        ConstructionProject saved = projectRepository.save(project);
        return modelMapper.map(saved, ConstructionProjectDTO.class);
    }
}
