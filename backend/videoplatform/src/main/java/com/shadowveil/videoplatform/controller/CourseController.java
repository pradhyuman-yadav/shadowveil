package com.shadowveil.videoplatform.controller;

import com.shadowveil.videoplatform.dto.CourseDto;
import com.shadowveil.videoplatform.entity.Course;
import com.shadowveil.videoplatform.service.CourseService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/courses")
public class CourseController {

    private final CourseService courseService;

    @Autowired
    public CourseController(CourseService courseService) {
        this.courseService = courseService;
    }

    @GetMapping
    public ResponseEntity<List<CourseDto.Response>> getAllCourses() {
        List<CourseDto.Response> courses = courseService.getAllCourses();
        return new ResponseEntity<>(courses, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CourseDto.Response> getCourseById(@PathVariable Integer id) {
        Optional<CourseDto.Response> course = courseService.getCourseById(id);
        return course.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping("/instructor/{instructorId}")
    public ResponseEntity<List<CourseDto.Response>> getCoursesByInstructorId(@PathVariable Integer instructorId) {
        List<CourseDto.Response> courses = courseService.getCoursesByInstructorId(instructorId);
        return new ResponseEntity<>(courses, HttpStatus.OK);
    }

    @GetMapping("/category/{categoryId}")
    public ResponseEntity<List<CourseDto.Response>> getCoursesByCategoryId(@PathVariable Integer categoryId) {
        List<CourseDto.Response> courses = courseService.getCoursesByCategoryId(categoryId);
        return new ResponseEntity<>(courses, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<CourseDto.Response> createCourse(@Valid @RequestBody CourseDto.Request courseDto) {
        Course course = courseService.createCourseFromDto(courseDto);
        CourseDto.Response responseDto = courseService.convertToDto(course);
        return new ResponseEntity<>(responseDto, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CourseDto.Response> updateCourse(@PathVariable Integer id, @Valid @RequestBody CourseDto.Request courseDto) {
        Course updatedCourse = courseService.updateCourseFromDto(id, courseDto);
        CourseDto.Response responseDto = courseService.convertToDto(updatedCourse);
        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCourse(@PathVariable Integer id) {
        courseService.deleteCourse(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}