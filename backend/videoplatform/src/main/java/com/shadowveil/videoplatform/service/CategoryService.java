package com.shadowveil.videoplatform.service;

import com.shadowveil.videoplatform.entity.Category;
import com.shadowveil.videoplatform.repository.CategoryRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    // Retrieve all categories.
    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    // Retrieve a single category by its ID.
    public Optional<Category> getCategoryById(Integer id) {
        return categoryRepository.findById(id);
    }

    // Create a new category.
    public Category createCategory(Category category) {
        // Additional business logic can be added here if needed.
        return categoryRepository.save(category);
    }

    // Update an existing category.
    public Category updateCategory(Integer id, Category categoryDetails) {
        return categoryRepository.findById(id).map(category -> {
            category.setName(categoryDetails.getName());
            category.setDescription(categoryDetails.getDescription());
            // Typically, you may not update createdAt; adjust as necessary.
            return categoryRepository.save(category);
        }).orElseThrow(() -> new RuntimeException("Category not found with id " + id));
    }

    // Delete a category by its ID.
    public void deleteCategory(Integer id) {
        categoryRepository.deleteById(id);
    }
}
