package com.shadowveil.videoplatform.controller;

import com.shadowveil.videoplatform.dto.PlaylistDto;
import com.shadowveil.videoplatform.dto.PlaylistVideoDto;
import com.shadowveil.videoplatform.dto.UserDto;
import com.shadowveil.videoplatform.dto.VideoDto;
import com.shadowveil.videoplatform.dto.ModuleDto; // Import ModuleDto
import com.shadowveil.videoplatform.entity.PlaylistVideo;
import com.shadowveil.videoplatform.entity.PlaylistVideoId;
import com.shadowveil.videoplatform.service.PlaylistVideoService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/playlist-videos")
public class PlaylistVideoController {

    private final PlaylistVideoService playlistVideoService;

    @Autowired
    public PlaylistVideoController(PlaylistVideoService playlistVideoService) {
        this.playlistVideoService = playlistVideoService;
    }

    @GetMapping
    public ResponseEntity<List<PlaylistVideoDto.Response>> getAllPlaylistVideos() {
        List<PlaylistVideo> playlistVideos = playlistVideoService.getAllPlaylistVideos();
        List<PlaylistVideoDto.Response> responseDtos = playlistVideos.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(responseDtos);
    }

    @GetMapping("/{playlistId}/{videoId}")
    public ResponseEntity<PlaylistVideoDto.Response> getPlaylistVideoById(
            @PathVariable Integer playlistId,
            @PathVariable Integer videoId) {
        PlaylistVideoId id = new PlaylistVideoId(playlistId, videoId); // Use constructor
        PlaylistVideo playlistVideo = playlistVideoService.getPlaylistVideoById(id);
        return ResponseEntity.ok(convertToDto(playlistVideo));
    }

    @PostMapping
    public ResponseEntity<PlaylistVideoDto.Response> createPlaylistVideo(@Valid @RequestBody PlaylistVideoDto.Request requestDto) {
        PlaylistVideo createdPlaylistVideo = playlistVideoService.createPlaylistVideo(requestDto);
        return new ResponseEntity<>(convertToDto(createdPlaylistVideo), HttpStatus.CREATED);
    }

    @PutMapping("/{playlistId}/{videoId}")
    public ResponseEntity<PlaylistVideoDto.Response> updatePlaylistVideo(
            @PathVariable Integer playlistId,
            @PathVariable Integer videoId,
            @Valid @RequestBody PlaylistVideoDto.UpdatePositionRequest requestDto) { // Use Update DTO
        PlaylistVideoId id = new PlaylistVideoId(playlistId, videoId); // Use constructor
        PlaylistVideo updatedPlaylistVideo = playlistVideoService.updatePlaylistVideo(id, requestDto);
        return ResponseEntity.ok(convertToDto(updatedPlaylistVideo));

    }

    @DeleteMapping("/{playlistId}/{videoId}")
    public ResponseEntity<Void> deletePlaylistVideo(
            @PathVariable Integer playlistId,
            @PathVariable Integer videoId) {
        PlaylistVideoId id = new PlaylistVideoId(playlistId, videoId); // Use constructor
        playlistVideoService.deletePlaylistVideo(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/playlist/{playlistId}")
    public ResponseEntity<List<PlaylistVideoDto.Response>> getPlaylistVideosByPlaylistId(@PathVariable Integer playlistId) {
        List<PlaylistVideo> playlistVideos = playlistVideoService.getPlaylistVideosByPlaylistId(playlistId);
        List<PlaylistVideoDto.Response> responseDtos = playlistVideos.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(responseDtos);
    }

    @GetMapping("/video/{videoId}")
    public ResponseEntity<List<PlaylistVideoDto.Response>> getPlaylistVideosByVideoId(@PathVariable Integer videoId) {
        List<PlaylistVideo> playlistVideos = playlistVideoService.getPlaylistVideosByVideoId(videoId);
        List<PlaylistVideoDto.Response> responseDtos = playlistVideos.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(responseDtos);
    }

    private PlaylistVideoDto.Response convertToDto(PlaylistVideo playlistVideo) {
        // Create nested DTOs (assuming you have PlaylistDto.Response and VideoDto.Response)
        PlaylistDto.Response playlistDto = new PlaylistDto.Response(
                playlistVideo.getPlaylist().getId(),
                new UserDto.Response(
                        playlistVideo.getPlaylist().getUser().getId(),
                        playlistVideo.getPlaylist().getUser().getUsername(),
                        playlistVideo.getPlaylist().getUser().getEmail(),
                        playlistVideo.getPlaylist().getUser().getRole(),
                        playlistVideo.getPlaylist().getUser().getCreatedAt(),
                        playlistVideo.getPlaylist().getUser().getUpdatedAt()

                ),
                playlistVideo.getPlaylist().getName(),
                playlistVideo.getPlaylist().getDescription(),
                playlistVideo.getPlaylist().getVisibility(),
                playlistVideo.getPlaylist().getCreatedAt(),
                playlistVideo.getPlaylist().getUpdatedAt()
        );
        //Corrected VideoDto
        VideoDto.Response videoDto = new VideoDto.Response(
                playlistVideo.getVideo().getId(),
                (playlistVideo.getVideo().getUser() != null) ? new UserDto.Response(
                        playlistVideo.getVideo().getUser().getId(),
                        playlistVideo.getVideo().getUser().getUsername(),
                        playlistVideo.getVideo().getUser().getEmail(),
                        playlistVideo.getVideo().getUser().getRole(),
                        playlistVideo.getVideo().getUser().getCreatedAt(),
                        playlistVideo.getVideo().getUser().getUpdatedAt()
                ) : null,
                playlistVideo.getVideo().getTitle(),
                playlistVideo.getVideo().getDescription(),
                playlistVideo.getVideo().getUrl(),
                playlistVideo.getVideo().getThumbnailUrl(), // Correct getter
                playlistVideo.getVideo().getDuration(),
                playlistVideo.getVideo().getStatus(),
                playlistVideo.getVideo().getViews(),
                playlistVideo.getVideo().getLikes(),
                playlistVideo.getVideo().getDislikes(),
                playlistVideo.getVideo().getCreatedAt(),
                playlistVideo.getVideo().getUpdatedAt(),
                (playlistVideo.getVideo().getModule() != null) ? new ModuleDto.Response( // Handle null module
                        playlistVideo.getVideo().getModule().getId(),
                        null, // No CourseDto needed
                        playlistVideo.getVideo().getModule().getTitle(),
                        playlistVideo.getVideo().getModule().getDescription(),
                        playlistVideo.getVideo().getModule().getPosition(),
                        playlistVideo.getVideo().getModule().getCreatedAt(),
                        playlistVideo.getVideo().getModule().getUpdatedAt()
                ) : null
        );

        return new PlaylistVideoDto.Response(
                playlistDto,
                videoDto,
                playlistVideo.getPosition(),
                playlistVideo.getCreatedAt()
        );
    }
    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<String> handleEntityNotFoundException(EntityNotFoundException ex){
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
    }
}