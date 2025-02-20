package com.shadowveil.videoplatform.service;

import com.shadowveil.videoplatform.dto.UserDto;
import com.shadowveil.videoplatform.entity.User;
import com.shadowveil.videoplatform.exception.BadRequestException;
import com.shadowveil.videoplatform.exception.ResourceNotFoundException;
import com.shadowveil.videoplatform.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional(readOnly = true)
    public List<UserDto.Response> getAllUsers() {
        return userRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Optional<UserDto.Response> getUserById(Integer id) {
        return userRepository.findById(id)
                .map(this::convertToDto);
    }

    @Transactional(readOnly = true)
    public Optional<UserDto.Response> getUserByUsername(String username) {
        return userRepository.findByUsername(username)
                .map(this::convertToDto);
    }

    @Transactional(readOnly = true)
    public Optional<UserDto.Response> getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .map(this::convertToDto);
    }

    @Transactional
    public User createUser(UserDto.Request userDto) {
        if (userRepository.existsByUsername(userDto.username())) {
            throw new BadRequestException("Username already exists.");
        }
        if (userRepository.existsByEmail(userDto.email())) {
            throw new BadRequestException("Email already exists.");
        }

        User user = new User();
        user.setUsername(userDto.username());
        user.setEmail(userDto.email());
        user.setPasswordHash(passwordEncoder.encode(userDto.password()));
        user.setRole(userDto.role());
        return userRepository.save(user);
    }

    @Transactional
    public User updateUser(Integer id, UserDto.Request userDto) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id " + id));

        if (!user.getUsername().equals(userDto.username()) && userRepository.existsByUsername(userDto.username())) {
            throw new BadRequestException("Username already exists.");
        }
        if (!user.getEmail().equals(userDto.email()) && userRepository.existsByEmail(userDto.email())) {
            throw new BadRequestException("Email already exists.");
        }

        if (userDto.username() != null) user.setUsername(userDto.username());
        if (userDto.email() != null) user.setEmail(userDto.email());
        if (userDto.role() != null) user.setRole(userDto.role());

        if (userDto.password() != null && !userDto.password().isBlank()) {
            user.setPasswordHash(passwordEncoder.encode(userDto.password()));
        }

        return userRepository.save(user);
    }

    @Transactional
    public void deleteUser(Integer id) {
        if (!userRepository.existsById(id)) {
            throw new ResourceNotFoundException("User with ID: " + id + " does not exist");
        }
        userRepository.deleteById(id);
    }

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