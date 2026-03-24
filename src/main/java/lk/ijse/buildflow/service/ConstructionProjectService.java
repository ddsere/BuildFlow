package lk.ijse.buildflow.service;

import lk.ijse.buildflow.entity.ConstructionProject;
import lk.ijse.buildflow.entity.ProjectRequest;
import lk.ijse.buildflow.entity.User;
import lk.ijse.buildflow.repository.ConstructionProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ConstructionProjectService {
    @Autowired
    private ConstructionProjectRepository projectRepository;

    public ConstructionProject startProject(ProjectRequest request, User contractor) {
        ConstructionProject project = new ConstructionProject();
        project.setProjectRequest(request);
        project.setContractor(contractor);
        project.setCurrentStatus("IN_PROGRESS");
        return projectRepository.save(project);
    }
}
