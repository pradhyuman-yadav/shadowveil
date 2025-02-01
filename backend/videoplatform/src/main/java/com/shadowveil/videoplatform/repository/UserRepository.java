package com.shadowveil.videoplatform.repository;

import com.shadowveil.videoplatform.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

    // Find a user by their username.
    Optional<User> findByUsername(String username);

    // Find a user by their email.
    Optional<User> findByEmail(String email);
}
