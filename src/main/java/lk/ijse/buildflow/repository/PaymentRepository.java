package lk.ijse.buildflow.repository;

import lk.ijse.buildflow.entity.ConstructionProject;
import lk.ijse.buildflow.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
    // යම් project එකක් සඳහා කර ඇති සියලුම ගෙවීම් බැලීමට
    List<Payment> findByProject(ConstructionProject project);

    // Transaction ID එක මගින් සෙවීමට
    Payment findByTransactionId(String transactionId);
}
