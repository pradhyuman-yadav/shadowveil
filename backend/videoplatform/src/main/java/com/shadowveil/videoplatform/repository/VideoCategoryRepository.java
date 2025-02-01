package com.shadowveil.videoplatform.repository;

import com.shadowveil.videoplatform.entity.VideoCategory;
import com.shadowveil.videoplatform.entity.VideoCategoryId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VideoCategoryRepository extends JpaRepository<VideoCategory, VideoCategoryId> {

    // Retrieve all VideoCategory records for a specific video.
    List<VideoCategory> findByVideo_Id(Integer videoId);

    // Retrieve all VideoCategory records for a specific category.
    List<VideoCategory> findByCategory_Id(Integer categoryId);
}
