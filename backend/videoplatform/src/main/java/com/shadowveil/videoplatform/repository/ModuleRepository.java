package com.shadowveil.videoplatform.repository;

import com.shadowveil.videoplatform.entity.Module;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ModuleRepository extends JpaRepository<Module, Integer> {
    List<Module> findByCourseId(Integer courseId);
    List<Module> findByCourseIdOrderByPositionAsc(Integer courseId);
}