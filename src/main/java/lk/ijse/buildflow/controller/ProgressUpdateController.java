package lk.ijse.buildflow.controller;

import lk.ijse.buildflow.dto.ProgressUpdateDTO;
import lk.ijse.buildflow.service.ProgressUpdateService;
import lk.ijse.buildflow.util.APIResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/progress")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class ProgressUpdateController {

    @Autowired
    private ProgressUpdateService updateService;

    @PostMapping("/add")
    public ResponseEntity<APIResponse<ProgressUpdateDTO>> addUpdate(@RequestBody ProgressUpdateDTO updateDTO) {
        try {
            ProgressUpdateDTO savedUpdate = updateService.addUpdate(updateDTO);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(new APIResponse<>(201, "Progress updated successfully!", savedUpdate));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new APIResponse<>(400, "Failed to add progress: " + e.getMessage(), null));
        }
    }

    @GetMapping("/project/{projectId}")
    public ResponseEntity<APIResponse<List<ProgressUpdateDTO>>> getProjectProgress(@PathVariable Long projectId) {
        try {
            List<ProgressUpdateDTO> updates = updateService.getUpdatesByProjectId(projectId);
            return ResponseEntity.ok(new APIResponse<>(200, "Progress updates retrieved successfully", updates));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new APIResponse<>(404, "Error finding project: " + e.getMessage(), null));
        }
    }
}