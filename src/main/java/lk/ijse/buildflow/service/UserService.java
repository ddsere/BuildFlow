package lk.ijse.buildflow.service;

import lk.ijse.buildflow.dto.UserDTO;

public interface UserService {
    UserDTO registerUser(UserDTO userDTO);
    UserDTO getUserByUsername(String username);
}
