package com.shadowveil.videoplatform.repository;

import com.shadowveil.videoplatform.entity.CourseEnrollment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CourseEnrollmentRepository extends JpaRepository<CourseEnrollment, Integer> {
    List<CourseEnrollment> findByCourseId(Integer courseId);
    List<CourseEnrollment> findByUserId(Integer userId);
    List<CourseEnrollment> findByCourseIdAndUserId(Integer courseId, Integer userId);
    Optional<CourseEnrollment> findOneByCourseIdAndUserId(Integer courseId, Integer userId);
}