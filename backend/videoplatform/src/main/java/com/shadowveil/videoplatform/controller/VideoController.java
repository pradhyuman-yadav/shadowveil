package com.shadowveil.videoplatform.controller;

import com.shadowveil.videoplatform.dto.VideoDto; // Import VideoDto
import com.shadowveil.videoplatform.entity.Video;
import com.shadowveil.videoplatform.service.VideoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/videos")
public class VideoController {

    private final VideoService videoService;

    @Autowired
    public VideoController(VideoService videoService) {
        this.videoService = videoService;
    }

    @GetMapping
    public ResponseEntity<List<VideoDto.Response>> getAllVideos() {
        List<VideoDto.Response> videos = videoService.getAllVideos();
        return ResponseEntity.ok(videos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<VideoDto.Response> getVideoById(@PathVariable Integer id) {
        return videoService.getVideoById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<VideoDto.Response> createVideo(@Valid @RequestBody VideoDto.Request videoDto) {
        Video createdVideo = videoService.createVideo(videoDto);
        VideoDto.Response responseDto = videoService.convertToDto(createdVideo); // Convert to response DTO
        return new ResponseEntity<>(responseDto, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<VideoDto.Response> updateVideo(@PathVariable Integer id, @Valid @RequestBody VideoDto.Request videoDto) {
        Video updatedVideo = videoService.updateVideo(id, videoDto);
        VideoDto.Response responseDto = videoService.convertToDto(updatedVideo);
        return ResponseEntity.ok(responseDto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteVideo(@PathVariable Integer id) {
        videoService.deleteVideo(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<VideoDto.Response>> getVideosByUserId(@PathVariable Integer userId) {
        List<VideoDto.Response> videos = videoService.getVideosByUserId(userId);
        return ResponseEntity.ok(videos);
    }

    @GetMapping("/module/{moduleId}")
    public ResponseEntity<List<VideoDto.Response>> getVideosByModuleId(@PathVariable Integer moduleId) {
        List<VideoDto.Response> videos = videoService.getVideosByModuleId(moduleId);
        return ResponseEntity.ok(videos);
    }
}