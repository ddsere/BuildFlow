package lk.ijse.buildflow.controller;

import lk.ijse.buildflow.dto.ProjectRequestDTO;
import lk.ijse.buildflow.service.ProjectRequestService;
import lk.ijse.buildflow.util.APIResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/requests")
public class ProjectRequestController {
    @Autowired
    private ProjectRequestService requestService;

    @PostMapping("/create")
    public ResponseEntity<APIResponse<ProjectRequestDTO>> createRequest(@RequestBody ProjectRequestDTO requestDTO) {
        ProjectRequestDTO savedRequest = requestService.createRequest(requestDTO);
        return ResponseEntity.ok(new APIResponse<>(201, "Project request submitted successfully! An email has been sent.", savedRequest));
    }

    @GetMapping
    public ResponseEntity<APIResponse<List<ProjectRequestDTO>>> getAllRequests() {
        List<ProjectRequestDTO> requests = requestService.getAllRequests();
        return ResponseEntity.ok(new APIResponse<>(200, "Requests fetched", requests));
    }

    @PutMapping("/{requestId}/approve")
    public ResponseEntity<APIResponse<String>> approveRequest(
            @PathVariable Long requestId,
            @RequestParam Long contractorId) {

        requestService.approveRequest(requestId, contractorId);
        return ResponseEntity.ok(new APIResponse<>(200, "Request Approved! Construction Project started and emails sent.", null));
    }
}
