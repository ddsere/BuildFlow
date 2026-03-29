package lk.ijse.buildflow.service.impl;

import lk.ijse.buildflow.dto.UserDTO;
import lk.ijse.buildflow.entity.User;
import lk.ijse.buildflow.enums.Role;
import lk.ijse.buildflow.repository.UserRepository;
import lk.ijse.buildflow.service.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    @Autowired private UserRepository userRepository;
    @Autowired private ModelMapper modelMapper;
    @Autowired private PasswordEncoder passwordEncoder;

    @Override
    public UserDTO registerUser(UserDTO userDTO) {
        User user = modelMapper.map(userDTO, User.class);
        user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        try {
            user.setRole(Role.valueOf(userDTO.getRole().toUpperCase()));
        } catch (Exception e) {
            user.setRole(Role.CLIENT);
        }

        User savedUser = userRepository.save(user);

        UserDTO result = modelMapper.map(savedUser, UserDTO.class);
        result.setRole(savedUser.getRole().name());
        return result;
    }

    @Override
    public UserDTO getUserByUsername(String username) {
        User user = userRepository.findByEmail(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
        UserDTO dto = modelMapper.map(user, UserDTO.class);
        dto.setRole(user.getRole() != null ? user.getRole().name() : Role.CLIENT.name());
        return dto;
    }
}