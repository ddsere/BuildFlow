package lk.ijse.buildflow.controller;

import lk.ijse.buildflow.dto.HouseModelDTO;
import lk.ijse.buildflow.service.HouseModelService;
import lk.ijse.buildflow.util.APIResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/models")
@CrossOrigin
public class HouseModelController {
    @Autowired
    private HouseModelService houseModelService;

    @GetMapping
    public ResponseEntity<APIResponse<List<HouseModelDTO>>> getAllModels() {
        List<HouseModelDTO> models = houseModelService.getAllModels();
        return ResponseEntity.ok(new APIResponse<>(200, "Models retrieved successfully", models));
    }

    @GetMapping("/search")
    public ResponseEntity<APIResponse<List<HouseModelDTO>>> searchModels(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) Double maxPrice,
            @RequestParam(required = false) Integer minBedrooms,
            @RequestParam(required = false) Double minArea) {

        List<HouseModelDTO> results = houseModelService.searchModels(name, maxPrice, minBedrooms, minArea);
        return ResponseEntity.ok(new APIResponse<>(200, "Search completed", results));
    }

    @PostMapping("/add")
    public ResponseEntity<APIResponse<HouseModelDTO>> addModel(@RequestBody HouseModelDTO houseModelDTO) {
        HouseModelDTO savedModel = houseModelService.saveModel(houseModelDTO);
        return ResponseEntity.ok(new APIResponse<>(201, "New 3D Model added successfully!", savedModel));
    }
}
