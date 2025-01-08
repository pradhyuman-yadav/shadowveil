package com.shadowveil.videoplatform.service;

import com.shadowveil.videoplatform.entity.Video;
import com.shadowveil.videoplatform.repository.VideoRepository;
import org.springframework.stereotype.Service;
import java.util.List;

/**
 * Service class for Video entity
 */
@Service
public class VideoService {

    private final VideoRepository videoRepository;

    public VideoService(VideoRepository videoRepository) {
        this.videoRepository = videoRepository;
    }

    /**
     * Retrieve all videos
     *
     * @return a list of all videos
     */
    public List<Video> findAllVideos() {
        return videoRepository.findAll();
    }

    /**
     * Retrieve a video by ID
     *
     * @param id the ID of the video
     * @return the Video if found
     * @throws RuntimeException if no video is found
     */
    public Video findVideoById(Long id) {
        return videoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Video not found with id: " + id));
    }

    /**
     * Retrieve all videos for a specific course
     *
     * @param courseId the ID of the course
     * @return a list of videos for the course
     */
    public List<Video> findVideosByCourseId(Long courseId) {
        return videoRepository.findByCourseId(courseId);
    }

    /**
     * Create a new video
     *
     * @param video the video to create
     * @return the saved video
     */
    public Video createVideo(Video video) {
        return videoRepository.save(video);
    }

    /**
     * Update an existing video
     *
     * @param id    the ID of the video to update
     * @param video the updated video data
     * @return the updated video
     */
    public Video updateVideo(Long id, Video video) {
        Video existingVideo = findVideoById(id);
        existingVideo.setTitle(video.getTitle());
        existingVideo.setVideoUrl(video.getVideoUrl());
        existingVideo.setDuration(video.getDuration());
        existingVideo.setPosition(video.getPosition());
        existingVideo.setCourseId(video.getCourseId());
        return videoRepository.save(existingVideo);
    }

    /**
     * Delete a video by its ID
     *
     * @param id the ID of the video to delete
     */
    public void deleteVideo(Long id) {
        Video video = findVideoById(id);
        videoRepository.delete(video);
    }
}

