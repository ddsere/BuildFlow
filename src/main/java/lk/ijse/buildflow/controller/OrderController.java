package lk.ijse.buildflow.controller;

import lk.ijse.buildflow.dto.CustomOrderRequestDTO;
import lk.ijse.buildflow.dto.OrderDTO;
import lk.ijse.buildflow.service.OrderService;
import lk.ijse.buildflow.util.APIResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/orders")
@CrossOrigin(origins = "*", allowedHeaders = "*") // Frontend එකට අවසර දීම
public class OrderController {

    @Autowired
    private OrderService orderService; // Service එක පමණක් Inject කරන්න

    // 1. Standard Marketplace Purchase (Advance Payment)
    @PostMapping("/purchase")
    public ResponseEntity<APIResponse<String>> purchasePlan(@RequestBody OrderDTO orderDTO) {
        try {
            // Logic ඔක්කොම Service එකෙන් කරලා Result එක දෙනවා
            String result = orderService.processStandardPurchase(orderDTO);

            return ResponseEntity.ok(new APIResponse<>(200, result, "Success"));

        } catch (Exception e) {
            e.printStackTrace(); // Console එකේ Error එක බලාගන්න
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new APIResponse<>(500, "Payment Failed: " + e.getMessage(), null));
        }
    }

    // 2. Admin Custom Order Creation (Inquiry Approval)
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