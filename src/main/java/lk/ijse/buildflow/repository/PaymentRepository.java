package lk.ijse.buildflow.repository;

import lk.ijse.buildflow.entity.ConstructionProject;
import lk.ijse.buildflow.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
    List<Payment> findByProject(ConstructionProject project);

    Payment findByTransactionId(String transactionId);
}
