package lk.ijse.buildflow.repository;

import lk.ijse.buildflow.entity.ConstructionProject;
import lk.ijse.buildflow.entity.ProgressUpdate;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProgressUpdateRepository extends JpaRepository<ProgressUpdate, Long> {
    // යම් project එකකට අදාළ සියලුම progress updates බැලීමට (අලුත්ම එක මුලට එන පරිදි)
    List<ProgressUpdate> findByProjectOrderByUpdateTimeDesc(ConstructionProject project);
}
