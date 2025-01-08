package com.shadowveil.videoplatform.controller;

import com.shadowveil.videoplatform.entity.User;
import com.shadowveil.videoplatform.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST Controller for User entity
 */
@RestController
@RequestMapping("/api/users")  // Base path for user-related APIs
public class UserController {

    private final UserService userService;

    // Constructor Injection
    public UserController(UserService userService) {
        this.userService = userService;
    }

    /**
     * GET /api/users
     *
     * @return a list of all users
     */
    @GetMapping
    public List<User> getAllUsers() {
        return userService.findAllUsers();
    }

    /**
     * GET /api/users/{id}
     *
     * @param id the ID of the user
     * @return the User with the given ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        User user = userService.findUserById(id);
        return ResponseEntity.ok(user);
    }

    /**
     * GET /api/users/email
     *
     * @param email the email of the user
     * @return the User with the given email
     */
    @GetMapping("/email")
    public ResponseEntity<User> getUserByEmail(@RequestParam String email) {
        return userService.findUserByEmail(email)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * POST /api/users
     *
     * @param user the User to create
     * @return the created User
     */
    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody User user) {
        User createdUser = userService.createUser(user);
        return ResponseEntity.ok(createdUser);
    }

    /**
     * PUT /api/users/{id}
     *
     * @param id   the ID of the user to update
     * @param user the updated user data
     * @return the updated User
     */
    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(@PathVariable Long id, @RequestBody User user) {
        User updatedUser = userService.updateUser(id, user);
        return ResponseEntity.ok(updatedUser);
    }

    /**
     * DELETE /api/users/{id}
     *
     * @param id the ID of the user to delete
     * @return a no-content response
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
}

