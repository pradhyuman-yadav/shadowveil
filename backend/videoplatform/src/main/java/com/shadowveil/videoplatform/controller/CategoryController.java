package com.shadowveil.videoplatform.controller;

import com.shadowveil.videoplatform.dto.CategoryDto;
import com.shadowveil.videoplatform.entity.Category;
import com.shadowveil.videoplatform.service.CategoryService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/categories")
public class CategoryController {

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping
    public ResponseEntity<List<CategoryDto.Response>> getAllCategories() {
        List<CategoryDto.Response> categories = categoryService.getAllCategories();
        return ResponseEntity.ok(categories);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CategoryDto.Response> getCategoryById(@PathVariable Integer id) {
        return categoryService.getCategoryById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/name/{name}")
    public ResponseEntity<CategoryDto.Response> getCategoryByName(@PathVariable String name) {
        return categoryService.getCategoryByName(name)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<CategoryDto.Response> createCategory(@Valid @RequestBody CategoryDto.Request categoryDto) {
        Category createdCategory = categoryService.createCategory(categoryDto);
        CategoryDto.Response responseDto = categoryService.convertToDto(createdCategory); // Convert to Response DTO
        return new ResponseEntity<>(responseDto, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CategoryDto.Response> updateCategory(@PathVariable Integer id, @Valid @RequestBody CategoryDto.Request categoryDto) {
        Category updatedCategory = categoryService.updateCategory(id, categoryDto);
        CategoryDto.Response responseDto = categoryService.convertToDto(updatedCategory); //Convert to Response DTO
        return ResponseEntity.ok(responseDto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCategory(@PathVariable Integer id) {
        categoryService.deleteCategory(id);
        return ResponseEntity.noContent().build();
    }
}