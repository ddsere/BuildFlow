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
@Transactional // Database එකට දත්ත ලියන නිසා මෙය අනිවාර්ය වේ
public class ProgressUpdateServiceImpl implements ProgressUpdateService {

    @Autowired
    private ProgressUpdateRepository updateRepository;

    @Autowired
    private ConstructionProjectRepository projectRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public ProgressUpdateDTO addUpdate(ProgressUpdateDTO updateDTO) {
        // 1. අදාළ Project එක තිබේදැයි පරීක්ෂා කිරීම
        ConstructionProject project = projectRepository.findById(updateDTO.getProjectId())
                .orElseThrow(() -> new RuntimeException("Project not found with ID: " + updateDTO.getProjectId()));

        // 2. DTO -> Entity හරවා සම්බන්ධතාවය (Relationship) සෑදීම
        ProgressUpdate update = modelMapper.map(updateDTO, ProgressUpdate.class);
        update.setProject(project);
        update.setUpdateTime(LocalDateTime.now()); // Update වන වේලාව ස්වයංක්‍රීයව සටහන් කිරීම

        // 3. Database එකෙහි Save කිරීම
        ProgressUpdate savedUpdate = updateRepository.save(update);

        /* 💡 මතක තබාගන්න:
           ඔයාගේ ConstructionProject entity එකේ currentProgress වගේ field එකක් තියෙනවා නම්,
           මෙතනදී ඒකත් අලුත් ප්‍රතිශතයෙන් (Percentage) Update කරලා Save කරන්න.
           project.setCurrentProgress(updateDTO.getPercentageComplete());
           projectRepository.save(project);
        */

        return modelMapper.map(savedUpdate, ProgressUpdateDTO.class);
    }

    @Override
    @Transactional(readOnly = true) // මෙය දත්ත කියවීමට පමණක් ඇති බැවින් readOnly යොදයි
    public List<ProgressUpdateDTO> getUpdatesByProjectId(Long projectId) {
        // Project එක ඇත්තටම තියෙනවද බලන එක වඩාත් ආරක්ෂිතයි
        ConstructionProject project = projectRepository.findById(projectId)
                .orElseThrow(() -> new RuntimeException("Project not found with ID: " + projectId));

        List<ProgressUpdate> updates = updateRepository.findByProjectOrderByUpdateTimeDesc(project);

        return updates.stream()
                .map(update -> modelMapper.map(update, ProgressUpdateDTO.class))
                .collect(Collectors.toList());
    }
}