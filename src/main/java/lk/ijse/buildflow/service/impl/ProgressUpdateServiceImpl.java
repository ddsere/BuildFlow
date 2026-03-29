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
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class ProgressUpdateServiceImpl implements ProgressUpdateService {

    @Autowired
    private ProgressUpdateRepository updateRepository;

    @Autowired
    private ConstructionProjectRepository projectRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public ProgressUpdateDTO addUpdate(ProgressUpdateDTO updateDTO) {
        ConstructionProject project = projectRepository.findById(updateDTO.getProjectId())
                .orElseThrow(() -> new RuntimeException("Project not found!"));

        ProgressUpdate update = new ProgressUpdate();
        update.setProject(project);
        update.setDescription(updateDTO.getDescription());
        update.setPercentageComplete(updateDTO.getPercentageComplete());
        update.setPhotoUrl(updateDTO.getPhotoUrl());
        update.setUpdateTime(LocalDateTime.now());
        ProgressUpdate saved = updateRepository.save(update);
        project.setCurrentProgress(updateDTO.getPercentageComplete());
        projectRepository.save(project);

        return modelMapper.map(saved, ProgressUpdateDTO.class);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProgressUpdateDTO> getUpdatesByProjectId(Long projectId) {
        ConstructionProject project = projectRepository.findById(projectId)
                .orElseThrow(() -> new RuntimeException("Project not found with ID: " + projectId));

        List<ProgressUpdate> updates = updateRepository.findByProjectOrderByUpdateTimeDesc(project);

        return updates.stream()
                .map(update -> modelMapper.map(update, ProgressUpdateDTO.class))
                .collect(Collectors.toList());
    }
}