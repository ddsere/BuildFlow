package lk.ijse.buildflow.repository;

import lk.ijse.buildflow.entity.HouseModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface HouseModelRepository extends JpaRepository<HouseModel, Long> {
    List<HouseModel> findByEstimatedCostLessThanEqual(Double budget);

    List<HouseModel> findByNumBedrooms(Integer bedrooms);

    List<HouseModel> findByFloorAreaGreaterThanEqual(Double area);

    List<HouseModel> findByModelNameContainingIgnoreCase(String name);

    List<HouseModel> findByEstimatedCostBetween(double minPrice, double maxPrice);
}
