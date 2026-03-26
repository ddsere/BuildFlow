package lk.ijse.buildflow.controller;

import lk.ijse.buildflow.dto.ProjectRequestDTO;
import lk.ijse.buildflow.entity.HouseModel;
import lk.ijse.buildflow.entity.User;
import lk.ijse.buildflow.repository.HouseModelRepository;
import lk.ijse.buildflow.repository.UserRepository;
import lk.ijse.buildflow.service.ProjectRequestService;
import lk.ijse.buildflow.service.impl.QuotationService;
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

    @Autowired
    private QuotationService quotationService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private HouseModelRepository houseModelRepository;

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

    @PostMapping("/request-quotation")
    public ResponseEntity<APIResponse<String>> sendQuotation(@RequestBody ProjectRequestDTO dto) {
        try {
            User user = userRepository.findById(dto.getUserId())
                    .orElseThrow(() -> new RuntimeException("User not found"));

            HouseModel model = houseModelRepository.findById(dto.getModelId())
                    .orElseThrow(() -> new RuntimeException("House Model not found"));

            quotationService.handleQuotationRequest(
                    user.getEmail(),
                    user.getUserName(),
                    model.getModelName(),
                    model.getEstimatedCost()
            );

            return ResponseEntity.ok(new APIResponse<>(200, "Quotation PDF has been sent to your email successfully!", "Success"));

        } catch (Exception e) {
            return ResponseEntity.status(500).body(new APIResponse<>(500, "Error: " + e.getMessage(), null));
        }
    }
}
