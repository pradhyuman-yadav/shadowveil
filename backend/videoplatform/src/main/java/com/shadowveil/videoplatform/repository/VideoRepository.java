package com.shadowveil.videoplatform.repository;

import com.shadowveil.videoplatform.model.Video;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VideoRepository extends JpaRepository<Video, Long> {
}