package lk.ijse.buildflow.controller;

import lk.ijse.buildflow.dto.AuthRequestDTO;
import lk.ijse.buildflow.dto.AuthResponseDTO;
import lk.ijse.buildflow.dto.UserDTO;
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
@CrossOrigin // Frontend එකට connect වෙන්න මේක ඕනේ
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserService userService;

    // 1. Register Endpoint (අලුත් User කෙනෙක්ව ඇතුළත් කිරීම)
    @PostMapping("/register")
    public ResponseEntity<APIResponse<UserDTO>> register(@RequestBody UserDTO userDTO) {
        try {
            UserDTO savedUser = userService.registerUser(userDTO);
            return ResponseEntity.ok(new APIResponse<>(200, "User registered successfully", savedUser));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(new APIResponse<>(500, e.getMessage(), null));
        }
    }

    // 2. Login Endpoint (Token එක ලබා ගැනීම)
    @PostMapping("/login")
    public ResponseEntity<APIResponse<AuthResponseDTO>> login(@RequestBody AuthRequestDTO authRequest) {
        try {
            // Spring Security හරහා Email සහ Password හරිද බලනවා
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authRequest.getEmail(), authRequest.getPassword())
            );

            // දත්ත හරි නම් Token එක හදනවා
            String token = jwtUtil.generateToken(authRequest.getEmail());

            AuthResponseDTO response = new AuthResponseDTO(authRequest.getEmail(), token);
            return ResponseEntity.ok(new APIResponse<>(200, "Login successful", response));

        } catch (Exception e) {
            return ResponseEntity.status(401).body(new APIResponse<>(401, "Invalid email or password", null));
        }
    }
}