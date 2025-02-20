package com.shadowveil.videoplatform.service;

import com.shadowveil.videoplatform.dto.CommentDto;
import com.shadowveil.videoplatform.dto.UserDto;
import com.shadowveil.videoplatform.dto.VideoDto;
import com.shadowveil.videoplatform.entity.Comment;
import com.shadowveil.videoplatform.entity.User;
import com.shadowveil.videoplatform.entity.Video;
import com.shadowveil.videoplatform.exception.ResourceNotFoundException;
import com.shadowveil.videoplatform.repository.CommentRepository;
import com.shadowveil.videoplatform.repository.UserRepository;
import com.shadowveil.videoplatform.repository.VideoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CommentService {

    private final CommentRepository commentRepository;
    private final VideoRepository videoRepository; // Inject
    private final UserRepository userRepository;    // Inject
    private final VideoService videoService; // Inject for DTO conversion
    private final UserService userService;

    @Autowired
    public CommentService(CommentRepository commentRepository, VideoRepository videoRepository,
                          UserRepository userRepository, VideoService videoService, UserService userService) {
        this.commentRepository = commentRepository;
        this.videoRepository = videoRepository;
        this.userRepository = userRepository;
        this.videoService = videoService;
        this.userService = userService;
    }

    @Transactional(readOnly = true)
    public List<CommentDto.Response> getAllComments() {
        return commentRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Optional<CommentDto.Response> getCommentById(Integer id) {
        return commentRepository.findById(id)
                .map(this::convertToDto);
    }

    @Transactional(readOnly = true)
    public List<CommentDto.Response> getCommentsByVideoId(Integer videoId) {
        return commentRepository.findByVideo_Id(videoId).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<CommentDto.Response> getCommentsByUserId(Integer userId) {
        return commentRepository.findByUser_Id(userId).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<CommentDto.Response> getChildComments(Integer parentCommentId) {
        return commentRepository.findByParentComment_Id(parentCommentId).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public Comment createComment(CommentDto.Request commentDto) {
        Video video = null;
        if(commentDto.videoId() != null){
            video = videoRepository.findById(commentDto.videoId())
                    .orElseThrow(() -> new ResourceNotFoundException("Video with ID " + commentDto.videoId() + " not found."));
        }

        User user = null;
        if(commentDto.userId() != null){
            user = userRepository.findById(commentDto.userId())
                    .orElseThrow(() -> new ResourceNotFoundException("User with ID " + commentDto.userId() + " not found."));
        }
        Comment parent = null;
        if(commentDto.parentCommentId() != null){
            parent = commentRepository.findById(commentDto.parentCommentId())
                    .orElseThrow(()-> new ResourceNotFoundException("Comment with ID " + commentDto.parentCommentId() + " not found"));
        }

        Comment comment = new Comment();
        comment.setVideo(video);
        comment.setUser(user);
        comment.setContent(commentDto.content());
        comment.setParentComment(parent); // Can be null
        comment.setCreatedAt(Instant.now());
        comment.setUpdatedAt(Instant.now());
        return commentRepository.save(comment);
    }

    @Transactional
    public Comment updateComment(Integer id, CommentDto.Request commentDto) {
        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Comment not found with id " + id));

        // Update Video (if provided and different)
        if (commentDto.videoId() != null && (comment.getVideo() == null || !comment.getVideo().getId().equals(commentDto.videoId()))) {
            Video newVideo = videoRepository.findById(commentDto.videoId())
                    .orElseThrow(() -> new ResourceNotFoundException("Video with ID " + commentDto.videoId() + " not found."));
            comment.setVideo(newVideo);
        }
        // Update User
        if (commentDto.userId() != null && (comment.getUser() == null || !comment.getUser().getId().equals(commentDto.userId()))) {
            User newUser = userRepository.findById(commentDto.userId())
                    .orElseThrow(() -> new ResourceNotFoundException("User with ID " + commentDto.userId() + " not found"));
            comment.setUser(newUser);
        }

        // Update Parent Comment
        if(commentDto.parentCommentId() != null && (comment.getParentComment() == null || !comment.getParentComment().getId().equals(commentDto.parentCommentId()))){
            Comment parentComment = commentRepository.findById(commentDto.parentCommentId())
                    .orElseThrow(()-> new ResourceNotFoundException("Comment with ID: " + commentDto.parentCommentId() + " not found"));
            comment.setParentComment(parentComment);
        }

        if(commentDto.content() != null) comment.setContent(commentDto.content()); // Update content
        comment.setUpdatedAt(Instant.now()); // Update the timestamp
        return commentRepository.save(comment);
    }

    @Transactional
    public void deleteComment(Integer id) {
        if (!commentRepository.existsById(id)) {
            throw new ResourceNotFoundException("Comment with ID " + id + " not found");
        }
        commentRepository.deleteById(id);
    }


    public CommentDto.Response convertToDto(Comment comment) {
        VideoDto.Response videoDto = null;
        if(comment.getVideo() != null){
            videoDto = videoService.convertToDto(comment.getVideo());
        }

        UserDto.Response userDto = null;
        if(comment.getUser() != null){
            userDto = userService.convertToDto(comment.getUser());
        }

        // Recursively convert replies
        List<CommentDto.Response> replyDtos = commentRepository.findByParentComment_Id(comment.getId())
                .stream()
                .map(this::convertToDto) // Recursive call
                .collect(Collectors.toList());


        return new CommentDto.Response(
                comment.getId(),
                videoDto,     // Use VideoService
                userDto,   // Use UserService
                comment.getContent(),
                comment.getParentComment() != null ? comment.getParentComment().getId() : null, // Get parent ID
                comment.getCreatedAt(),
                comment.getUpdatedAt(),
                replyDtos  // Include the replies
        );
    }
}