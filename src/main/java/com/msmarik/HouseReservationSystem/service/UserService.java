package com.msmarik.HouseReservationSystem.service;

import com.msmarik.HouseReservationSystem.dto.UserDTO;
import com.msmarik.HouseReservationSystem.exception.UserAlreadyExistsException;
import com.msmarik.HouseReservationSystem.model.entity.User;
import com.msmarik.HouseReservationSystem.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public UserDTO registerUser(UserDTO userDTO) {
        final String username = userDTO.getUsername();
        final Optional<User> existingUser = userRepository.findByName(username);
        if (existingUser.isPresent()) {
            throw new UserAlreadyExistsException(username);
        }

        User newUser = new User();
        newUser.setName(username);
        newUser.setPasswordHash(passwordEncoder.encode(userDTO.getPassword()));
        User savedUser = userRepository.save(newUser);
        return convertToDTO(savedUser);
    }

    public Optional<UserDTO> findByUsername(String username) {
        return userRepository.findByName(username).map(this::convertToDTO);
    }

    private UserDTO convertToDTO(User user) {
        UserDTO userDTO = new UserDTO();
        userDTO.setId(user.getId());
        userDTO.setUsername(user.getName());
        return userDTO;
    }
}
