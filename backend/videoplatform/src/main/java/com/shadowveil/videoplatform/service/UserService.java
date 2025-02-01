package com.shadowveil.videoplatform.service;

import com.shadowveil.videoplatform.entity.User;
import com.shadowveil.videoplatform.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // Retrieve all users.
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    // Retrieve a single user by its ID.
    public Optional<User> getUserById(Integer id) {
        return userRepository.findById(id);
    }

    // Create a new user.
    public User createUser(User user) {
        return userRepository.save(user);
    }

    // Update an existing user.
    public User updateUser(Integer id, User userDetails) {
        return userRepository.findById(id).map(user -> {
            user.setUsername(userDetails.getUsername());
            user.setEmail(userDetails.getEmail());
            user.setPasswordHash(userDetails.getPasswordHash());
            user.setRole(userDetails.getRole());
            user.setUpdatedAt(userDetails.getUpdatedAt());
            return userRepository.save(user);
        }).orElseThrow(() -> new RuntimeException("User not found with id " + id));
    }

    // Delete a user by its ID.
    public void deleteUser(Integer id) {
        userRepository.deleteById(id);
    }

    // Optional: Retrieve a user by username.
    public Optional<User> getUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    // Optional: Retrieve a user by email.
    public Optional<User> getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }
}
