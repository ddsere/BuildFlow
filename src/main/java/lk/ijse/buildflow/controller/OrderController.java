package lk.ijse.buildflow.controller;

import lk.ijse.buildflow.dto.CustomOrderRequestDTO;
import lk.ijse.buildflow.dto.OrderDTO;
import lk.ijse.buildflow.service.OrderService;
import lk.ijse.buildflow.util.APIResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/orders")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @PostMapping("/purchase")
    public ResponseEntity<APIResponse<String>> purchasePlan(@RequestBody OrderDTO orderDTO) {
        try {
            String result = orderService.processStandardPurchase(orderDTO);

            return ResponseEntity.ok(new APIResponse<>(200, result, "Success"));

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new APIResponse<>(500, "Payment Failed: " + e.getMessage(), null));
        }
    }

    @GetMapping("/all")
    public ResponseEntity<APIResponse<List<OrderDTO>>> getAllOrders() {
        try {
            List<OrderDTO> allOrders = orderService.getAllOrders();
            return ResponseEntity.ok(new APIResponse<>(200, "Success", allOrders));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new APIResponse<>(500, "Error: " + e.getMessage(), null));
        }
    }

    @PostMapping("/create-custom")
    public ResponseEntity<APIResponse<String>> createCustomOrder(@RequestBody CustomOrderRequestDTO requestDTO) {
        try {
            String result = orderService.createCustomOrder(requestDTO);

            return ResponseEntity.ok(new APIResponse<>(200, result, "Success"));

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new APIResponse<>(500, "Failed to create custom order: " + e.getMessage(), null));
        }
    }
}