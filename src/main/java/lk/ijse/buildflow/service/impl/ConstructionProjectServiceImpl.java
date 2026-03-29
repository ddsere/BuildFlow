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
@Transactional // පන්තියේ ඇති සියලුම Database ගනුදෙනු ආරක්ෂිතව සිදු කිරීමට
public class ConstructionProjectServiceImpl implements ConstructionProjectService {

    @Autowired
    private ConstructionProjectRepository projectRepository;

    @Autowired
    private ModelMapper modelMapper;

    /**
     * අලුත් Project එකක් ආරම්භ කිරීම
     */
    @Override
    public ConstructionProjectDTO startProject(ProjectRequestDTO requestDTO) {
        // 1. Request එකෙන් එන දත්ත Project Entity එකට Map කරගැනීම
        ConstructionProject project = modelMapper.map(requestDTO, ConstructionProject.class);

        // 2. අලුත් Project එකක් පටන් ගනිද්දී තිබිය යුතු මූලික තත්වයන් Set කිරීම
        project.setStartDate(LocalDate.now()); // ආරම්භ කළ දිනය අද ලෙස සටහන් කිරීම
        project.setCurrentStatus("STARTED");   // මූලික තත්වය (Status)
        project.setCurrentProgress(0);         // ආරම්භයේදී ප්‍රගතිය (Progress) 0% කි

        // 3. Database එකෙහි Save කිරීම
        ConstructionProject savedProject = projectRepository.save(project);

        // 4. Save වූ දත්ත නැවත DTO එකකට හරවා Controller එකට යැවීම
        return modelMapper.map(savedProject, ConstructionProjectDTO.class);
    }

    /**
     * දැනට ක්‍රියාත්මක වන සියලුම Projects ලබා ගැනීම
     */
    @Override
    @Transactional(readOnly = true) // දත්ත කියවීමට පමණක් ඇති බැවින් Performance වැඩි කිරීමට මෙය යොදයි
    public List<ConstructionProjectDTO> getAllOngoingProjects() {
        // 1. Database එකෙන් සියලුම Projects ලබා ගැනීම
        List<ConstructionProject> projects = projectRepository.findAll();

        // 2. Entity List එක DTO List එකක් බවට හරවා යැවීම (Java Streams භාවිතයෙන්)
        return projects.stream()
                .map(project -> modelMapper.map(project, ConstructionProjectDTO.class))
                .collect(Collectors.toList());
    }
}