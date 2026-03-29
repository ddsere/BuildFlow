package lk.ijse.buildflow.service;

import lk.ijse.buildflow.dto.CustomOrderRequestDTO;
import lk.ijse.buildflow.dto.OrderDTO;

public interface OrderService {
    String createCustomOrder(CustomOrderRequestDTO requestDTO);
    String processStandardPurchase(OrderDTO orderDTO);
}
