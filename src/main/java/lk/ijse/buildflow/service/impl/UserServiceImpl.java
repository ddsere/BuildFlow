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

    @Override
    public UserDTO registerUser(UserDTO userDTO) {
        User user = modelMapper.map(userDTO, User.class);
        User savedUser = userRepository.save(user);
        return modelMapper.map(savedUser, UserDTO.class);
    }

    @Override
    public UserDTO getUserByUsername(String username) {
        User user = userRepository.findByUserName(username).orElseThrow();
        return modelMapper.map(user, UserDTO.class);
    }
}
