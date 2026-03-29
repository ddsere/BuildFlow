package lk.ijse.buildflow.controller;

import lk.ijse.buildflow.dto.AuthRequestDTO;
import lk.ijse.buildflow.dto.AuthResponseDTO;
import lk.ijse.buildflow.dto.UserDTO;
import lk.ijse.buildflow.entity.User;
import lk.ijse.buildflow.enums.Role;
import lk.ijse.buildflow.repository.UserRepository;
import lk.ijse.buildflow.security.JwtUtil;
import lk.ijse.buildflow.service.UserService;
import lk.ijse.buildflow.util.APIResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    @Autowired private AuthenticationManager authenticationManager;
    @Autowired private JwtUtil jwtUtil;
    @Autowired private UserService userService;
    @Autowired private UserRepository userRepository;

    @PostMapping("/register")
    public ResponseEntity<APIResponse<UserDTO>> register(@RequestBody UserDTO userDTO) {
        try {
            if (userDTO.getRole() == null || userDTO.getRole().isEmpty()) {
                userDTO.setRole(Role.CLIENT.name());
            }
            UserDTO savedUser = userService.registerUser(userDTO);
            return ResponseEntity.ok(new APIResponse<>(200, "User registered successfully", savedUser));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body(new APIResponse<>(500, "Registration failed: " + e.getMessage(), null));
        }
    }


    @PostMapping("/login")
    public ResponseEntity<APIResponse<AuthResponseDTO>> login(@RequestBody AuthRequestDTO authRequest) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authRequest.getEmail(), authRequest.getPassword())
            );

            User user = userRepository.findByEmail(authRequest.getEmail())
                    .orElseThrow(() -> new RuntimeException("User not found"));

            String roleStr = user.getRole() != null ? user.getRole().name() : "CLIENT";

            AuthResponseDTO response = new AuthResponseDTO(authRequest.getEmail(), "LOGGED_IN", roleStr);
            return ResponseEntity.ok(new APIResponse<>(200, "Login successful", response));

        } catch (Exception e) {
            return ResponseEntity.status(401).body(new APIResponse<>(401, "Invalid email or password", null));
        }
    }
}