package com.shadowveil.videoplatform.repository;

import com.shadowveil.videoplatform.entity.AssignmentSubmission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface AssignmentSubmissionRepository extends JpaRepository<AssignmentSubmission, Integer> {
    List<AssignmentSubmission> findByAssignmentId(Integer assignmentId);
    List<AssignmentSubmission> findByUserId(Integer userId);
    List<AssignmentSubmission> findByAssignmentIdAndUserId(Integer assignmentId, Integer userId);
}