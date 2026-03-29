package lk.ijse.buildflow.service;

import lk.ijse.buildflow.dto.CustomOrderRequestDTO;
import lk.ijse.buildflow.dto.OrderDTO;

import java.util.List;

public interface OrderService {
    String createCustomOrder(CustomOrderRequestDTO requestDTO);
    String processStandardPurchase(OrderDTO orderDTO);
    List<OrderDTO> getAllOrders();
}
