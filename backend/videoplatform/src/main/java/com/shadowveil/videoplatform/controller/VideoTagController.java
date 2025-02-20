package com.shadowveil.videoplatform.controller;

import com.shadowveil.videoplatform.entity.VideoTag;
import com.shadowveil.videoplatform.entity.VideoTagId;
import com.shadowveil.videoplatform.service.VideoTagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/video-tags")
public class VideoTagController {

    private final VideoTagService videoTagService;

    @Autowired
    public VideoTagController(VideoTagService videoTagService) {
        this.videoTagService = videoTagService;
    }

    @GetMapping
    public ResponseEntity<List<VideoTag>> getAllVideoTags() {
        return new ResponseEntity<>(videoTagService.getAllVideoTags(), HttpStatus.OK);
    }

    // Remove this endpoint.  It's not very useful with a composite key.
    // @GetMapping("/{id}")
    // public ResponseEntity<VideoTag> getVideoTagById(@PathVariable VideoTagId id) { ... }

    @GetMapping("/video/{videoId}")
    public ResponseEntity<List<VideoTag>> getVideoTagsByVideoId(@PathVariable Integer videoId) {
        return new ResponseEntity<>(videoTagService.getVideoTagsByVideoId(videoId), HttpStatus.OK);
    }

    @GetMapping("/tag/{tagId}")
    public ResponseEntity<List<VideoTag>> getVideoTagsByTagId(@PathVariable Integer tagId) {
        return new ResponseEntity<>(videoTagService.getVideoTagsByTagId(tagId), HttpStatus.OK);
    }

    @GetMapping("/video/{videoId}/tag/{tagId}")
    public ResponseEntity<List<VideoTag>> getVideoTagsByVideoIdAndTagId(@PathVariable Integer videoId, @PathVariable Integer tagId) {
        return new ResponseEntity<>(videoTagService.getVideoTagByVideoIdAndTagId(videoId, tagId), HttpStatus.OK);
    }

    // Change this to take videoId and tagId as parameters
    @PostMapping("/video/{videoId}/tag/{tagId}")
    public ResponseEntity<VideoTag> createVideoTag(
            @PathVariable Integer videoId,
            @PathVariable Integer tagId) {
        try {
            VideoTag savedVideoTag = videoTagService.createVideoTag(videoId, tagId); // Pass IDs
            return new ResponseEntity<>(savedVideoTag, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST); // 400 for invalid video/tag or duplicate
        }
    }

    @DeleteMapping("/video/{videoId}/tag/{tagId}")
    public ResponseEntity<Void> deleteVideoTag(
            @PathVariable Integer videoId,
            @PathVariable Integer tagId) {
        try {
            videoTagService.deleteVideoTag(videoId, tagId);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        catch (IllegalArgumentException e){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}