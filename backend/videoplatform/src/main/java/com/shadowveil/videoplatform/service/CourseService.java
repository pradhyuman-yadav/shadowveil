package com.shadowveil.videoplatform.service;

import com.shadowveil.videoplatform.dto.CategoryDto;
import com.shadowveil.videoplatform.dto.CourseDto;
import com.shadowveil.videoplatform.dto.UserDto;
import com.shadowveil.videoplatform.entity.Category;
import com.shadowveil.videoplatform.entity.Course;
import com.shadowveil.videoplatform.entity.User;
import com.shadowveil.videoplatform.exception.ResourceNotFoundException;
import com.shadowveil.videoplatform.repository.CategoryRepository;
import com.shadowveil.videoplatform.repository.CourseRepository;
import com.shadowveil.videoplatform.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CourseService {

    private final CourseRepository courseRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;

    @Autowired
    public CourseService(CourseRepository courseRepository, UserRepository userRepository, CategoryRepository categoryRepository) {
        this.courseRepository = courseRepository;
        this.userRepository = userRepository;
        this.categoryRepository = categoryRepository;
    }

    @Transactional(readOnly = true)
    public List<CourseDto.Response> getAllCourses() {
        return courseRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Optional<CourseDto.Response> getCourseById(Integer id) {
        return courseRepository.findById(id)
                .map(this::convertToDto);
    }

    @Transactional(readOnly = true)
    public List<CourseDto.Response> getCoursesByInstructorId(Integer instructorId) {
        return courseRepository.findByInstructorId(instructorId).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<CourseDto.Response> getCoursesByCategoryId(Integer categoryId) {
        return courseRepository.findByCategoryId(categoryId).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }


    @Transactional
    public Course createCourseFromDto(CourseDto.Request courseDto) {
        User instructor = userRepository.findById(courseDto.instructorId())
                .orElseThrow(() -> new ResourceNotFoundException("Instructor with ID " + courseDto.instructorId() + " not found."));

        Category category = categoryRepository.findById(courseDto.categoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Category with ID " + courseDto.categoryId() + " not found."));

        Course course = new Course();
        course.setInstructor(instructor);
        course.setCategory(category);
        course.setTitle(courseDto.title());
        course.setDescription(courseDto.description());
        course.setCreatedAt(Instant.now()); // Set creation time explicitly
        course.setUpdatedAt(Instant.now()); // Set both created and updated
        return courseRepository.save(course);
    }

    @Transactional
    public Course updateCourseFromDto(Integer id, CourseDto.Request courseDto) {
        Course existingCourse = courseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Course with ID: " + id + " not found."));

        // Update instructor if provided
        if (courseDto.instructorId() != null && !existingCourse.getInstructor().getId().equals(courseDto.instructorId())) {
            User newInstructor = userRepository.findById(courseDto.instructorId())
                    .orElseThrow(() -> new ResourceNotFoundException("Instructor with ID " + courseDto.instructorId() + " not found."));
            existingCourse.setInstructor(newInstructor);
        }

        // Update category if provided
        if (courseDto.categoryId() != null && !existingCourse.getCategory().getId().equals(courseDto.categoryId())) {
            Category newCategory = categoryRepository.findById(courseDto.categoryId())
                    .orElseThrow(() -> new ResourceNotFoundException("Category with ID " + courseDto.categoryId() + " not found."));
            existingCourse.setCategory(newCategory);
        }

        // Update other fields
        if (courseDto.title() != null) existingCourse.setTitle(courseDto.title());
        if (courseDto.description() != null) existingCourse.setDescription(courseDto.description());

        existingCourse.setUpdatedAt(Instant.now());
        return courseRepository.save(existingCourse);
    }



    @Transactional
    public void deleteCourse(Integer id) {
        if(!courseRepository.existsById(id)){
            throw new ResourceNotFoundException("Course with ID " + id + " does not exists");
        }
        courseRepository.deleteById(id);
    }

    // --- DTO Conversion Methods (Private) ---

    public CourseDto.Response convertToDto(Course course) {
        UserDto.Response userDto = null;
        if(course.getInstructor() != null){
            userDto = convertToDto(course.getInstructor());
        }

        CategoryDto.Response categoryDto = null;
        if(course.getCategory() != null){
            categoryDto = convertToDto(course.getCategory());
        }

        return new CourseDto.Response(
                course.getId(),
                userDto, //
                course.getTitle(),
                course.getDescription(),
                course.getCreatedAt(),
                course.getUpdatedAt(),
                categoryDto
        );
    }
    //Helper methods for conversion
    private UserDto.Response convertToDto(User user) { // Corrected return type
        return new UserDto.Response(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getRole(),
                user.getCreatedAt(),
                user.getUpdatedAt()
        );
    }

    private CategoryDto.Response convertToDto(Category category) {
        return new CategoryDto.Response(
                category.getId(),
                category.getName(),
                category.getDescription(),
                category.getCreatedAt()
        );
    }
}