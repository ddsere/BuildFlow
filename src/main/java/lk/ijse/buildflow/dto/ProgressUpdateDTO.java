package lk.ijse.buildflow.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ProgressUpdateDTO {
    private Long projectId;
    private Integer percentageComplete;
    private String description;
    private String photoUrl;
    private LocalDateTime updateTime;
}
