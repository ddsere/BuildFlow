package lk.ijse.buildflow.dto;

import lombok.Data;

@Data
public class OrderDTO {
    private String modelName;
    private String customerName;
    private String customerEmail;
    private Double amountPaid;
}
