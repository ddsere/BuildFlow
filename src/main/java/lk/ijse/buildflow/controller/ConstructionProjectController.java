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

    /**
     * අලුත් Construction Project එකක් ආරම්භ කිරීම
     * Endpoint: POST /api/v1/projects/start
     */
    @PostMapping("/start")
    public ResponseEntity<APIResponse<ConstructionProjectDTO>> startProject(@RequestBody ProjectRequestDTO requestDTO) {
        try {
            // Service එක හරහා Project එක Save කර DTO එකක් ලෙස ලබා ගැනීම
            ConstructionProjectDTO startedProject = projectService.startProject(requestDTO);

            // 201 CREATED - අලුත් Record එකක් හැදුනු බව පෙන්වීමට
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(new APIResponse<>(201, "Project started successfully!", startedProject));

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new APIResponse<>(400, "Failed to start project: " + e.getMessage(), null));
        }
    }

    /**
     * දැනට ක්‍රියාත්මක වන (Ongoing) සියලුම Projects ලබා ගැනීම (Admin Panel එකේ Table එකට)
     * Endpoint: GET /api/v1/projects/all
     */
    @GetMapping("/all")
    public ResponseEntity<APIResponse<List<ConstructionProjectDTO>>> getAllProjects() {
        try {
            // Service එකෙන් Projects List එක අරගැනීම
            List<ConstructionProjectDTO> projects = projectService.getAllOngoingProjects();

            // 200 OK - සාර්ථකව දත්ත ලබා දුන් බව පෙන්වීමට
            return ResponseEntity.ok(new APIResponse<>(200, "Projects retrieved successfully", projects));

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new APIResponse<>(500, "Error fetching projects: " + e.getMessage(), null));
        }
    }

    // අවශ්‍ය නම් අනාගතයේදී Project එකක Status එක වෙනස් කරන Endpoints මෙතැනට එකතු කළ හැක.
}