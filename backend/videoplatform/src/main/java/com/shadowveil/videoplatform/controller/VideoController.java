package com.shadowveil.videoplatform.controller;

import com.shadowveil.videoplatform.entity.Video;
import com.shadowveil.videoplatform.service.VideoService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/videos")
public class VideoController {

    private final VideoService videoService;

    public VideoController(VideoService videoService) {
        this.videoService = videoService;
    }

    // GET /api/videos
    @GetMapping
    public ResponseEntity<List<Video>> getAllVideos() {
        List<Video> videos = videoService.getAllVideos();
        return ResponseEntity.ok(videos);
    }

    // GET /api/videos/{id}
    @GetMapping("/{id}")
    public ResponseEntity<Video> getVideoById(@PathVariable Integer id) {
        return videoService.getVideoById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // POST /api/videos
    @PostMapping
    public ResponseEntity<Video> createVideo(@Valid @RequestBody Video video) {
        Video createdVideo = videoService.createVideo(video);
        return ResponseEntity.ok(createdVideo);
    }

    // PUT /api/videos/{id}
    @PutMapping("/{id}")
    public ResponseEntity<Video> updateVideo(@PathVariable Integer id, @Valid @RequestBody Video video) {
        try {
            Video updatedVideo = videoService.updateVideo(id, video);
            return ResponseEntity.ok(updatedVideo);
        } catch (RuntimeException ex) {
            return ResponseEntity.notFound().build();
        }
    }

    // DELETE /api/videos/{id}
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteVideo(@PathVariable Integer id) {
        videoService.deleteVideo(id);
        return ResponseEntity.noContent().build();
    }

    // GET /api/videos/user/{userId}
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Video>> getVideosByUserId(@PathVariable Integer userId) {
        List<Video> videos = videoService.getVideosByUserId(userId);
        return ResponseEntity.ok(videos);
    }
}
