package com.shadowveil.videoplatform.service;

import com.shadowveil.videoplatform.dto.UserDto;
import com.shadowveil.videoplatform.entity.User;
import com.shadowveil.videoplatform.exception.BadRequestException; // Custom exception
import com.shadowveil.videoplatform.exception.ResourceNotFoundException; // Custom exception
import com.shadowveil.videoplatform.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder; // Import PasswordEncoder
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder; // Inject PasswordEncoder

    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder; // Inject PasswordEncoder
    }

    @Transactional(readOnly = true)
    public List<UserDto.Response> getAllUsers() {
        return userRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public UserDto.Response getUserById(Integer id) {
        return userRepository.findById(id)
                .map(this::convertToDto)
                .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + id)); // Use orElseThrow
    }

    @Transactional(readOnly = true)
    public UserDto.Response getUserByUsername(String username) {
        return userRepository.findByUsername(username)
                .map(this::convertToDto)
                .orElseThrow(() -> new EntityNotFoundException("User not found with username: " + username));
    }

    @Transactional(readOnly = true)
    public UserDto.Response getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .map(this::convertToDto)
                .orElseThrow(() -> new EntityNotFoundException("User not found with email: " + email));
    }

    @Transactional
    public User createUser(UserDto.Request userDto) {
        // Check for duplicate username/email *before* creating the user
        if (userRepository.existsByUsername(userDto.username())) {
            throw new BadRequestException("Username already exists."); // Custom exception
        }
        if (userRepository.existsByEmail(userDto.email())) {
            throw new BadRequestException("Email already exists."); // Custom exception
        }

        User user = new User();
        user.setUsername(userDto.username());
        user.setEmail(userDto.email());
        user.setPasswordHash(passwordEncoder.encode(userDto.password())); // Hash the password!
        user.setRole(userDto.role()); // Set the role
        return userRepository.save(user);
    }
    @Transactional
    public User updateUser(Integer id, UserDto.Request userDto) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id " + id));

        // Check for username/email conflicts *only if they are changed*
        if (!user.getUsername().equals(userDto.username()) && userRepository.existsByUsername(userDto.username())) {
            throw new BadRequestException("Username already exists.");
        }
        if (!user.getEmail().equals(userDto.email()) && userRepository.existsByEmail(userDto.email())) {
            throw new BadRequestException("Email already exists.");
        }

        // Update fields (check for nulls to allow partial updates)
        if (userDto.username() != null) {
            user.setUsername(userDto.username());
        }
        if (userDto.email() != null) {
            user.setEmail(userDto.email());
        }
        // Update role.
        if(userDto.role() != null){
            user.setRole(userDto.role());
        }

        // Update password *only if a new password is provided*
        if (userDto.password() != null && !userDto.password().isBlank()) {
            user.setPasswordHash(passwordEncoder.encode(userDto.password()));
        }

        return userRepository.save(user);
    }

    @Transactional
    public void deleteUser(Integer id) {
        User user = userRepository.findById(id)
                .orElseThrow(()-> new EntityNotFoundException("User with ID: "+ id + " does not exists"));
        userRepository.delete(user);
    }

    // Helper method to convert User entity to UserDto.Response
    public UserDto.Response convertToDto(User user) {
        return new UserDto.Response(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getRole(),
                user.getCreatedAt(),
                user.getUpdatedAt()
        );
    }
}