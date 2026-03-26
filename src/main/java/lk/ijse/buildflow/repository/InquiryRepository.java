package lk.ijse.buildflow.repository;

import lk.ijse.buildflow.entity.Inquiry;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface InquiryRepository extends JpaRepository<Inquiry, Long> {
    List<Inquiry> findAllByOrderBySubmittedAtDesc();
}
