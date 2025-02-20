package com.shadowveil.videoplatform.controller;

import com.shadowveil.videoplatform.dto.CommentDto;
import com.shadowveil.videoplatform.entity.Comment;
import com.shadowveil.videoplatform.service.CommentService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/comments")
public class CommentController {

    private final CommentService commentService;

    @Autowired
    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @GetMapping
    public ResponseEntity<List<CommentDto.Response>> getAllComments() {
        List<CommentDto.Response> comments = commentService.getAllComments();
        return ResponseEntity.ok(comments);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CommentDto.Response> getCommentById(@PathVariable Integer id) {
        Optional<CommentDto.Response> comment = commentService.getCommentById(id);
        return comment.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping
    public ResponseEntity<CommentDto.Response> createComment(@Valid @RequestBody CommentDto.Request commentDto) {
        Comment createdComment = commentService.createComment(commentDto);
        CommentDto.Response responseDto = commentService.convertToDto(createdComment); // Convert to DTO
        return new ResponseEntity<>(responseDto, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CommentDto.Response> updateComment(@PathVariable Integer id, @Valid @RequestBody CommentDto.Request commentDto) {
        Comment updatedComment = commentService.updateComment(id, commentDto);
        CommentDto.Response responseDto = commentService.convertToDto(updatedComment);
        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteComment(@PathVariable Integer id) {
        commentService.deleteComment(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/video/{videoId}")
    public ResponseEntity<List<CommentDto.Response>> getCommentsByVideoId(@PathVariable Integer videoId) {
        List<CommentDto.Response> comments = commentService.getCommentsByVideoId(videoId);
        return ResponseEntity.ok(comments);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<CommentDto.Response>> getCommentsByUserId(@PathVariable Integer userId) {
        List<CommentDto.Response> comments = commentService.getCommentsByUserId(userId);
        return ResponseEntity.ok(comments);
    }

    @GetMapping("/parent/{parentCommentId}")
    public ResponseEntity<List<CommentDto.Response>> getChildComments(@PathVariable Integer parentCommentId) {
        List<CommentDto.Response> comments = commentService.getChildComments(parentCommentId);
        return ResponseEntity.ok(comments);
    }
}