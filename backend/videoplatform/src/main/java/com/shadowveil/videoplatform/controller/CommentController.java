package com.shadowveil.videoplatform.controller;

import com.shadowveil.videoplatform.entity.Comment;
import com.shadowveil.videoplatform.service.CommentService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/comments")
public class CommentController {

    private final CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    // GET /api/comments
    @GetMapping
    public ResponseEntity<List<Comment>> getAllComments() {
        List<Comment> comments = commentService.getAllComments();
        return ResponseEntity.ok(comments);
    }

    // GET /api/comments/{id}
    @GetMapping("/{id}")
    public ResponseEntity<Comment> getCommentById(@PathVariable Integer id) {
        return commentService.getCommentById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // POST /api/comments
    @PostMapping
    public ResponseEntity<Comment> createComment(@Valid @RequestBody Comment comment) {
        Comment createdComment = commentService.createComment(comment);
        return ResponseEntity.ok(createdComment);
    }

    // PUT /api/comments/{id}
    @PutMapping("/{id}")
    public ResponseEntity<Comment> updateComment(@PathVariable Integer id, @Valid @RequestBody Comment comment) {
        try {
            Comment updatedComment = commentService.updateComment(id, comment);
            return ResponseEntity.ok(updatedComment);
        } catch (RuntimeException ex) {
            return ResponseEntity.notFound().build();
        }
    }

    // DELETE /api/comments/{id}
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteComment(@PathVariable Integer id) {
        commentService.deleteComment(id);
        return ResponseEntity.noContent().build();
    }

    // GET /api/comments/video/{videoId}
    @GetMapping("/video/{videoId}")
    public ResponseEntity<List<Comment>> getCommentsByVideoId(@PathVariable Integer videoId) {
        List<Comment> comments = commentService.getCommentsByVideoId(videoId);
        return ResponseEntity.ok(comments);
    }

    // GET /api/comments/user/{userId}
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Comment>> getCommentsByUserId(@PathVariable Integer userId) {
        List<Comment> comments = commentService.getCommentsByUserId(userId);
        return ResponseEntity.ok(comments);
    }

    // GET /api/comments/parent/{parentCommentId}
    @GetMapping("/parent/{parentCommentId}")
    public ResponseEntity<List<Comment>> getChildComments(@PathVariable Integer parentCommentId) {
        List<Comment> comments = commentService.getChildComments(parentCommentId);
        return ResponseEntity.ok(comments);
    }
}
