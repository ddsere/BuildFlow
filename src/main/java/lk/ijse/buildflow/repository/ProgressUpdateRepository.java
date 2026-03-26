package lk.ijse.buildflow.repository;

import lk.ijse.buildflow.entity.ConstructionProject;
import lk.ijse.buildflow.entity.ProgressUpdate;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProgressUpdateRepository extends JpaRepository<ProgressUpdate, Long> {
    List<ProgressUpdate> findByProjectOrderByUpdateTimeDesc(ConstructionProject project);
}
