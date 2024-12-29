package com.shadowveil.videoplatform.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
class AdminController {

    @GetMapping("/users")
    public ResponseEntity<List<String>> getAllUsers() {
        return ResponseEntity.ok(List.of("User1", "User2"));
    }

    @GetMapping("/videos")
    public ResponseEntity<List<String>> getAllVideos() {
        return ResponseEntity.ok(List.of("Admin Video1", "Admin Video2"));
    }

    @DeleteMapping("/users/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable String id) {
        return ResponseEntity.ok("User deleted: " + id);
    }

    @DeleteMapping("/videos/{id}")
    public ResponseEntity<String> deleteVideo(@PathVariable String id) {
        return ResponseEntity.ok("Video deleted: " + id);
    }
}
