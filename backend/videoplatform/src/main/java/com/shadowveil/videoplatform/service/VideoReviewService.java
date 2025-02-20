package com.shadowveil.videoplatform.service;

import com.shadowveil.videoplatform.dto.VideoReviewDto;
import com.shadowveil.videoplatform.entity.User;
import com.shadowveil.videoplatform.entity.Video;
import com.shadowveil.videoplatform.entity.VideoReview;
import com.shadowveil.videoplatform.repository.VideoReviewRepository;
import com.shadowveil.videoplatform.repository.UserRepository;
import com.shadowveil.videoplatform.repository.VideoRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    public VideoReview getReviewById(Integer id) {
        return videoReviewRepository.findById(id).orElseThrow(()-> new EntityNotFoundException("Video review not found with ID: "+ id));
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
    public VideoReview createReview(VideoReviewDto.Request requestDto) {
        // Validate user and video
        User user = userRepository.findById(requestDto.userId())
                .orElseThrow(() -> new EntityNotFoundException("User with ID " + requestDto.userId() + " not found."));
        Video video = videoRepository.findById(requestDto.videoId())
                .orElseThrow(() -> new EntityNotFoundException("Video with ID " + requestDto.videoId() + " not found."));

        // Prevent duplicate reviews
        if (videoReviewRepository.findOneByVideoIdAndUserId(requestDto.videoId(), requestDto.userId()).isPresent()) {
            throw new DataIntegrityViolationException("User has already reviewed this video.");
        }

        VideoReview review = new VideoReview();
        review.setUser(user);
        review.setVideo(video);
        review.setRating(requestDto.rating());
        review.setReviewText(requestDto.reviewText());

        return videoReviewRepository.save(review);
    }

    @Transactional
    public VideoReview updateReview(Integer id, VideoReviewDto.UpdateRequest requestDto) {
        VideoReview existingReview = videoReviewRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Review not found with id: " + id));

        // Update fields
        existingReview.setRating(requestDto.rating());
        existingReview.setReviewText(requestDto.reviewText());
        // updatedAt is handled automatically

        return videoReviewRepository.save(existingReview);

    }

    @Transactional
    public void deleteReview(Integer id) {
        VideoReview videoReview = videoReviewRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Video review not found with id: " + id));
        videoReviewRepository.delete(videoReview);
    }
}