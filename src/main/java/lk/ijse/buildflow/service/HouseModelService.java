package lk.ijse.buildflow.service;

import lk.ijse.buildflow.dto.HouseModelDTO;

import java.util.List;

public interface HouseModelService {
    List<HouseModelDTO> getAllModels();

    List<HouseModelDTO> searchModels(String name, Double maxPrice, Integer minBedrooms, Double minArea);

    HouseModelDTO saveModel(HouseModelDTO houseModelDTO);
}
