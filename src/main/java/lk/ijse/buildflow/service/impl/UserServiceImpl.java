package lk.ijse.buildflow.service.impl;

import lk.ijse.buildflow.dto.UserDTO;
import lk.ijse.buildflow.entity.User;
import lk.ijse.buildflow.repository.UserRepository;
import lk.ijse.buildflow.service.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {
    @Autowired private UserRepository userRepository;
    @Autowired private ModelMapper modelMapper;

    // SecurityConfig එකේ අපි හදපු Bean එක මෙතනට ගන්නවා
    @Autowired private org.springframework.security.crypto.password.PasswordEncoder passwordEncoder;

    @Override
    public UserDTO registerUser(UserDTO userDTO) {
        // 1. DTO එක Entity එකට හරවනවා
        User user = modelMapper.map(userDTO, User.class);

        // 2. Password එක Encrypt කරලා ආපහු set කරනවා
        user.setPassword(passwordEncoder.encode(userDTO.getPassword()));

        // 3. දැන් Encrypt වුණු password එකත් එක්ක Save කරනවා
        User savedUser = userRepository.save(user);

        return modelMapper.map(savedUser, UserDTO.class);
    }

    @Override
    public UserDTO getUserByUsername(String username) {
        // මෙතන ඔයාගේ Repository එකේ findByEmail ද findByUserName ද තියෙන්නේ කියලා බලන්න
        User user = userRepository.findByEmail(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return modelMapper.map(user, UserDTO.class);
    }
}