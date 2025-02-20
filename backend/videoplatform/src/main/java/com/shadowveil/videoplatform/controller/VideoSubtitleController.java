// File: src/main/java/com/shadowveil/videoplatform/controller/VideoSubtitleController.java
package com.shadowveil.videoplatform.controller;

import com.shadowveil.videoplatform.entity.VideoSubtitle;
import com.shadowveil.videoplatform.service.VideoSubtitleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/video-subtitles")
public class VideoSubtitleController {

    private final VideoSubtitleService videoSubtitleService;

    @Autowired
    public VideoSubtitleController(VideoSubtitleService videoSubtitleService) {
        this.videoSubtitleService = videoSubtitleService;
    }

    @GetMapping
    public ResponseEntity<List<VideoSubtitle>> getAllSubtitles() {
        return new ResponseEntity<>(videoSubtitleService.getAllSubtitles(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<VideoSubtitle> getSubtitleById(@PathVariable Integer id) {
        Optional<VideoSubtitle> subtitle = videoSubtitleService.getSubtitleById(id);
        return subtitle.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping("/video/{videoId}")
    public ResponseEntity<List<VideoSubtitle>> getSubtitlesByVideoId(@PathVariable Integer videoId) {
        return new ResponseEntity<>(videoSubtitleService.getSubtitlesByVideoId(videoId), HttpStatus.OK);
    }

    @GetMapping("/video/{videoId}/language/{language}")
    public ResponseEntity<List<VideoSubtitle>> getSubtitlesByVideoIdAndLanguage(
            @PathVariable Integer videoId,
            @PathVariable String language) {
        return new ResponseEntity<>(videoSubtitleService.getSubtitlesByVideoIdAndLanguage(videoId, language), HttpStatus.OK);
    }


    @PostMapping
    public ResponseEntity<VideoSubtitle> createSubtitle(@RequestBody VideoSubtitle subtitle) {
        try {
            VideoSubtitle savedSubtitle = videoSubtitleService.createSubtitle(subtitle);
            return new ResponseEntity<>(savedSubtitle, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST); // 400 for invalid video
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<VideoSubtitle> updateSubtitle(@PathVariable Integer id, @RequestBody VideoSubtitle subtitleDetails) {
        try {
            VideoSubtitle updatedSubtitle = videoSubtitleService.updateSubtitle(id, subtitleDetails);
            return updatedSubtitle != null ? new ResponseEntity<>(updatedSubtitle, HttpStatus.OK)
                    : new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSubtitle(@PathVariable Integer id) {
        try{
            videoSubtitleService.deleteSubtitle(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        catch (IllegalArgumentException e){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}