package lk.ijse.buildflow.dto;

import lombok.Data;

@Data
public class InquiryDTO {
    private Long id;
    private String modelName;
    private String customerName;
    private String customerEmail;
    private String customerPhone;
    private String message;
    private String status;
}