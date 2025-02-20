// File: src/main/java/com/shadowveil/videoplatform/controller/UserWatchHistoryController.java
package com.shadowveil.videoplatform.controller;

import com.shadowveil.videoplatform.entity.UserWatchHistory;
import com.shadowveil.videoplatform.service.UserWatchHistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/watch-history") // Good, descriptive path
public class UserWatchHistoryController {

    private final UserWatchHistoryService userWatchHistoryService;

    @Autowired
    public UserWatchHistoryController(UserWatchHistoryService userWatchHistoryService) {
        this.userWatchHistoryService = userWatchHistoryService;
    }

    @GetMapping
    public ResponseEntity<List<UserWatchHistory>> getAllWatchHistory() {
        return new ResponseEntity<>(userWatchHistoryService.getAllWatchHistory(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserWatchHistory> getWatchHistoryById(@PathVariable Integer id) {
        Optional<UserWatchHistory> historyEntry = userWatchHistoryService.getWatchHistoryById(id);
        return historyEntry.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<UserWatchHistory>> getWatchHistoryByUserId(@PathVariable Integer userId) {
        return new ResponseEntity<>(userWatchHistoryService.getWatchHistoryByUserId(userId), HttpStatus.OK);
    }

    @GetMapping("/video/{videoId}")
    public ResponseEntity<List<UserWatchHistory>> getWatchHistoryByVideoId(@PathVariable Integer videoId) {
        return new ResponseEntity<>(userWatchHistoryService.getWatchHistoryByVideoId(videoId), HttpStatus.OK);
    }

    @GetMapping("/user/{userId}/video/{videoId}")
    public ResponseEntity<List<UserWatchHistory>> getWatchHistoryByUserAndVideo(
            @PathVariable Integer userId,
            @PathVariable Integer videoId) {
        return new ResponseEntity<>(userWatchHistoryService.getWatchHistoryByUserIdAndVideoId(userId, videoId), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<UserWatchHistory> createWatchHistoryEntry(@RequestBody UserWatchHistory historyEntry) {
        try {
            UserWatchHistory savedEntry = userWatchHistoryService.createWatchHistoryEntry(historyEntry);
            return new ResponseEntity<>(savedEntry, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST); // 400 for invalid user/video
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserWatchHistory> updateWatchHistoryEntry(@PathVariable Integer id, @RequestBody UserWatchHistory historyDetails) {
        try {
            UserWatchHistory updatedEntry = userWatchHistoryService.updateWatchHistoryEntry(id, historyDetails);
            return updatedEntry!=null ? new ResponseEntity<>(updatedEntry, HttpStatus.OK) : new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteWatchHistoryEntry(@PathVariable Integer id) {
        try{
            userWatchHistoryService.deleteWatchHistoryEntry(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        catch (IllegalArgumentException e){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}