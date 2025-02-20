// (No major changes, but added a potentially useful method)
package com.shadowveil.videoplatform.repository;

import com.shadowveil.videoplatform.entity.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.List;

@Repository
public interface TagRepository extends JpaRepository<Tag, Integer> {
    Optional<Tag> findByName(String name);
    List<Tag> findByNameContainingIgnoreCase(String name); // Useful for search
}