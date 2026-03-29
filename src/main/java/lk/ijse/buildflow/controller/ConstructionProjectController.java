package lk.ijse.buildflow.controller;

import lk.ijse.buildflow.dto.ConstructionProjectDTO;
import lk.ijse.buildflow.dto.ProjectRequestDTO;
import lk.ijse.buildflow.service.ConstructionProjectService;
import lk.ijse.buildflow.util.APIResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/projects")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class ConstructionProjectController {

    @Autowired
    private ConstructionProjectService projectService;

    @PostMapping("/start")
    public ResponseEntity<APIResponse<ConstructionProjectDTO>> startProject(@RequestBody ProjectRequestDTO requestDTO) {
        try {
            ConstructionProjectDTO startedProject = projectService.startProject(requestDTO);

            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(new APIResponse<>(201, "Project started successfully!", startedProject));

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new APIResponse<>(400, "Failed to start project: " + e.getMessage(), null));
        }
    }


    @GetMapping("/all")
    public ResponseEntity<APIResponse<List<ConstructionProjectDTO>>> getAllProjects() {
        try {
            List<ConstructionProjectDTO> projects = projectService.getAllOngoingProjects();

            return ResponseEntity.ok(new APIResponse<>(200, "Projects retrieved successfully", projects));

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new APIResponse<>(500, "Error fetching projects: " + e.getMessage(), null));
        }
    }

}