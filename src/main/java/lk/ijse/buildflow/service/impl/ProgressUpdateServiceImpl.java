package lk.ijse.buildflow.service.impl;

import lk.ijse.buildflow.dto.ProgressUpdateDTO;
import lk.ijse.buildflow.entity.ConstructionProject;
import lk.ijse.buildflow.entity.ProgressUpdate;
import lk.ijse.buildflow.repository.ConstructionProjectRepository;
import lk.ijse.buildflow.repository.ProgressUpdateRepository;
import lk.ijse.buildflow.service.ProgressUpdateService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProgressUpdateServiceImpl implements ProgressUpdateService {
    @Autowired private ProgressUpdateRepository updateRepository;
    @Autowired private ConstructionProjectRepository projectRepository;
    @Autowired private ModelMapper modelMapper;

    @Override
    public ProgressUpdateDTO addUpdate(ProgressUpdateDTO updateDTO) {
        ProgressUpdate update = modelMapper.map(updateDTO, ProgressUpdate.class);

        ConstructionProject project = projectRepository.findById(updateDTO.getProjectId())
                .orElseThrow(() -> new RuntimeException("Project not found!"));

        update.setProject(project);

        ProgressUpdate saved = updateRepository.save(update);

        return modelMapper.map(saved, ProgressUpdateDTO.class);
    }

    @Override
    public List<ProgressUpdateDTO> getUpdatesByProjectId(Long projectId) {

        ConstructionProject project = new ConstructionProject();
        project.setProjectId(projectId);

        List<ProgressUpdate> updates = updateRepository.findByProjectOrderByUpdateTimeDesc(project);

        return updates.stream()
                .map(update -> modelMapper.map(update, ProgressUpdateDTO.class))
                .collect(Collectors.toList());
    }
}
