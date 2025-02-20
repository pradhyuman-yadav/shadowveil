// File: src/main/java/com/shadowveil/videoplatform/repository/TagRepository.java
package com.shadowveil.videoplatform.repository;

import com.shadowveil.videoplatform.entity.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.List;

@Repository
public interface TagRepository extends JpaRepository<Tag, Integer> {
    // Find a tag by its name (useful because of the unique constraint)
    Optional<Tag> findByName(String name);
    List<Tag> findByNameContainingIgnoreCase(String name);
}