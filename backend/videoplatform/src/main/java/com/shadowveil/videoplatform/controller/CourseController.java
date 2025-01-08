package com.shadowveil.videoplatform.controller;

import com.shadowveil.videoplatform.entity.Course;
import com.shadowveil.videoplatform.service.CourseService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST Controller for Course entity
 */
@RestController
@RequestMapping("/api/courses")
public class CourseController {

    private final CourseService courseService;

    public CourseController(CourseService courseService) {
        this.courseService = courseService;
    }

    /**
     * GET /api/courses
     *
     * @return a list of all courses
     */
    @GetMapping
    public List<Course> getAllCourses() {
        return courseService.findAllCourses();
    }

    /**
     * GET /api/courses/{id}
     *
     * @param id the ID of the course
     * @return the Course with the given ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<Course> getCourseById(@PathVariable Long id) {
        Course course = courseService.findCourseById(id);
        return ResponseEntity.ok(course);
    }

    /**
     * GET /api/courses/instructor/{instructorId}
     *
     * @param instructorId the ID of the instructor
     * @return a list of courses taught by the instructor
     */
    @GetMapping("/instructor/{instructorId}")
    public List<Course> getCoursesByInstructorId(@PathVariable Long instructorId) {
        return courseService.findCoursesByInstructorId(instructorId);
    }

    /**
     * POST /api/courses
     *
     * @param course the Course to create
     * @return the created Course
     */
    @PostMapping
    public ResponseEntity<Course> createCourse(@RequestBody Course course) {
        Course createdCourse = courseService.createCourse(course);
        return ResponseEntity.ok(createdCourse);
    }

    /**
     * PUT /api/courses/{id}
     *
     * @param id     the ID of the course to update
     * @param course the updated course data
     * @return the updated Course
     */
    @PutMapping("/{id}")
    public ResponseEntity<Course> updateCourse(
            @PathVariable Long id,
            @RequestBody Course course
    ) {
        Course updatedCourse = courseService.updateCourse(id, course);
        return ResponseEntity.ok(updatedCourse);
    }

    /**
     * DELETE /api/courses/{id}
     *
     * @param id the ID of the course to delete
     * @return a no-content response
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCourse(@PathVariable Long id) {
        courseService.deleteCourse(id);
        return ResponseEntity.noContent().build();
    }
}

