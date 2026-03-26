package lk.ijse.buildflow.service;

import lk.ijse.buildflow.dto.ProgressUpdateDTO;

import java.util.List;

public interface ProgressUpdateService {
    ProgressUpdateDTO addUpdate(ProgressUpdateDTO updateDTO);
    List<ProgressUpdateDTO> getUpdatesByProjectId(Long projectId);
}
