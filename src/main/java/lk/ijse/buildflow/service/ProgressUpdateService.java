package lk.ijse.buildflow.service;

import lk.ijse.buildflow.entity.ConstructionProject;
import lk.ijse.buildflow.entity.ProgressUpdate;
import lk.ijse.buildflow.repository.ProgressUpdateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProgressUpdateService {
    @Autowired
    private ProgressUpdateRepository updateRepository;

    public ProgressUpdate addUpdate(ConstructionProject project, Integer percentage, String desc) {
        ProgressUpdate update = new ProgressUpdate();
        update.setProject(project);
        update.setPercentageComplete(percentage);
        update.setDescription(desc);
        return updateRepository.save(update);
    }
}
