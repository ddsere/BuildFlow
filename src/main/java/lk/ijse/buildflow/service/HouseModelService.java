package lk.ijse.buildflow.service;

import lk.ijse.buildflow.entity.HouseModel;
import lk.ijse.buildflow.repository.HouseModelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HouseModelService {
    @Autowired
    private HouseModelRepository modelRepository;

    public List<HouseModel> getAllModels() { return modelRepository.findAll(); }

    public List<HouseModel> filterModels(Double budget, Integer bedrooms) {
        if (budget != null) return modelRepository.findByEstimatedCostLessThanEqual(budget);
        if (bedrooms != null) return modelRepository.findByNumBedrooms(bedrooms);
        return modelRepository.findAll();
    }
}
