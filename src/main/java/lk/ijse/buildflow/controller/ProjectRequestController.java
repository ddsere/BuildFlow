package lk.ijse.buildflow.controller;

import lk.ijse.buildflow.dto.HouseModelDTO;
import lk.ijse.buildflow.dto.ProjectRequestDTO;
import lk.ijse.buildflow.repository.HouseModelRepository;
import lk.ijse.buildflow.repository.UserRepository;
import lk.ijse.buildflow.service.HouseModelService;
import lk.ijse.buildflow.service.ProjectRequestService;
import lk.ijse.buildflow.service.impl.QuotationService;
import lk.ijse.buildflow.util.APIResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/requests")
@CrossOrigin(origins = "http://127.0.0.1:5500", allowedHeaders = "*")
public class ProjectRequestController {
    @Autowired
    private ProjectRequestService requestService;

    @Autowired
    private HouseModelService modelService;

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

    @GetMapping("/download-quotation/{modelId}")
    public ResponseEntity<byte[]> downloadQuotation(@PathVariable Long modelId) {
        try {
            HouseModelDTO model = modelService.getHouseModelById(modelId);

            byte[] pdfContent = quotationService.getQuotationPdfBytes(
                    "Valued Customer",
                    model.getModelName(),
                    model.getEstimatedCost(),
                    model.getNumBedrooms(),
                    model.getFloorArea()
            );

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDispositionFormData("attachment", "Quotation_" + model.getModelName() + ".pdf");

            return new ResponseEntity<>(pdfContent, headers, HttpStatus.OK);

        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
