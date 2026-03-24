package lk.ijse.buildflow.repository;

import lk.ijse.buildflow.entity.ConstructionProject;
import lk.ijse.buildflow.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ConstructionProjectRepository extends JpaRepository<ConstructionProject, Long> {
    // Contractor කෙනෙකුට අදාළ project ලැයිස්තුව ගැනීමට
    List<ConstructionProject> findByContractor(User contractor);

    // දැනට ක්‍රියාත්මක වන projects (IN_PROGRESS) බැලීමට
    List<ConstructionProject> findByCurrentStatus(String status);
}
