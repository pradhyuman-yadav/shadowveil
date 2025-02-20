package com.shadowveil.videoplatform.controller;

import com.shadowveil.videoplatform.dto.UserDto;
import com.shadowveil.videoplatform.dto.VideoDto;
import com.shadowveil.videoplatform.dto.VideoReviewDto;
import com.shadowveil.videoplatform.dto.ModuleDto;
import com.shadowveil.videoplatform.entity.VideoReview;
import com.shadowveil.videoplatform.service.VideoReviewService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/video-reviews")
public class VideoReviewController {

    private final VideoReviewService videoReviewService;

    @Autowired
    public VideoReviewController(VideoReviewService videoReviewService) {
        this.videoReviewService = videoReviewService;
    }

    @GetMapping
    public ResponseEntity<List<VideoReviewDto.Response>> getAllReviews() {
        List<VideoReview> reviews = videoReviewService.getAllReviews();
        List<VideoReviewDto.Response> responseDtos = reviews.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
        return new ResponseEntity<>(responseDtos, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<VideoReviewDto.Response> getReviewById(@PathVariable Integer id) {
        VideoReview review = videoReviewService.getReviewById(id);
        return new ResponseEntity<>(convertToDto(review), HttpStatus.OK);
    }

    @GetMapping("/video/{videoId}")
    public ResponseEntity<List<VideoReviewDto.Response>> getReviewsByVideoId(@PathVariable Integer videoId) {
        List<VideoReview> reviews = videoReviewService.getReviewsByVideoId(videoId);
        List<VideoReviewDto.Response> responseDtos = reviews.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
        return new ResponseEntity<>(responseDtos, HttpStatus.OK);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<VideoReviewDto.Response>> getReviewsByUserId(@PathVariable Integer userId) {
        List<VideoReview> reviews = videoReviewService.getReviewsByUserId(userId);
        List<VideoReviewDto.Response> responseDtos = reviews.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
        return new ResponseEntity<>(responseDtos, HttpStatus.OK);
    }
    @GetMapping("/video/{videoId}/user/{userId}")
    public ResponseEntity<List<VideoReviewDto.Response>> getReviewsByVideoAndUser(
            @PathVariable Integer videoId,
            @PathVariable Integer userId) {
        List<VideoReview> reviews = videoReviewService.getReviewsByVideoIdAndUserId(videoId, userId);
        List<VideoReviewDto.Response> responseDtos = reviews.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
        return new ResponseEntity<>(responseDtos, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<VideoReviewDto.Response> createReview(@Valid @RequestBody VideoReviewDto.Request requestDto) {
        VideoReview savedReview = videoReviewService.createReview(requestDto);
        return new ResponseEntity<>(convertToDto(savedReview), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<VideoReviewDto.Response> updateReview(
            @PathVariable Integer id,
            @Valid @RequestBody VideoReviewDto.UpdateRequest requestDto) { // Use UpdateRequest DTO
        VideoReview updatedReview = videoReviewService.updateReview(id, requestDto);
        return new ResponseEntity<>(convertToDto(updatedReview), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReview(@PathVariable Integer id) {
        videoReviewService.deleteReview(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    private VideoReviewDto.Response convertToDto(VideoReview review) {
        // Create nested DTOs
        VideoDto.Response videoDto = new VideoDto.Response(
                review.getVideo().getId(),
                (review.getVideo().getUser() != null) ? new UserDto.Response(
                        review.getVideo().getUser().getId(),
                        review.getVideo().getUser().getUsername(),
                        review.getVideo().getUser().getEmail(),
                        review.getVideo().getUser().getRole(),
                        review.getVideo().getUser().getCreatedAt(),
                        review.getVideo().getUser().getUpdatedAt()
                ) : null,
                review.getVideo().getTitle(),
                review.getVideo().getDescription(),
                review.getVideo().getUrl(),
                review.getVideo().getThumbnailUrl(),
                review.getVideo().getDuration(),
                review.getVideo().getStatus(),
                review.getVideo().getViews(),
                review.getVideo().getLikes(),
                review.getVideo().getDislikes(),
                review.getVideo().getCreatedAt(),
                review.getVideo().getUpdatedAt(),
                (review.getVideo().getModule() != null) ? new ModuleDto.Response( // Handle null module
                        review.getVideo().getModule().getId(),
                        null, // No CourseDto needed
                        review.getVideo().getModule().getTitle(),
                        review.getVideo().getModule().getDescription(),
                        review.getVideo().getModule().getPosition(),
                        review.getVideo().getModule().getCreatedAt(),
                        review.getVideo().getModule().getUpdatedAt()
                ) : null
        );

        UserDto.Response userDto = new UserDto.Response(
                review.getUser().getId(),
                review.getUser().getUsername(),
                review.getUser().getEmail(),
                review.getUser().getRole(),
                review.getUser().getCreatedAt(),
                review.getUser().getUpdatedAt()
        );


        return new VideoReviewDto.Response(
                review.getId(),
                videoDto,
                userDto,
                review.getRating(),
                review.getReviewText(),
                review.getCreatedAt(),
                review.getUpdatedAt()
        );
    }
    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<String> handleEntityNotFoundException(EntityNotFoundException ex){
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
    }
    @ExceptionHandler(IllegalArgumentException.class)
    public  ResponseEntity<String> handleIllegalArgumentException(IllegalArgumentException ex){
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<String> handleDataIntegrityViolationException(DataIntegrityViolationException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.CONFLICT); // 409 Conflict
    }
}