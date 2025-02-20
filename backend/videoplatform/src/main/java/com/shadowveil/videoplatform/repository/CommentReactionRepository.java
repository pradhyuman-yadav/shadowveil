// File: src/main/java/com/shadowveil/videoplatform/repository/CommentReactionRepository.java
package com.shadowveil.videoplatform.repository;

import com.shadowveil.videoplatform.entity.CommentReaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface CommentReactionRepository extends JpaRepository<CommentReaction, Integer> {
    List<CommentReaction> findByCommentId(Integer commentId);
    List<CommentReaction> findByUserId(Integer userId);
    List<CommentReaction> findByCommentIdAndUserId(Integer commentId, Integer userId);

    // Optional, but useful for preventing duplicate reactions:
    Optional<CommentReaction> findOneByCommentIdAndUserId(Integer commentId, Integer userId);
}