// File: src/main/java/com/shadowveil/videoplatform/controller/VideoReviewController.java
package com.shadowveil.videoplatform.controller;

import com.shadowveil.videoplatform.entity.VideoReview;
import com.shadowveil.videoplatform.service.VideoReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/video-reviews")
public class VideoReviewController {

    private final VideoReviewService videoReviewService;

    @Autowired
    public VideoReviewController(VideoReviewService videoReviewService) {
        this.videoReviewService = videoReviewService;
    }

    @GetMapping
    public ResponseEntity<List<VideoReview>> getAllReviews() {
        return new ResponseEntity<>(videoReviewService.getAllReviews(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<VideoReview> getReviewById(@PathVariable Integer id) {
        Optional<VideoReview> review = videoReviewService.getReviewById(id);
        return review.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping("/video/{videoId}")
    public ResponseEntity<List<VideoReview>> getReviewsByVideoId(@PathVariable Integer videoId) {
        return new ResponseEntity<>(videoReviewService.getReviewsByVideoId(videoId), HttpStatus.OK);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<VideoReview>> getReviewsByUserId(@PathVariable Integer userId) {
        return new ResponseEntity<>(videoReviewService.getReviewsByUserId(userId), HttpStatus.OK);
    }
    @GetMapping("/video/{videoId}/user/{userId}")
    public ResponseEntity<List<VideoReview>> getReviewsByVideoAndUser(
            @PathVariable Integer videoId,
            @PathVariable Integer userId) {
        return new ResponseEntity<>(videoReviewService.getReviewsByVideoIdAndUserId(videoId, userId), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<VideoReview> createReview(@RequestBody VideoReview review) {
        try {
            VideoReview savedReview = videoReviewService.createReview(review);
            return new ResponseEntity<>(savedReview, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST); // 400 for invalid user/video or duplicate
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<VideoReview> updateReview(@PathVariable Integer id, @RequestBody VideoReview reviewDetails) {
        try {
            VideoReview updatedReview = videoReviewService.updateReview(id, reviewDetails);
            return updatedReview != null ? new ResponseEntity<>(updatedReview, HttpStatus.OK)
                    : new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReview(@PathVariable Integer id) {
        try{
            videoReviewService.deleteReview(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        catch (IllegalArgumentException e){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

    }
}