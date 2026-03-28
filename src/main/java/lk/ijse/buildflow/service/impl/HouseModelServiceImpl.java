package lk.ijse.buildflow.service.impl;

import lk.ijse.buildflow.dto.HouseModelDTO;
import lk.ijse.buildflow.entity.HouseModel;
import lk.ijse.buildflow.repository.HouseModelRepository;
import lk.ijse.buildflow.service.HouseModelService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class HouseModelServiceImpl implements HouseModelService {
    @Autowired private HouseModelRepository modelRepository;
    @Autowired private ModelMapper modelMapper;

    @Override
    public List<HouseModelDTO> getAllModels() {
        return modelRepository.findAll().stream()
                .map(m -> modelMapper.map(m, HouseModelDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public List<HouseModelDTO> searchModels(String name, Double maxPrice, Integer minBedrooms, Double minArea) {

        return modelRepository.findAll().stream()
                .filter(m -> (name == null || name.isBlank() ||
                        m.getModelName().toLowerCase().contains(name.toLowerCase())))
                .filter(m -> (maxPrice == null || m.getEstimatedCost() <= maxPrice))
                .filter(m -> (minBedrooms == null || m.getNumBedrooms() >= minBedrooms))
                .filter(m -> (minArea == null || m.getFloorArea() >= minArea))
                .map(m -> modelMapper.map(m, HouseModelDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public HouseModelDTO saveModel(HouseModelDTO houseModelDTO) {
        HouseModel model = modelMapper.map(houseModelDTO, HouseModel.class);
        HouseModel savedModel = modelRepository.save(model);
        return modelMapper.map(savedModel, HouseModelDTO.class);
    }

    @Override
    public HouseModelDTO getHouseModelById(Long modelId) {
        // Database එකෙන් Entity එක හොයනවා
        HouseModel houseModel = modelRepository.findById(modelId)
                .orElseThrow(() -> new RuntimeException("House Model not found with ID: " + modelId));

        // Entity එක DTO එකකට හරවනවා (Manual mapping)
        HouseModelDTO dto = new HouseModelDTO();
        dto.setModelId(houseModel.getModelId());
        dto.setModelName(houseModel.getModelName());
        dto.setDescription(houseModel.getDescription());
        dto.setEstimatedCost(houseModel.getEstimatedCost());
        dto.setFloorArea(houseModel.getFloorArea());
        dto.setNumBedrooms(houseModel.getNumBedrooms());

        return dto;
    }
}
