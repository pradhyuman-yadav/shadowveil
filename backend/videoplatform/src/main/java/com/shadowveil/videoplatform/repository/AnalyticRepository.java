package com.shadowveil.videoplatform.repository;

import com.shadowveil.videoplatform.entity.Analytic;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AnalyticRepository extends JpaRepository<Analytic, Integer> {

    // Find all analytics records for a given video (using the video entity's id)
    List<Analytic> findByVideo_Id(Integer videoId);

    // Find all analytics records for a given user (using the user entity's id)
    List<Analytic> findByUser_Id(Integer userId);
}
