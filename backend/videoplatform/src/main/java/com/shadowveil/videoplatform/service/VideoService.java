package com.shadowveil.videoplatform.service;

import com.shadowveil.videoplatform.entity.Video;
import com.shadowveil.videoplatform.repository.VideoRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class VideoService {

    private final VideoRepository videoRepository;

    public VideoService(VideoRepository videoRepository) {
        this.videoRepository = videoRepository;
    }

    // Retrieve all videos.
    public List<Video> getAllVideos() {
        return videoRepository.findAll();
    }

    // Retrieve a single video by its ID.
    public Optional<Video> getVideoById(Integer id) {
        return videoRepository.findById(id);
    }

    // Create a new video.
    public Video createVideo(Video video) {
        return videoRepository.save(video);
    }

    // Update an existing video.
    public Video updateVideo(Integer id, Video videoDetails) {
        return videoRepository.findById(id).map(video -> {
            video.setUser(videoDetails.getUser());
            video.setTitle(videoDetails.getTitle());
            video.setDescription(videoDetails.getDescription());
            video.setUrl(videoDetails.getUrl());
            video.setThumbnailUrl(videoDetails.getThumbnailUrl());
            video.setDuration(videoDetails.getDuration());
            video.setStatus(videoDetails.getStatus());
            video.setViews(videoDetails.getViews());
            video.setLikes(videoDetails.getLikes());
            video.setDislikes(videoDetails.getDislikes());
            video.setUpdatedAt(videoDetails.getUpdatedAt());
            return videoRepository.save(video);
        }).orElseThrow(() -> new RuntimeException("Video not found with id " + id));
    }

    // Delete a video by its ID.
    public void deleteVideo(Integer id) {
        videoRepository.deleteById(id);
    }

    // Retrieve videos by a given user ID.
    public List<Video> getVideosByUserId(Integer userId) {
        return videoRepository.findByUser_Id(userId);
    }
}
