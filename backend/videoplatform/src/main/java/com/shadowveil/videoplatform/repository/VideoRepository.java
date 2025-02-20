package com.shadowveil.videoplatform.repository;

import com.shadowveil.videoplatform.entity.Video;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VideoRepository extends JpaRepository<Video, Integer> {

    List<Video> findByUser_Id(Integer userId);
    List<Video> findByModuleId(Integer moduleId); // Add this
}