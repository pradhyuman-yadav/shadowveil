package com.shadowveil.videoplatform.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

public class temp {
    @RestController
    @RequestMapping("/api/videos")
    public class VideoController {
        @GetMapping
        public ResponseEntity<List<String>> getVideos() {
            return ResponseEntity.ok(List.of("Video1", "Video2"));
        }
    }

}
