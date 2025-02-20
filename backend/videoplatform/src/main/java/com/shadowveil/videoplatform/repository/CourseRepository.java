package com.shadowveil.videoplatform.repository;

import com.shadowveil.videoplatform.entity.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CourseRepository extends JpaRepository<Course, Integer> {
    List<Course> findByInstructorId(Integer instructorId);
    List<Course> findByCategoryId(Integer categoryId);
}