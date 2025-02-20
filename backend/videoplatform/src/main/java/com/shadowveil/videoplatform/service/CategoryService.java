package com.shadowveil.videoplatform.service;

import com.shadowveil.videoplatform.dto.CategoryDto;
import com.shadowveil.videoplatform.entity.Category;
import com.shadowveil.videoplatform.exception.BadRequestException;
import com.shadowveil.videoplatform.exception.ResourceNotFoundException;
import com.shadowveil.videoplatform.repository.CategoryRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Transactional(readOnly = true)
    public List<CategoryDto.Response> getAllCategories() {
        return categoryRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Optional<CategoryDto.Response> getCategoryById(Integer id) {
        return categoryRepository.findById(id)
                .map(this::convertToDto);
    }

    @Transactional(readOnly = true)
    public Optional<CategoryDto.Response> getCategoryByName(String name) {
        return categoryRepository.findByName(name)
                .map(this::convertToDto);
    }

    @Transactional
    public Category createCategory(CategoryDto.Request categoryDto) {
        if (categoryRepository.findByName(categoryDto.name()).isPresent()) {
            throw new BadRequestException("A category with this name already exists.");
        }
        Category category = new Category();
        category.setName(categoryDto.name());
        category.setDescription(categoryDto.description());
        return categoryRepository.save(category);
    }

    @Transactional
    public Category updateCategory(Integer id, CategoryDto.Request categoryDto) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with id " + id));

        if (!category.getName().equals(categoryDto.name()) && categoryRepository.findByName(categoryDto.name()).isPresent()) {
            throw new BadRequestException("A category with this name already exists.");
        }
        if(categoryDto.name() != null) category.setName(categoryDto.name()); //Update only if provided
        if(categoryDto.description() != null) category.setDescription(categoryDto.description()); //Update only if provided
        return categoryRepository.save(category);
    }

    @Transactional
    public void deleteCategory(Integer id) {
        if (!categoryRepository.existsById(id)) {
            throw new ResourceNotFoundException("Category with ID " + id + " does not exist");
        }
        categoryRepository.deleteById(id);
    }

    public CategoryDto.Response convertToDto(Category category) {
        return new CategoryDto.Response(
                category.getId(),
                category.getName(),
                category.getDescription(),
                category.getCreatedAt()
        );
    }
}