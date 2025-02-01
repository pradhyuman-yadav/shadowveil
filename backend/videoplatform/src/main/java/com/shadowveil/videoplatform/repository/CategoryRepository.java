package com.shadowveil.videoplatform.repository;

import com.shadowveil.videoplatform.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Integer> {

    // Optional method to find a category by its name.
    Optional<Category> findByName(String name);
}
