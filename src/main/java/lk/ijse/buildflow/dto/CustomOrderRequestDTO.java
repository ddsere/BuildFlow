package lk.ijse.buildflow.dto;

import lombok.Data;

@Data
public class CustomOrderRequestDTO {
    private Long inquiryId;
    private Double finalPrice;
    private String customSpecs;
}
