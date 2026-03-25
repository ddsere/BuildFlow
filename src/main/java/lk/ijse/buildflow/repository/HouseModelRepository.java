package lk.ijse.buildflow.repository;

import lk.ijse.buildflow.entity.HouseModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface HouseModelRepository extends JpaRepository<HouseModel, Long> {
    // Budget එකට වඩා අඩු models සෙවීමට
    List<HouseModel> findByEstimatedCostLessThanEqual(Double budget);

    // Bedrooms ගණන අනුව සෙවීමට
    List<HouseModel> findByNumBedrooms(Integer bedrooms);

    // Floor area එක අනුව සෙවීමට
    List<HouseModel> findByFloorAreaGreaterThanEqual(Double area);

    // නම අනුව search කිරීම (Case-insensitive)
    List<HouseModel> findByModelNameContainingIgnoreCase(String name);

    // මිල පරාසයක් (Price Range) අනුව filter කිරීම
    List<HouseModel> findByEstimatedCostBetween(double minPrice, double maxPrice);
}
