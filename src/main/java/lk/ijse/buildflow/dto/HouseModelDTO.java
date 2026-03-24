package lk.ijse.buildflow.dto;

import lombok.Data;

@Data
public class HouseModelDTO {
    private Long modelId;
    private String modelName;
    private String description;
    private Double estimatedCost;
    private Double floorArea;
    private Integer numBedrooms;
    private String modelUrl;
}
