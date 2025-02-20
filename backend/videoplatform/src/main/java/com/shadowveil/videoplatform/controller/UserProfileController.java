// File: src/main/java/com/shadowveil/videoplatform/controller/UserProfileController.java
package com.shadowveil.videoplatform.controller;

import com.shadowveil.videoplatform.entity.UserProfile;
import com.shadowveil.videoplatform.service.UserProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/user-profiles")
public class UserProfileController {

    private final UserProfileService userProfileService;

    @Autowired
    public UserProfileController(UserProfileService userProfileService) {
        this.userProfileService = userProfileService;
    }

    @GetMapping
    public ResponseEntity<List<UserProfile>> getAllUserProfiles() {
        return new ResponseEntity<>(userProfileService.getAllUserProfiles(), HttpStatus.OK);
    }

    @GetMapping("/{id}") // Get by profile ID (which is also the user ID)
    public ResponseEntity<UserProfile> getUserProfileById(@PathVariable Integer id) {
        Optional<UserProfile> userProfile = userProfileService.getUserProfileById(id);
        return userProfile.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping("/user/{userId}") // Get by user ID (more explicit)
    public ResponseEntity<UserProfile> getUserProfileByUserId(@PathVariable Integer userId) {
        Optional<UserProfile> userProfile = userProfileService.getUserProfileByUserId(userId);
        return userProfile.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping
    public ResponseEntity<UserProfile> createUserProfile(@RequestBody UserProfile userProfile) {
        try {
            UserProfile savedUserProfile = userProfileService.createUserProfile(userProfile);
            return new ResponseEntity<>(savedUserProfile, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST); // 400 for invalid user or duplicate profile
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserProfile> updateUserProfile(@PathVariable Integer id, @RequestBody UserProfile profileDetails) {
        try {
            UserProfile updatedProfile = userProfileService.updateUserProfile(id, profileDetails);
            return updatedProfile != null ? new ResponseEntity<>(updatedProfile, HttpStatus.OK)
                    : new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUserProfile(@PathVariable Integer id) {
        try{
            userProfileService.deleteUserProfile(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        catch (IllegalArgumentException e){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

    }
}