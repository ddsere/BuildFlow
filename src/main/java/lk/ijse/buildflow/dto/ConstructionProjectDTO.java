package lk.ijse.buildflow.dto;

import lombok.Data;
import java.time.LocalDate;

@Data
public class ConstructionProjectDTO {
    private Long projectId;
    private String modelName;
    private String customerName;
    private String customerEmail;
    private String contractorName;
    private LocalDate startDate;
    private String currentStatus;
    private Double finalAgreedCost;
    private Integer currentProgress;
}