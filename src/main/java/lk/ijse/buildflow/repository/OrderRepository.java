package lk.ijse.buildflow.repository;

import lk.ijse.buildflow.entity.HouseOrder;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<HouseOrder, Long> {
}
