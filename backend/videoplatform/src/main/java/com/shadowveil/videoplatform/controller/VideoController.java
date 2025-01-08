package com.shadowveil.videoplatform.controller;

import com.shadowveil.videoplatform.entity.Video;
import com.shadowveil.videoplatform.service.VideoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST Controller for Video entity
 */
@RestController
@RequestMapping("/api/videos")
public class VideoController {

    private final VideoService videoService;

    public VideoController(VideoService videoService) {
        this.videoService = videoService;
    }

    /**
     * GET /api/videos
     *
     * @return a list of all videos
     */
    @GetMapping
    public List<Video> getAllVideos() {
        return videoService.findAllVideos();
    }

    /**
     * GET /api/videos/{id}
     *
     * @param id the ID of the video
     * @return the Video with the given ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<Video> getVideoById(@PathVariable Long id) {
        Video video = videoService.findVideoById(id);
        return ResponseEntity.ok(video);
    }

    /**
     * GET /api/videos/course/{courseId}
     *
     * @param courseId the ID of the course
     * @return a list of videos for the given course
     */
    @GetMapping("/course/{courseId}")
    public List<Video> getVideosByCourseId(@PathVariable Long courseId) {
        return videoService.findVideosByCourseId(courseId);
    }

    /**
     * POST /api/videos
     *
     * @param video the Video to create
     * @return the created Video
     */
    @PostMapping
    public ResponseEntity<Video> createVideo(@RequestBody Video video) {
        Video createdVideo = videoService.createVideo(video);
        return ResponseEntity.ok(createdVideo);
    }

    /**
     * PUT /api/videos/{id}
     *
     * @param id    the ID of the video to update
     * @param video the updated video data
     * @return the updated Video
     */
    @PutMapping("/{id}")
    public ResponseEntity<Video> updateVideo(
            @PathVariable Long id,
            @RequestBody Video video
    ) {
        Video updatedVideo = videoService.updateVideo(id, video);
        return ResponseEntity.ok(updatedVideo);
    }

    /**
     * DELETE /api/videos/{id}
     *
     * @param id the ID of the video to delete
     * @return a no-content response
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteVideo(@PathVariable Long id) {
        videoService.deleteVideo(id);
        return ResponseEntity.noContent().build();
    }
}