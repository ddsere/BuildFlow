package lk.ijse.buildflow.service.impl;

import lk.ijse.buildflow.dto.ConstructionProjectDTO;
import lk.ijse.buildflow.dto.ProjectRequestDTO;
import lk.ijse.buildflow.entity.ConstructionProject;
import lk.ijse.buildflow.repository.ConstructionProjectRepository;
import lk.ijse.buildflow.service.ConstructionProjectService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class ConstructionProjectServiceImpl implements ConstructionProjectService {

    @Autowired
    private ConstructionProjectRepository projectRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public ConstructionProjectDTO startProject(ProjectRequestDTO requestDTO) {
        ConstructionProject project = modelMapper.map(requestDTO, ConstructionProject.class);

        project.setStartDate(LocalDate.now());
        project.setCurrentStatus("STARTED");
        project.setCurrentProgress(0);

        ConstructionProject savedProject = projectRepository.save(project);

        return modelMapper.map(savedProject, ConstructionProjectDTO.class);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ConstructionProjectDTO> getAllOngoingProjects() {
        List<ConstructionProject> projects = projectRepository.findAll();
        return projects.stream()
                .map(project -> modelMapper.map(project, ConstructionProjectDTO.class))
                .collect(Collectors.toList());
    }
}