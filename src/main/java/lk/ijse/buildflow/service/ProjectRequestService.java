package lk.ijse.buildflow.service;

import lk.ijse.buildflow.dto.ProjectRequestDTO;

import java.util.List;

public interface ProjectRequestService {
    // Client කෙනෙක් අලුත් Request එකක් (Quotation/Edit Plan) දැමීම
    ProjectRequestDTO createRequest(ProjectRequestDTO requestDTO);

    // පද්ධතියේ ඇති සියලුම Requests බැලීමට (Admin සඳහා)
    List<ProjectRequestDTO> getAllRequests();

    // Request එකක් Approve කර Project එකක් ආරම්භ කිරීම
    void approveRequest(Long requestId, Long contractorId);
}
