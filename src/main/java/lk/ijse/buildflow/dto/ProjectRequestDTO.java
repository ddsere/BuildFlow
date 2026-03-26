package lk.ijse.buildflow.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ProjectRequestDTO {
    private Long requestId;
    private Long userId;
    private Long modelId;
    private String requestType;
    private String status;
    private LocalDateTime requestDate;

    private String clientName;
    private String modelName;
}
