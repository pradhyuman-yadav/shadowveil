package com.shadowveil.videoplatform.service;

import com.shadowveil.videoplatform.dto.UserDto;
import com.shadowveil.videoplatform.dto.UserProfileDto;
import com.shadowveil.videoplatform.entity.User;
import com.shadowveil.videoplatform.entity.UserProfile;
import com.shadowveil.videoplatform.repository.UserProfileRepository;
import com.shadowveil.videoplatform.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserProfileService {

    private final UserProfileRepository userProfileRepository;
    private final UserRepository userRepository; // Inject UserRepository

    @Autowired
    public UserProfileService(UserProfileRepository userProfileRepository, UserRepository userRepository) {
        this.userProfileRepository = userProfileRepository;
        this.userRepository = userRepository;
    }

    public List<UserProfileDto.Response> getAllUserProfiles() {
        return userProfileRepository.findAll().stream()
                .map(this::convertToDto).collect(Collectors.toList());
    }

    public UserProfileDto.Response getUserProfileById(Integer id) {
        return convertToDto(userProfileRepository.findById(id).orElseThrow(()-> new EntityNotFoundException("User profile not found with ID: " + id)));
    }

    public UserProfileDto.Response getUserProfileByUserId(Integer userId) {
        return convertToDto(userProfileRepository.findByUsersId(userId).orElseThrow(()-> new EntityNotFoundException("User profile not found for User ID: "+ userId)));
    }

    @Transactional
    public UserProfile createUserProfile(UserProfileDto.Request requestDto) {
        // 1. Validate that the user exists
        User user = userRepository.findById(requestDto.userId())
                .orElseThrow(() -> new EntityNotFoundException("User with ID " + requestDto.userId() + " not found."));

        // 2. Check if a profile *already* exists (prevent duplicates)
        if (userProfileRepository.findByUsersId(requestDto.userId()).isPresent()) {
            throw new DataIntegrityViolationException("A profile already exists for user ID " + requestDto.userId());
        }

        // 3. Create the UserProfile entity
        UserProfile userProfile = new UserProfile();

        // 4. **CRITICAL (CORRECTED)**:  Set the *User* entity.  DO NOT set the ID directly.
        userProfile.setUsers(user);      // Set the associated User entity.  This is what @MapsId uses.

        // 5. Set other fields from the DTO
        userProfile.setFullName(requestDto.fullName());
        userProfile.setBio(requestDto.bio());
        userProfile.setProfilePictureUrl(requestDto.profilePictureUrl());
        userProfile.setSocialLinks(requestDto.socialLinks());

        // 6. Save the UserProfile.  The ID will be set automatically.
        return userProfileRepository.save(userProfile);
    }


    @Transactional
    public UserProfile updateUserProfile(Integer id, UserProfileDto.UpdateRequest requestDto) {
        UserProfile existingProfile = userProfileRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User profile not found with id: " + id));

        // Update fields (check for nulls to allow partial updates)
        if (requestDto.fullName() != null) {
            existingProfile.setFullName(requestDto.fullName());
        }
        if (requestDto.bio() != null) {
            existingProfile.setBio(requestDto.bio());
        }
        if (requestDto.profilePictureUrl() != null) {
            existingProfile.setProfilePictureUrl(requestDto.profilePictureUrl());
        }
        if (requestDto.socialLinks() != null) {
            existingProfile.setSocialLinks(requestDto.socialLinks());
        }

        return userProfileRepository.save(existingProfile);
    }

    @Transactional
    public void deleteUserProfile(Integer id) {
        UserProfile userProfile = userProfileRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User profile not found with id: " + id));
        userProfileRepository.delete(userProfile);
    }

    // Helper method to convert to DTO
    private UserProfileDto.Response convertToDto(UserProfile userProfile) {
        UserDto.Response userDto = new UserDto.Response(
                userProfile.getUsers().getId(),
                userProfile.getUsers().getUsername(),
                userProfile.getUsers().getEmail(),
                userProfile.getUsers().getRole(),
                userProfile.getUsers().getCreatedAt(),
                userProfile.getUsers().getUpdatedAt()
        );

        return new UserProfileDto.Response(
                userProfile.getId(), // This is the user ID
                userDto,
                userProfile.getFullName(),
                userProfile.getBio(),
                userProfile.getProfilePictureUrl(),
                userProfile.getSocialLinks(),
                userProfile.getCreatedAt(),
                userProfile.getUpdatedAt()
        );
    }
}