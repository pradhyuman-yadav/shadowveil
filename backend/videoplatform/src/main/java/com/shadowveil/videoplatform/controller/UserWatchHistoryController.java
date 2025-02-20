package com.shadowveil.videoplatform.controller;

import com.shadowveil.videoplatform.dto.UserDto;
import com.shadowveil.videoplatform.dto.UserWatchHistoryDto;
import com.shadowveil.videoplatform.dto.VideoDto;
import com.shadowveil.videoplatform.dto.ModuleDto; // Import ModuleDto
import com.shadowveil.videoplatform.entity.UserWatchHistory;
import com.shadowveil.videoplatform.service.UserWatchHistoryService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/watch-history") // Good, descriptive path
public class UserWatchHistoryController {

    private final UserWatchHistoryService userWatchHistoryService;

    @Autowired
    public UserWatchHistoryController(UserWatchHistoryService userWatchHistoryService) {
        this.userWatchHistoryService = userWatchHistoryService;
    }

    @GetMapping
    public ResponseEntity<List<UserWatchHistoryDto.Response>> getAllWatchHistory() {
        List<UserWatchHistory> historyEntries = userWatchHistoryService.getAllWatchHistory();
        List<UserWatchHistoryDto.Response> responseDtos = historyEntries.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(responseDtos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserWatchHistoryDto.Response> getWatchHistoryById(@PathVariable Integer id) {
        UserWatchHistory historyEntry = userWatchHistoryService.getWatchHistoryById(id);
        return ResponseEntity.ok(convertToDto(historyEntry));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<UserWatchHistoryDto.Response>> getWatchHistoryByUserId(@PathVariable Integer userId) {
        List<UserWatchHistory> historyEntries = userWatchHistoryService.getWatchHistoryByUserId(userId);
        List<UserWatchHistoryDto.Response> responseDtos = historyEntries.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(responseDtos);
    }

    @GetMapping("/video/{videoId}")
    public ResponseEntity<List<UserWatchHistoryDto.Response>> getWatchHistoryByVideoId(@PathVariable Integer videoId) {
        List<UserWatchHistory> historyEntries = userWatchHistoryService.getWatchHistoryByVideoId(videoId);
        List<UserWatchHistoryDto.Response> responseDtos = historyEntries.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(responseDtos);
    }

    @GetMapping("/user/{userId}/video/{videoId}")
    public ResponseEntity<List<UserWatchHistoryDto.Response>> getWatchHistoryByUserAndVideo(
            @PathVariable Integer userId,
            @PathVariable Integer videoId) {
        List<UserWatchHistory> historyEntries = userWatchHistoryService.getWatchHistoryByUserIdAndVideoId(userId, videoId);
        List<UserWatchHistoryDto.Response> responseDtos = historyEntries.stream().map(this::convertToDto).collect(Collectors.toList());
        return ResponseEntity.ok(responseDtos);
    }

    @PostMapping
    public ResponseEntity<UserWatchHistoryDto.Response> createWatchHistoryEntry(@Valid @RequestBody UserWatchHistoryDto.Request requestDto) {
        UserWatchHistory savedEntry = userWatchHistoryService.createWatchHistoryEntry(requestDto);
        return new ResponseEntity<>(convertToDto(savedEntry), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")  // Correct endpoint
    public ResponseEntity<UserWatchHistoryDto.Response> updateWatchHistoryEntry(
            @PathVariable Integer id,
            @Valid @RequestBody UserWatchHistoryDto.UpdateRequest requestDto) { // Use UpdateRequest DTO
        UserWatchHistory updatedEntry = userWatchHistoryService.updateWatchHistoryEntry(id, requestDto);
        return ResponseEntity.ok(convertToDto(updatedEntry));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteWatchHistoryEntry(@PathVariable Integer id) {
        userWatchHistoryService.deleteWatchHistoryEntry(id);
        return ResponseEntity.noContent().build();
    }

    private UserWatchHistoryDto.Response convertToDto(UserWatchHistory historyEntry) {
        // Create nested DTOs
        UserDto.Response userDto = new UserDto.Response(
                historyEntry.getUser().getId(),
                historyEntry.getUser().getUsername(),
                historyEntry.getUser().getEmail(),
                historyEntry.getUser().getRole(),
                historyEntry.getUser().getCreatedAt(),
                historyEntry.getUser().getUpdatedAt()
        );

        VideoDto.Response videoDto = new VideoDto.Response(
                historyEntry.getVideo().getId(),
                (historyEntry.getVideo().getUser() != null) ? new UserDto.Response(
                        historyEntry.getVideo().getUser().getId(),
                        historyEntry.getVideo().getUser().getUsername(),
                        historyEntry.getVideo().getUser().getEmail(),
                        historyEntry.getVideo().getUser().getRole(),
                        historyEntry.getVideo().getUser().getCreatedAt(),
                        historyEntry.getVideo().getUser().getUpdatedAt()
                ) : null,
                historyEntry.getVideo().getTitle(),
                historyEntry.getVideo().getDescription(),
                historyEntry.getVideo().getUrl(),
                historyEntry.getVideo().getThumbnailUrl(),
                historyEntry.getVideo().getDuration(),
                historyEntry.getVideo().getStatus(),
                historyEntry.getVideo().getViews(),
                historyEntry.getVideo().getLikes(),
                historyEntry.getVideo().getDislikes(),
                historyEntry.getVideo().getCreatedAt(),
                historyEntry.getVideo().getUpdatedAt(),
                (historyEntry.getVideo().getModule() != null) ? new ModuleDto.Response( // Handle null module
                        historyEntry.getVideo().getModule().getId(),
                        null, // No CourseDto needed
                        historyEntry.getVideo().getModule().getTitle(),
                        historyEntry.getVideo().getModule().getDescription(),
                        historyEntry.getVideo().getModule().getPosition(),
                        historyEntry.getVideo().getModule().getCreatedAt(),
                        historyEntry.getVideo().getModule().getUpdatedAt()
                ) : null
        );

        return new UserWatchHistoryDto.Response(
                historyEntry.getId(),
                userDto,
                videoDto,
                historyEntry.getWatchedAt(),
                historyEntry.getDurationWatched()
        );
    }
    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<String> handleEntityNotFoundException(EntityNotFoundException ex){
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
    }
    @ExceptionHandler(IllegalArgumentException.class)
    public  ResponseEntity<String> handleIllegalArgumentException(IllegalArgumentException ex){
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<String> handleDataIntegrityViolationException(DataIntegrityViolationException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.CONFLICT);
    }
}