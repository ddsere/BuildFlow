package lk.ijse.buildflow.dto;

import lombok.Data;

@Data
public class UserDTO {
    private String userId;
    private String userName;
    private String email;
    private String password;
    private String role;
}
