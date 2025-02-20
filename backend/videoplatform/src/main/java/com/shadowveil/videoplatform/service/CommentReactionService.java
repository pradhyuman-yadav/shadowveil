// File: src/main/java/com/shadowveil/videoplatform/service/CommentReactionService.java
package com.shadowveil.videoplatform.service;

import com.shadowveil.videoplatform.entity.Comment;
import com.shadowveil.videoplatform.entity.CommentReaction;
import com.shadowveil.videoplatform.entity.User;
import com.shadowveil.videoplatform.repository.CommentReactionRepository;
import com.shadowveil.videoplatform.repository.CommentRepository;
import com.shadowveil.videoplatform.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class CommentReactionService {

    private final CommentReactionRepository commentReactionRepository;
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;

    @Autowired
    public CommentReactionService(
            CommentReactionRepository commentReactionRepository,
            CommentRepository commentRepository,
            UserRepository userRepository) {
        this.commentReactionRepository = commentReactionRepository;
        this.commentRepository = commentRepository;
        this.userRepository = userRepository;
    }

    public List<CommentReaction> getAllReactions() {
        return commentReactionRepository.findAll();
    }

    public Optional<CommentReaction> getReactionById(Integer id) {
        return commentReactionRepository.findById(id);
    }

    public List<CommentReaction> getReactionsByCommentId(Integer commentId) {
        return commentReactionRepository.findByCommentId(commentId);
    }

    public List<CommentReaction> getReactionsByUserId(Integer userId) {
        return commentReactionRepository.findByUserId(userId);
    }
    public List<CommentReaction> getCommentReactionByCommentIdAndUserId(Integer commentId, Integer userId){
        return commentReactionRepository.findByCommentIdAndUserId(commentId,userId);
    }

    @Transactional
    public CommentReaction createReaction(CommentReaction reaction) {
        // Validate that the comment exists
        Optional<Comment> comment = commentRepository.findById(reaction.getComment().getId());
        if (comment.isEmpty()) {
            throw new IllegalArgumentException("Comment with ID " + reaction.getComment().getId() + " not found.");
        }

        // Validate that the user exists
        Optional<User> user = userRepository.findById(reaction.getUser().getId());
        if (user.isEmpty()) {
            throw new IllegalArgumentException("User with ID " + reaction.getUser().getId() + " not found.");
        }

        // Prevent duplicate reactions
        Optional<CommentReaction> existingReaction = commentReactionRepository.findOneByCommentIdAndUserId(
                reaction.getComment().getId(), reaction.getUser().getId());
        if (existingReaction.isPresent()) {
            throw new IllegalArgumentException("User has already reacted to this comment.");
        }

        reaction.setComment(comment.get());
        reaction.setUser(user.get());
        // reaction.setCreatedAt(Instant.now()); //Removed to use DB default

        return commentReactionRepository.save(reaction);
    }

    @Transactional
    public CommentReaction updateReaction(Integer id, CommentReaction reactionDetails) {
        Optional<CommentReaction> optionalReaction = commentReactionRepository.findById(id);
        if (optionalReaction.isPresent()) {
            CommentReaction existingReaction = optionalReaction.get();

            // Validate comment and user if they are being changed.
            if(reactionDetails.getComment() != null && !reactionDetails.getComment().getId().equals(existingReaction.getComment().getId())){
                Optional<Comment> comment = commentRepository.findById(reactionDetails.getComment().getId());
                if(comment.isEmpty()){
                    throw new IllegalArgumentException("Comment with ID " + reactionDetails.getComment().getId()+ " does not exists");
                }
                // Check for duplicate reaction on update
                Optional<CommentReaction> existingReactionNewComment = commentReactionRepository.findOneByCommentIdAndUserId(reactionDetails.getComment().getId(), existingReaction.getUser().getId());
                if(existingReactionNewComment.isPresent()){
                    throw new IllegalArgumentException("User has already reacted to this comment.");
                }
                existingReaction.setComment(comment.get());
            }
            if(reactionDetails.getUser() != null && !reactionDetails.getUser().getId().equals(existingReaction.getUser().getId())){
                Optional<User> user = userRepository.findById(reactionDetails.getUser().getId());
                if(user.isEmpty()){
                    throw new IllegalArgumentException("User with ID " + reactionDetails.getUser().getId() + " does not exists");
                }
                //Check for duplicate reaction on update
                Optional<CommentReaction> existingReactionNewUser = commentReactionRepository.findOneByCommentIdAndUserId(existingReaction.getComment().getId(), reactionDetails.getUser().getId());
                if(existingReactionNewUser.isPresent()){
                    throw new IllegalArgumentException("User has already reacted to this comment.");
                }
                existingReaction.setUser(user.get());
            }

            existingReaction.setReactionType(reactionDetails.getReactionType());
            // You probably wouldn't update createdAt
            return commentReactionRepository.save(existingReaction);
        } else {
            return null; // Or throw an exception
        }
    }

    @Transactional
    public void deleteReaction(Integer id) {
        if(commentReactionRepository.existsById(id)){
            commentReactionRepository.deleteById(id);
        }
        else{
            throw new IllegalArgumentException("Comment reaction with ID " + id + " does not exists");
        }
    }
}