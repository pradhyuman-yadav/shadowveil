package com.shadowveil.videoplatform.repository;

import com.shadowveil.videoplatform.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Integer> {
    List<Comment> findByVideo_Id(Integer videoId);

    List<Comment> findByUser_Id(Integer userId);

    List<Comment> findByParentComment_Id(Integer parentCommentId);
}