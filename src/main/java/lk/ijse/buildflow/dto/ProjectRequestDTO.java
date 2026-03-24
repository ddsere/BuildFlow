package lk.ijse.buildflow.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ProjectRequestDTO {
    private Long requestId;
    private String clientName; // User ID එක වෙනුවට නම පෙන්වීමට
    private String modelName;  // Model ID එක වෙනුවට නම පෙන්වීමට
    private String requestType;
    private String status;
    private LocalDateTime requestDate;
}
