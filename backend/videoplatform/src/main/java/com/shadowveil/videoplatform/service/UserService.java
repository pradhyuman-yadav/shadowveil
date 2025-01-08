package com.shadowveil.videoplatform.service;

import com.shadowveil.videoplatform.entity.User;
import com.shadowveil.videoplatform.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Service class for User entity
 */
@Service
public class UserService {

    private final UserRepository userRepository;

    // Constructor Injection
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Retrieve all users
     *
     * @return a list of users
     */
    public List<User> findAllUsers() {
        return userRepository.findAll();
    }

    /**
     * Retrieve a user by ID
     *
     * @param id the ID of the user
     * @return the User if found
     * @throws RuntimeException if no user is found
     */
    public User findUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));
    }

    /**
     * Retrieve a user by email
     *
     * @param email the email of the user
     * @return an Optional containing the User if found
     */
    public Optional<User> findUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    /**
     * Create a new user
     *
     * @param user the User to create
     * @return the saved User
     */
    public User createUser(User user) {
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new RuntimeException("User already exists with email: " + user.getEmail());
        }
        return userRepository.save(user);
    }

    /**
     * Update an existing user
     *
     * @param id   the ID of the user to update
     * @param user the updated user data
     * @return the updated User
     */
    public User updateUser(Long id, User user) {
        User existingUser = findUserById(id);
        existingUser.setFullName(user.getFullName());
        existingUser.setEmail(user.getEmail());
        existingUser.setRole(user.getRole());
        return userRepository.save(existingUser);
    }

    /**
     * Delete a user by ID
     *
     * @param id the ID of the user to delete
     */
    public void deleteUser(Long id) {
        User user = findUserById(id);
        userRepository.delete(user);
    }
}
