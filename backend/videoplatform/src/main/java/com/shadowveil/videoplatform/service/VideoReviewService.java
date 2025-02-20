// File: src/main/java/com/shadowveil/videoplatform/service/VideoReviewService.java
package com.shadowveil.videoplatform.service;

import com.shadowveil.videoplatform.entity.User;
import com.shadowveil.videoplatform.entity.Video;
import com.shadowveil.videoplatform.entity.VideoReview;
import com.shadowveil.videoplatform.repository.VideoReviewRepository;
import com.shadowveil.videoplatform.repository.UserRepository;
import com.shadowveil.videoplatform.repository.VideoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Service
public class VideoReviewService {

    private final VideoReviewRepository videoReviewRepository;
    private final VideoRepository videoRepository;
    private final UserRepository userRepository;

    @Autowired
    public VideoReviewService(
            VideoReviewRepository videoReviewRepository,
            VideoRepository videoRepository,
            UserRepository userRepository) {
        this.videoReviewRepository = videoReviewRepository;
        this.videoRepository = videoRepository;
        this.userRepository = userRepository;
    }

    public List<VideoReview> getAllReviews() {
        return videoReviewRepository.findAll();
    }

    public Optional<VideoReview> getReviewById(Integer id) {
        return videoReviewRepository.findById(id);
    }

    public List<VideoReview> getReviewsByVideoId(Integer videoId) {
        return videoReviewRepository.findByVideoId(videoId);
    }

    public List<VideoReview> getReviewsByUserId(Integer userId) {
        return videoReviewRepository.findByUserId(userId);
    }
    public List<VideoReview> getReviewsByVideoIdAndUserId(Integer videoId, Integer userId){
        return videoReviewRepository.findByVideoIdAndUserId(videoId, userId);
    }

    @Transactional
    public VideoReview createReview(VideoReview review) {
        // Validate user and video
        Optional<User> user = userRepository.findById(review.getUser().getId());
        if (user.isEmpty()) {
            throw new IllegalArgumentException("User with ID " + review.getUser().getId() + " not found.");
        }
        Optional<Video> video = videoRepository.findById(review.getVideo().getId());
        if (video.isEmpty()) {
            throw new IllegalArgumentException("Video with ID " + review.getVideo().getId() + " not found.");
        }

        // Prevent duplicate reviews
        Optional<VideoReview> existingReview = videoReviewRepository.findOneByVideoIdAndUserId(
                review.getVideo().getId(), review.getUser().getId());
        if (existingReview.isPresent()) {
            throw new IllegalArgumentException("User has already reviewed this video.");
        }

        review.setUser(user.get());
        review.setVideo(video.get());
        review.setUpdatedAt(Instant.now());
        return videoReviewRepository.save(review);
    }

    @Transactional
    public VideoReview updateReview(Integer id, VideoReview reviewDetails) {
        Optional<VideoReview> optionalReview = videoReviewRepository.findById(id);
        if (optionalReview.isPresent()) {
            VideoReview existingReview = optionalReview.get();

            // Validate user and video if they are being changed
            if(reviewDetails.getUser() != null && !reviewDetails.getUser().getId().equals(existingReview.getUser().getId())){
                Optional<User> user = userRepository.findById(reviewDetails.getUser().getId());
                if(user.isEmpty()){
                    throw new IllegalArgumentException("User with ID " + reviewDetails.getUser().getId() + " does not exists");
                }
                // Check for duplicate on update
                Optional<VideoReview> existingReviewForNewUser = videoReviewRepository.findOneByVideoIdAndUserId(existingReview.getVideo().getId(), reviewDetails.getUser().getId());
                if(existingReviewForNewUser.isPresent()){
                    throw new IllegalArgumentException("User has already reviewed this video.");
                }
                existingReview.setUser(user.get());
            }
            if(reviewDetails.getVideo() != null && !reviewDetails.getVideo().getId().equals(existingReview.getVideo().getId())){
                Optional<Video> video = videoRepository.findById(reviewDetails.getVideo().getId());
                if(video.isEmpty()){
                    throw new IllegalArgumentException("Video with ID " + reviewDetails.getVideo().getId() + " does not exists");
                }
                // Check for duplicate on update
                Optional<VideoReview> existingReviewForNewVideo = videoReviewRepository.findOneByVideoIdAndUserId(reviewDetails.getVideo().getId(), existingReview.getUser().getId());
                if(existingReviewForNewVideo.isPresent()){
                    throw  new IllegalArgumentException("User has already reviewed this video.");
                }
                existingReview.setVideo(video.get());
            }
            existingReview.setRating(reviewDetails.getRating());
            existingReview.setReviewText(reviewDetails.getReviewText());
            existingReview.setUpdatedAt(Instant.now()); // Update the timestamp
            return videoReviewRepository.save(existingReview);
        } else {
            return null; // Or throw an exception
        }
    }

    @Transactional
    public void deleteReview(Integer id) {
        if(videoReviewRepository.existsById(id)){
            videoReviewRepository.deleteById(id);
        }
        else {
            throw new IllegalArgumentException("Video review with ID " + id + " does not exists");
        }
    }
}