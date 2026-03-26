package lk.ijse.buildflow.repository;

import lk.ijse.buildflow.entity.ConstructionProject;
import lk.ijse.buildflow.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ConstructionProjectRepository extends JpaRepository<ConstructionProject, Long> {
    List<ConstructionProject> findByContractor(User contractor);
    List<ConstructionProject> findByCurrentStatus(String status);
}
