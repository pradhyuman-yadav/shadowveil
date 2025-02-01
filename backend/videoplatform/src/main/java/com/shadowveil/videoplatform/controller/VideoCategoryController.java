package com.shadowveil.videoplatform.controller;

import com.shadowveil.videoplatform.entity.VideoCategory;
import com.shadowveil.videoplatform.entity.VideoCategoryId;
import com.shadowveil.videoplatform.service.VideoCategoryService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/video-categories")
public class VideoCategoryController {

    private final VideoCategoryService videoCategoryService;

    public VideoCategoryController(VideoCategoryService videoCategoryService) {
        this.videoCategoryService = videoCategoryService;
    }

    // GET /api/video-categories
    @GetMapping
    public ResponseEntity<List<VideoCategory>> getAllVideoCategories() {
        List<VideoCategory> videoCategories = videoCategoryService.getAllVideoCategories();
        return ResponseEntity.ok(videoCategories);
    }

    // GET /api/video-categories/{videoId}/{categoryId}
    @GetMapping("/{videoId}/{categoryId}")
    public ResponseEntity<VideoCategory> getVideoCategoryById(
            @PathVariable Integer videoId,
            @PathVariable Integer categoryId) {
        VideoCategoryId id = new VideoCategoryId();
        id.setVideoId(videoId);
        id.setCategoryId(categoryId);
        return videoCategoryService.getVideoCategoryById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // POST /api/video-categories
    @PostMapping
    public ResponseEntity<VideoCategory> createVideoCategory(@Valid @RequestBody VideoCategory videoCategory) {
        VideoCategory createdVideoCategory = videoCategoryService.createVideoCategory(videoCategory);
        return ResponseEntity.ok(createdVideoCategory);
    }

    // PUT /api/video-categories/{videoId}/{categoryId}
    @PutMapping("/{videoId}/{categoryId}")
    public ResponseEntity<VideoCategory> updateVideoCategory(
            @PathVariable Integer videoId,
            @PathVariable Integer categoryId,
            @Valid @RequestBody VideoCategory videoCategory) {
        VideoCategoryId id = new VideoCategoryId();
        id.setVideoId(videoId);
        id.setCategoryId(categoryId);
        try {
            VideoCategory updatedVideoCategory = videoCategoryService.updateVideoCategory(id, videoCategory);
            return ResponseEntity.ok(updatedVideoCategory);
        } catch (RuntimeException ex) {
            return ResponseEntity.notFound().build();
        }
    }

    // DELETE /api/video-categories/{videoId}/{categoryId}
    @DeleteMapping("/{videoId}/{categoryId}")
    public ResponseEntity<Void> deleteVideoCategory(
            @PathVariable Integer videoId,
            @PathVariable Integer categoryId) {
        VideoCategoryId id = new VideoCategoryId();
        id.setVideoId(videoId);
        id.setCategoryId(categoryId);
        videoCategoryService.deleteVideoCategory(id);
        return ResponseEntity.noContent().build();
    }

    // GET /api/video-categories/video/{videoId}
    @GetMapping("/video/{videoId}")
    public ResponseEntity<List<VideoCategory>> getVideoCategoriesByVideoId(@PathVariable Integer videoId) {
        List<VideoCategory> videoCategories = videoCategoryService.getVideoCategoriesByVideoId(videoId);
        return ResponseEntity.ok(videoCategories);
    }

    // GET /api/video-categories/category/{categoryId}
    @GetMapping("/category/{categoryId}")
    public ResponseEntity<List<VideoCategory>> getVideoCategoriesByCategoryId(@PathVariable Integer categoryId) {
        List<VideoCategory> videoCategories = videoCategoryService.getVideoCategoriesByCategoryId(categoryId);
        return ResponseEntity.ok(videoCategories);
    }
}
