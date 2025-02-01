package com.shadowveil.videoplatform.repository;

import com.shadowveil.videoplatform.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Integer> {

    // Find all comments associated with a specific video.
    List<Comment> findByVideo_Id(Integer videoId);

    // Find all comments created by a specific user.
    List<Comment> findByUser_Id(Integer userId);

    // Find all child comments (replies) for a given parent comment.
    List<Comment> findByParentComment_Id(Integer parentCommentId);
}
