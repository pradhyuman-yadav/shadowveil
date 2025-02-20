// File: src/main/java/com/shadowveil/videoplatform/controller/CommentReactionController.java
package com.shadowveil.videoplatform.controller;

import com.shadowveil.videoplatform.entity.CommentReaction;
import com.shadowveil.videoplatform.service.CommentReactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/comment-reactions")
public class CommentReactionController {

    private final CommentReactionService commentReactionService;

    @Autowired
    public CommentReactionController(CommentReactionService commentReactionService) {
        this.commentReactionService = commentReactionService;
    }

    @GetMapping
    public ResponseEntity<List<CommentReaction>> getAllReactions() {
        return new ResponseEntity<>(commentReactionService.getAllReactions(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CommentReaction> getReactionById(@PathVariable Integer id) {
        Optional<CommentReaction> reaction = commentReactionService.getReactionById(id);
        return reaction.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping("/comment/{commentId}")
    public ResponseEntity<List<CommentReaction>> getReactionsByCommentId(@PathVariable Integer commentId) {
        return new ResponseEntity<>(commentReactionService.getReactionsByCommentId(commentId), HttpStatus.OK);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<CommentReaction>> getReactionsByUserId(@PathVariable Integer userId) {
        return new ResponseEntity<>(commentReactionService.getReactionsByUserId(userId), HttpStatus.OK);
    }
    @GetMapping("/comment/{commentId}/user/{userId}")
    public ResponseEntity<List<CommentReaction>> getReactionsByCommentAndUser(
            @PathVariable Integer commentId,
            @PathVariable Integer userId) {
        return new ResponseEntity<>(commentReactionService.getCommentReactionByCommentIdAndUserId(commentId, userId), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<CommentReaction> createReaction(@RequestBody CommentReaction reaction) {
        try {
            CommentReaction savedReaction = commentReactionService.createReaction(reaction);
            return new ResponseEntity<>(savedReaction, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST); // 400 for invalid comment/user or duplicate
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<CommentReaction> updateReaction(@PathVariable Integer id, @RequestBody CommentReaction reactionDetails) {
        try {
            CommentReaction updatedReaction = commentReactionService.updateReaction(id, reactionDetails);
            return updatedReaction != null ? new ResponseEntity<>(updatedReaction, HttpStatus.OK)
                    : new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReaction(@PathVariable Integer id) {
        try{
            commentReactionService.deleteReaction(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        catch (IllegalArgumentException e){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

    }
}