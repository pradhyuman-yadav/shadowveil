// src/main/java/com/shadowveil/videoplatform/repository/VideoTagRepository.java
package com.shadowveil.videoplatform.repository;

import com.shadowveil.videoplatform.entity.VideoTag;
import com.shadowveil.videoplatform.entity.VideoTagId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface VideoTagRepository extends JpaRepository<VideoTag, VideoTagId> {
    List<VideoTag> findByVideoId(Integer videoId);
    List<VideoTag> findByTagId(Integer tagId);
    List<VideoTag> findByVideoIdAndTagId(Integer videoId, Integer tagId); // Keep this
    Optional<VideoTag> findOneByVideoIdAndTagId(Integer videoId, Integer tagId);
    @Transactional
    void deleteByVideoIdAndTagId(Integer videoId, Integer tagId);
}