package com.shadowveil.videoplatform.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/live")
class LiveStreamingController {

    @PostMapping("/start")
    public ResponseEntity<String> startLiveStream() {
        return ResponseEntity.ok("Live stream started");
    }

    @PostMapping("/stop")
    public ResponseEntity<String> stopLiveStream() {
        return ResponseEntity.ok("Live stream stopped");
    }

    @GetMapping("/{id}")
    public ResponseEntity<String> getLiveStreamDetails(@PathVariable String id) {
        return ResponseEntity.ok("Details of live stream: " + id);
    }
}
