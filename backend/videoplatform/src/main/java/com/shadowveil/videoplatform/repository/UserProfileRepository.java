// File: src/main/java/com/shadowveil/videoplatform/repository/UserProfileRepository.java
package com.shadowveil.videoplatform.repository;

import com.shadowveil.videoplatform.entity.UserProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserProfileRepository extends JpaRepository<UserProfile, Integer> {
    // Find a user profile by user ID
    Optional<UserProfile> findByUsersId(Integer userId);
}