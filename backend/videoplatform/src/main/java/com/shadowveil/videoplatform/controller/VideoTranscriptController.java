// File: src/main/java/com/shadowveil/videoplatform/controller/VideoTranscriptController.java
package com.shadowveil.videoplatform.controller;

import com.shadowveil.videoplatform.entity.VideoTranscript;
import com.shadowveil.videoplatform.service.VideoTranscriptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/video-transcripts")
public class VideoTranscriptController {

    private final VideoTranscriptService videoTranscriptService;

    @Autowired
    public VideoTranscriptController(VideoTranscriptService videoTranscriptService) {
        this.videoTranscriptService = videoTranscriptService;
    }

    @GetMapping
    public ResponseEntity<List<VideoTranscript>> getAllTranscripts() {
        return new ResponseEntity<>(videoTranscriptService.getAllTranscripts(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<VideoTranscript> getTranscriptById(@PathVariable Integer id) {
        Optional<VideoTranscript> transcript = videoTranscriptService.getTranscriptById(id);
        return transcript.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping("/video/{videoId}")
    public ResponseEntity<List<VideoTranscript>> getTranscriptsByVideoId(@PathVariable Integer videoId) {
        return new ResponseEntity<>(videoTranscriptService.getTranscriptsByVideoId(videoId), HttpStatus.OK);
    }

    @GetMapping("/video/{videoId}/language/{language}")
    public ResponseEntity<List<VideoTranscript>> getTranscriptByVideoIdAndLanguage(
            @PathVariable Integer videoId,
            @PathVariable String language) {
        return new ResponseEntity<>(videoTranscriptService.getTranscriptsByVideoIdAndLanguage(videoId, language), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<VideoTranscript> createTranscript(@RequestBody VideoTranscript transcript) {
        try {
            VideoTranscript savedTranscript = videoTranscriptService.createTranscript(transcript);
            return new ResponseEntity<>(savedTranscript, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST); // 400 for invalid video
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<VideoTranscript> updateTranscript(@PathVariable Integer id, @RequestBody VideoTranscript transcriptDetails) {
        try {
            VideoTranscript updatedTranscript = videoTranscriptService.updateTranscript(id, transcriptDetails);
            return updatedTranscript != null ? new ResponseEntity<>(updatedTranscript, HttpStatus.OK)
                    : new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTranscript(@PathVariable Integer id) {
        try{
            videoTranscriptService.deleteTranscript(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        catch (IllegalArgumentException e){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}