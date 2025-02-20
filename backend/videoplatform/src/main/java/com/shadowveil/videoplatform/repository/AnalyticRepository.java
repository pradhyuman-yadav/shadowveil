package com.shadowveil.videoplatform.repository;

import com.shadowveil.videoplatform.entity.Analytic;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AnalyticRepository extends JpaRepository<Analytic, Integer> {
    List<Analytic> findByVideo_Id(Integer videoId);
    List<Analytic> findByUser_Id(Integer userId);
}