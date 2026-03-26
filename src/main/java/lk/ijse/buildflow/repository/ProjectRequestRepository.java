package lk.ijse.buildflow.repository;

import lk.ijse.buildflow.entity.ProjectRequest;
import lk.ijse.buildflow.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProjectRequestRepository extends JpaRepository<ProjectRequest, Long> {
    List<ProjectRequest> findByClient(User client);

    List<ProjectRequest> findByStatus(String status);
}
