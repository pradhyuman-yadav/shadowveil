// File: src/main/java/com/shadowveil/videoplatform/service/UserProfileService.java
package com.shadowveil.videoplatform.service;

import com.shadowveil.videoplatform.entity.User;
import com.shadowveil.videoplatform.entity.UserProfile;
import com.shadowveil.videoplatform.repository.UserProfileRepository;
import com.shadowveil.videoplatform.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Service
public class UserProfileService {

    private final UserProfileRepository userProfileRepository;
    private final UserRepository userRepository; // Inject UserRepository

    @Autowired
    public UserProfileService(UserProfileRepository userProfileRepository, UserRepository userRepository) {
        this.userProfileRepository = userProfileRepository;
        this.userRepository = userRepository;
    }

    public List<UserProfile> getAllUserProfiles() {
        return userProfileRepository.findAll();
    }

    public Optional<UserProfile> getUserProfileById(Integer id) {
        return userProfileRepository.findById(id);
    }

    public Optional<UserProfile> getUserProfileByUserId(Integer userId) {
        return userProfileRepository.findByUsersId(userId);
    }

    @Transactional
    public UserProfile createUserProfile(UserProfile userProfile) {
        // Validate that the user exists
        Optional<User> user = userRepository.findById(userProfile.getId()); // Use getId(), as it's the PK
        if (user.isEmpty()) {
            throw new IllegalArgumentException("User with ID " + userProfile.getId() + " not found.");
        }

        // Check if a profile already exists for this user
        Optional<UserProfile> existingProfile = userProfileRepository.findByUsersId(userProfile.getId());
        if (existingProfile.isPresent()) {
            throw new IllegalArgumentException("A profile already exists for user ID " + userProfile.getId());
        }

        userProfile.setUsers(user.get()); // Set the User object
        userProfile.setUpdatedAt(Instant.now());
        return userProfileRepository.save(userProfile);
    }

    @Transactional
    public UserProfile updateUserProfile(Integer id, UserProfile profileDetails) {
        Optional<UserProfile> optionalProfile = userProfileRepository.findById(id);
        if (optionalProfile.isPresent()) {
            UserProfile existingProfile = optionalProfile.get();

            // No need to check for user existence here, as the profile ID == user ID,
            // and we already found the profile.
            if(profileDetails.getUsers() != null && !profileDetails.getUsers().getId().equals(existingProfile.getUsers().getId())){
                Optional<User> user = userRepository.findById(profileDetails.getUsers().getId());
                if(user.isEmpty()){
                    throw new IllegalArgumentException("User with ID " + profileDetails.getUsers().getId()+ " does not exists");
                }
            }
            existingProfile.setFullName(profileDetails.getFullName());
            existingProfile.setBio(profileDetails.getBio());
            existingProfile.setProfilePictureUrl(profileDetails.getProfilePictureUrl());
            existingProfile.setSocialLinks(profileDetails.getSocialLinks());
            existingProfile.setUpdatedAt(Instant.now());
            return userProfileRepository.save(existingProfile);

        } else {
            return null; // Or throw an exception
        }
    }

    @Transactional
    public void deleteUserProfile(Integer id) {
        if(userProfileRepository.existsById(id)) {
            userProfileRepository.deleteById(id);
        }
        else{
            throw new IllegalArgumentException("User profile with ID " + id + " does not exists");
        }
    }
}