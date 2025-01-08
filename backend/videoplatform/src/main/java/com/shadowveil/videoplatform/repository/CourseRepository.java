package com.shadowveil.videoplatform.repository;

import com.shadowveil.videoplatform.entity.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository interface for Course entity
 */
@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {

    /**
     * Find all courses by instructor ID
     *
     * @param instructorId the ID of the instructor
     * @return a list of courses taught by the instructor
     */
    List<Course> findByInstructorId(Long instructorId);
}

