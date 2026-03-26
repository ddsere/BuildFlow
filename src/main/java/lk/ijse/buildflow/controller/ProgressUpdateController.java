package lk.ijse.buildflow.controller;

import lk.ijse.buildflow.dto.ProgressUpdateDTO;
import lk.ijse.buildflow.service.ProgressUpdateService;
import lk.ijse.buildflow.util.APIResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/progress")
@CrossOrigin
public class ProgressUpdateController {
    @Autowired
    private ProgressUpdateService updateService;

    @PostMapping("/add")
    public ResponseEntity<APIResponse<ProgressUpdateDTO>> addUpdate(@RequestBody ProgressUpdateDTO updateDTO) {
        ProgressUpdateDTO savedUpdate = updateService.addUpdate(updateDTO);
        return ResponseEntity.ok(new APIResponse<>(201, "Progress updated successfully!", savedUpdate));
    }

    @GetMapping("/project/{projectId}")
    public ResponseEntity<APIResponse<List<ProgressUpdateDTO>>> getProjectProgress(@PathVariable Long projectId) {
        List<ProgressUpdateDTO> updates = updateService.getUpdatesByProjectId(projectId);
        return ResponseEntity.ok(new APIResponse<>(200, "Progress updates retrieved", updates));
    }
}
