package com.shadowveil.videoplatform.controller;

import com.shadowveil.videoplatform.dto.UserDto;
import com.shadowveil.videoplatform.dto.UserProfileDto;
import com.shadowveil.videoplatform.entity.UserProfile;
import com.shadowveil.videoplatform.service.UserProfileService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/user-profiles")
public class UserProfileController {

    private final UserProfileService userProfileService;

    @Autowired
    public UserProfileController(UserProfileService userProfileService) {
        this.userProfileService = userProfileService;
    }

    @GetMapping
    public ResponseEntity<List<UserProfileDto.Response>> getAllUserProfiles() {
        List<UserProfileDto.Response> userProfiles = userProfileService.getAllUserProfiles(); // Corrected type
        return new ResponseEntity<>(userProfiles, HttpStatus.OK);
    }

    @GetMapping("/{id}") // Get by profile ID (which is also the user ID)
    public ResponseEntity<UserProfileDto.Response> getUserProfileById(@PathVariable Integer id) {
        UserProfileDto.Response userProfile = userProfileService.getUserProfileById(id);
        return new ResponseEntity<>(userProfile, HttpStatus.OK);
    }

    @GetMapping("/user/{userId}") // Get by user ID (more explicit)
    public ResponseEntity<UserProfileDto.Response> getUserProfileByUserId(@PathVariable Integer userId) {
        UserProfileDto.Response userProfile = userProfileService.getUserProfileByUserId(userId);
        return new ResponseEntity<>(userProfile, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<UserProfileDto.Response> createUserProfile(@Valid @RequestBody UserProfileDto.Request requestDto) {
        UserProfile savedUserProfile = userProfileService.createUserProfile(requestDto);
        return new ResponseEntity<>(convertToDto(savedUserProfile), HttpStatus.CREATED); // Convert to DTO
    }


    @PutMapping("/{id}")
    public ResponseEntity<UserProfileDto.Response> updateUserProfile(
            @PathVariable Integer id,
            @Valid @RequestBody UserProfileDto.UpdateRequest requestDto) { // Use UpdateRequest
        UserProfile updatedProfile = userProfileService.updateUserProfile(id, requestDto);
        return new ResponseEntity<>(convertToDto(updatedProfile), HttpStatus.OK); // Convert to DTO
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUserProfile(@PathVariable Integer id) {
        userProfileService.deleteUserProfile(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    private UserProfileDto.Response convertToDto(UserProfile userProfile){
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
    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<String> handleEntityNotFoundException(EntityNotFoundException ex){
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
    }
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<String> handleDataIntegrityViolationException(DataIntegrityViolationException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.CONFLICT); // 409 Conflict
    }
    @ExceptionHandler(IllegalArgumentException.class)
    public  ResponseEntity<String> handleIllegalArgumentException(IllegalArgumentException ex){
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }
}