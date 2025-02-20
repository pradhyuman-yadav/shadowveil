// src/main/java/com/shadowveil/videoplatform/repository/ReportRepository.java
package com.shadowveil.videoplatform.repository;

import com.shadowveil.videoplatform.entity.Report;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ReportRepository extends JpaRepository<Report, Integer> {
    List<Report> findByReporterId(Integer reporterId);
    List<Report> findByReportedTypeAndReportedId(String reportedType, Integer reportedId);
    List<Report> findByStatus(String status); // Keep as String
    // Consider pagination for large result sets:
    // List<Report> findByReporterId(Integer reporterId, Pageable pageable);
}