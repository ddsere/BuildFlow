package lk.ijse.buildflow.controller;

import lk.ijse.buildflow.dto.UserDTO;
import lk.ijse.buildflow.service.UserService;
import lk.ijse.buildflow.util.APIResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/user")
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public ResponseEntity<APIResponse<UserDTO>> registerUser(@RequestBody UserDTO userDTO) {
        UserDTO savedUser = userService.registerUser(userDTO);
        return ResponseEntity.ok(new APIResponse<>(200, "User registered successfully!", savedUser));
    }

    @GetMapping("/{username}")
    public ResponseEntity<APIResponse<UserDTO>> getUser(@PathVariable String username) {
        UserDTO user = userService.getUserByUsername(username);
        return ResponseEntity.ok(new APIResponse<>(200, "User found", user));
    }
}
