package com.shadowveil.videoplatform.service;

import com.shadowveil.videoplatform.entity.Comment;
import com.shadowveil.videoplatform.repository.CommentRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CommentService {

    private final CommentRepository commentRepository;

    public CommentService(CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }

    // Retrieve all comments.
    public List<Comment> getAllComments() {
        return commentRepository.findAll();
    }

    // Retrieve a single comment by its ID.
    public Optional<Comment> getCommentById(Integer id) {
        return commentRepository.findById(id);
    }

    // Create a new comment.
    public Comment createComment(Comment comment) {
        return commentRepository.save(comment);
    }

    // Update an existing comment.
    public Comment updateComment(Integer id, Comment commentDetails) {
        return commentRepository.findById(id).map(comment -> {
            comment.setVideo(commentDetails.getVideo());
            comment.setUser(commentDetails.getUser());
            comment.setContent(commentDetails.getContent());
            comment.setParentComment(commentDetails.getParentComment());
            // Typically, you might update the updatedAt timestamp here.
            comment.setUpdatedAt(commentDetails.getUpdatedAt());
            return commentRepository.save(comment);
        }).orElseThrow(() -> new RuntimeException("Comment not found with id " + id));
    }

    // Delete a comment by its ID.
    public void deleteComment(Integer id) {
        commentRepository.deleteById(id);
    }

    // Retrieve comments by video ID.
    public List<Comment> getCommentsByVideoId(Integer videoId) {
        return commentRepository.findByVideo_Id(videoId);
    }

    // Retrieve comments by user ID.
    public List<Comment> getCommentsByUserId(Integer userId) {
        return commentRepository.findByUser_Id(userId);
    }

    // Retrieve child comments (replies) for a specific parent comment.
    public List<Comment> getChildComments(Integer parentCommentId) {
        return commentRepository.findByParentComment_Id(parentCommentId);
    }
}
