package lk.ijse.buildflow.controller;

import lk.ijse.buildflow.dto.OrderDTO;
import lk.ijse.buildflow.entity.HouseOrder;
import lk.ijse.buildflow.repository.OrderRepository;
import lk.ijse.buildflow.util.APIResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/orders")
@CrossOrigin
public class OrderController {
    @Autowired
    private OrderRepository orderRepository;

    @PostMapping("/purchase")
    public ResponseEntity<APIResponse<String>> purchasePlan(@RequestBody OrderDTO orderDTO) {
        try {
            HouseOrder order = new HouseOrder();
            order.setModelName(orderDTO.getModelName());
            order.setCustomerName(orderDTO.getCustomerName());
            order.setCustomerEmail(orderDTO.getCustomerEmail());
            order.setAmountPaid(orderDTO.getAmountPaid());
            order.setPaymentStatus("COMPLETED");

            orderRepository.save(order);


            return ResponseEntity.ok(new APIResponse<>(200, "Purchase Successful!", "Success"));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(new APIResponse<>(500, "Payment Failed: " + e.getMessage(), null));
        }
    }
}
