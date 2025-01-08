package com.shadowveil.videoplatform.repository;

import com.shadowveil.videoplatform.entity.Course;
import com.shadowveil.videoplatform.entity.Video;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository interface for Video entity
 */
@Repository
public interface VideoRepository extends JpaRepository<Video, Long> {

    /**
     * Find all videos by course ID
     *
     * @param courseId the ID of the course
     * @return a list of videos for the given course
     */
    @Query("SELECT v FROM Video v WHERE v.course.id = :courseId")
    List<Video> findByCourseId(@Param("courseId") Long courseId);

}