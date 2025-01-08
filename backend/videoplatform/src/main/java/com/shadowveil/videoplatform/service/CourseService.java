package com.shadowveil.videoplatform.service;

import com.shadowveil.videoplatform.entity.Course;
import com.shadowveil.videoplatform.repository.CourseRepository;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service class for Course entity
 */
@Service
public class CourseService {

    private final CourseRepository courseRepository;

    public CourseService(CourseRepository courseRepository) {
        this.courseRepository = courseRepository;
    }

    /**
     * Retrieve all courses
     *
     * @return a list of all courses
     */
    public List<Course> findAllCourses() {
        return courseRepository.findAll();
    }

    /**
     * Retrieve a course by its ID
     *
     * @param id the ID of the course
     * @return the Course if found
     * @throws RuntimeException if no course is found
     */
    public Course findCourseById(Long id) {
        return courseRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Course not found with id: " + id));
    }

    /**
     * Retrieve all courses taught by a specific instructor
     *
     * @param instructorId the ID of the instructor
     * @return a list of courses
     */
    public List<Course> findCoursesByInstructorId(Long instructorId) {
        return courseRepository.findByInstructorId(instructorId);
    }

    /**
     * Create a new course
     *
     * @param course the course to create
     * @return the saved course
     */
    public Course createCourse(Course course) {
        return courseRepository.save(course);
    }

    /**
     * Update an existing course
     *
     * @param id     the ID of the course to update
     * @param course the updated course data
     * @return the updated course
     */
    public Course updateCourse(Long id, Course course) {
        Course existingCourse = findCourseById(id);
        existingCourse.setTitle(course.getTitle());
        existingCourse.setDescription(course.getDescription());
//        existingCourse.setInstructorId(course.getInstructorId());
        return courseRepository.save(existingCourse);
    }

    /**
     * Delete a course by its ID
     *
     * @param id the ID of the course to delete
     */
    public void deleteCourse(Long id) {
        Course course = findCourseById(id);
        courseRepository.delete(course);
    }
}

