package com.shadowveil.videoplatform.repository;

import com.shadowveil.videoplatform.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository interface for User entity
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * Find a user by email
     *
     * @param email the email to search for
     * @return an Optional containing the User if found, or empty otherwise
     */
    Optional<User> findByEmail(String email);

    /**
     * Check if a user exists by email
     *
     * @param email the email to check
     * @return true if a user with the given email exists, false otherwise
     */
    boolean existsByEmail(String email);
}


