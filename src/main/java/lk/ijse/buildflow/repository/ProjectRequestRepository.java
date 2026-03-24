package lk.ijse.buildflow.repository;

import lk.ijse.buildflow.entity.ProjectRequest;
import lk.ijse.buildflow.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProjectRequestRepository extends JpaRepository<ProjectRequest, Long> {
    // එක් එක් client ට අදාළ requests බැලීමට
    List<ProjectRequest> findByClient(User client);

    // Status එක අනුව (Pending/Approved) filter කිරීමට
    List<ProjectRequest> findByStatus(String status);
}
