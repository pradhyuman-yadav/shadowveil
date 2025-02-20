//(No Change needed, well-defined)
package com.shadowveil.videoplatform.repository;

import com.shadowveil.videoplatform.entity.UserProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserProfileRepository extends JpaRepository<UserProfile, Integer> {
    Optional<UserProfile> findByUsersId(Integer userId);
}