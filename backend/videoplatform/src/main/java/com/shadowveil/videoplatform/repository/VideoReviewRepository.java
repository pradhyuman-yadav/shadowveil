// File: src/main/java/com/shadowveil/videoplatform/repository/VideoReviewRepository.java
package com.shadowveil.videoplatform.repository;

import com.shadowveil.videoplatform.entity.VideoReview;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface VideoReviewRepository extends JpaRepository<VideoReview, Integer> {
    List<VideoReview> findByVideoId(Integer videoId);
    List<VideoReview> findByUserId(Integer userId);
    List<VideoReview> findByVideoIdAndUserId(Integer videoId, Integer userId);

    // Optional, but useful for preventing duplicate reviews:
    Optional<VideoReview> findOneByVideoIdAndUserId(Integer videoId, Integer userId);
}