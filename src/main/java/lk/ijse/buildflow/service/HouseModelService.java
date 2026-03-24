package lk.ijse.buildflow.service;

import lk.ijse.buildflow.dto.HouseModelDTO;

import java.util.List;

public interface HouseModelService {
    // Marketplace එකේ සියලුම models ලබා ගැනීමට
    List<HouseModelDTO> getAllModels();

    // Budget, Bedrooms සහ Area අනුව filter කිරීමට (Advanced Search)
    List<HouseModelDTO> searchModels(Double maxPrice, Integer minBedrooms, Double minArea);

    // අලුත් Model එකක් ඇතුළත් කිරීමට (Admin සඳහා)
    HouseModelDTO saveModel(HouseModelDTO houseModelDTO);
}
