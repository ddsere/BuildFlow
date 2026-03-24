package lk.ijse.buildflow.service;

import lk.ijse.buildflow.dto.UserDTO;
import lk.ijse.buildflow.entity.User;

import java.util.List;
import java.util.Optional;

public interface UserService {
    UserDTO registerUser(UserDTO userDTO);
    UserDTO getUserByUsername(String username);
}
