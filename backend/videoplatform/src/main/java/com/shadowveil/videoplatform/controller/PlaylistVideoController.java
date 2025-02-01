package com.shadowveil.videoplatform.controller;

import com.shadowveil.videoplatform.entity.PlaylistVideo;
import com.shadowveil.videoplatform.entity.PlaylistVideoId;
import com.shadowveil.videoplatform.service.PlaylistVideoService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/playlist-videos")
public class PlaylistVideoController {

    private final PlaylistVideoService playlistVideoService;

    public PlaylistVideoController(PlaylistVideoService playlistVideoService) {
        this.playlistVideoService = playlistVideoService;
    }

    // GET /api/playlist-videos
    @GetMapping
    public ResponseEntity<List<PlaylistVideo>> getAllPlaylistVideos() {
        List<PlaylistVideo> playlistVideos = playlistVideoService.getAllPlaylistVideos();
        return ResponseEntity.ok(playlistVideos);
    }

    // GET /api/playlist-videos/{playlistId}/{videoId}
    @GetMapping("/{playlistId}/{videoId}")
    public ResponseEntity<PlaylistVideo> getPlaylistVideoById(
            @PathVariable Integer playlistId,
            @PathVariable Integer videoId) {
        PlaylistVideoId id = new PlaylistVideoId();
        id.setPlaylistId(playlistId);
        id.setVideoId(videoId);
        return playlistVideoService.getPlaylistVideoById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // POST /api/playlist-videos
    @PostMapping
    public ResponseEntity<PlaylistVideo> createPlaylistVideo(@Valid @RequestBody PlaylistVideo playlistVideo) {
        PlaylistVideo createdPlaylistVideo = playlistVideoService.createPlaylistVideo(playlistVideo);
        return ResponseEntity.ok(createdPlaylistVideo);
    }

    // PUT /api/playlist-videos/{playlistId}/{videoId}
    @PutMapping("/{playlistId}/{videoId}")
    public ResponseEntity<PlaylistVideo> updatePlaylistVideo(
            @PathVariable Integer playlistId,
            @PathVariable Integer videoId,
            @Valid @RequestBody PlaylistVideo playlistVideo) {
        PlaylistVideoId id = new PlaylistVideoId();
        id.setPlaylistId(playlistId);
        id.setVideoId(videoId);
        try {
            PlaylistVideo updatedPlaylistVideo = playlistVideoService.updatePlaylistVideo(id, playlistVideo);
            return ResponseEntity.ok(updatedPlaylistVideo);
        } catch (RuntimeException ex) {
            return ResponseEntity.notFound().build();
        }
    }

    // DELETE /api/playlist-videos/{playlistId}/{videoId}
    @DeleteMapping("/{playlistId}/{videoId}")
    public ResponseEntity<Void> deletePlaylistVideo(
            @PathVariable Integer playlistId,
            @PathVariable Integer videoId) {
        PlaylistVideoId id = new PlaylistVideoId();
        id.setPlaylistId(playlistId);
        id.setVideoId(videoId);
        playlistVideoService.deletePlaylistVideo(id);
        return ResponseEntity.noContent().build();
    }

    // GET /api/playlist-videos/playlist/{playlistId}
    @GetMapping("/playlist/{playlistId}")
    public ResponseEntity<List<PlaylistVideo>> getPlaylistVideosByPlaylistId(@PathVariable Integer playlistId) {
        List<PlaylistVideo> playlistVideos = playlistVideoService.getPlaylistVideosByPlaylistId(playlistId);
        return ResponseEntity.ok(playlistVideos);
    }

    // GET /api/playlist-videos/video/{videoId}
    @GetMapping("/video/{videoId}")
    public ResponseEntity<List<PlaylistVideo>> getPlaylistVideosByVideoId(@PathVariable Integer videoId) {
        List<PlaylistVideo> playlistVideos = playlistVideoService.getPlaylistVideosByVideoId(videoId);
        return ResponseEntity.ok(playlistVideos);
    }
}
