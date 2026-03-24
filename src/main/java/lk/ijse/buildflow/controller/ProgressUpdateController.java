package lk.ijse.buildflow.controller;

import lk.ijse.buildflow.dto.ProgressUpdateDTO;
import lk.ijse.buildflow.service.ProgressUpdateService;
import lk.ijse.buildflow.util.APIResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/progress")
public class ProgressUpdateController {
    @Autowired
    private ProgressUpdateService updateService;

    @PostMapping("/add")
    public ResponseEntity<APIResponse<ProgressUpdateDTO>> addUpdate(@RequestBody ProgressUpdateDTO updateDTO) {
        ProgressUpdateDTO savedUpdate = updateService.addUpdate(updateDTO);
        return ResponseEntity.ok(new APIResponse<>(201, "Progress updated successfully!", savedUpdate));
    }
}
