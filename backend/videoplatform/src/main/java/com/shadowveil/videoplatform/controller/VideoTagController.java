package com.shadowveil.videoplatform.controller;

import com.shadowveil.videoplatform.dto.TagDto;
import com.shadowveil.videoplatform.dto.VideoDto;
import com.shadowveil.videoplatform.dto.VideoTagDto;
import com.shadowveil.videoplatform.dto.ModuleDto;
import com.shadowveil.videoplatform.dto.UserDto;
import com.shadowveil.videoplatform.entity.VideoTag;
import com.shadowveil.videoplatform.service.VideoTagService;
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
@RequestMapping("/api/video-tags")
public class VideoTagController {

    private final VideoTagService videoTagService;

    @Autowired
    public VideoTagController(VideoTagService videoTagService) {
        this.videoTagService = videoTagService;
    }

    @GetMapping
    public ResponseEntity<List<VideoTagDto.Response>> getAllVideoTags() {
        List<VideoTag> videoTags = videoTagService.getAllVideoTags();
        List<VideoTagDto.Response> responseDtos = videoTags.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(responseDtos);
    }

    // Remove this endpoint.  It's not very useful with a composite key.
    // @GetMapping("/{id}")
    // public ResponseEntity<VideoTag> getVideoTagById(@PathVariable VideoTagId id) { ... }

    @GetMapping("/video/{videoId}")
    public ResponseEntity<List<VideoTagDto.Response>> getVideoTagsByVideoId(@PathVariable Integer videoId) {
        List<VideoTag> videoTags = videoTagService.getVideoTagsByVideoId(videoId);
        List<VideoTagDto.Response> responseDtos = videoTags.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(responseDtos);
    }

    @GetMapping("/tag/{tagId}")
    public ResponseEntity<List<VideoTagDto.Response>> getVideoTagsByTagId(@PathVariable Integer tagId) {
        List<VideoTag> videoTags = videoTagService.getVideoTagsByTagId(tagId);
        List<VideoTagDto.Response> responseDtos = videoTags.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(responseDtos);
    }

    @GetMapping("/video/{videoId}/tag/{tagId}")
    public ResponseEntity<List<VideoTagDto.Response>> getVideoTagsByVideoIdAndTagId(@PathVariable Integer videoId, @PathVariable Integer tagId) {
        List<VideoTag> videoTags = videoTagService.getVideoTagsByVideoIdAndTagId(videoId,tagId);
        List<VideoTagDto.Response> responseDtos = videoTags.stream().map(this::convertToDto).collect(Collectors.toList());
        return ResponseEntity.ok(responseDtos);
    }

    // Change this to take videoId and tagId as parameters
    @PostMapping
    public ResponseEntity<VideoTagDto.Response> createVideoTag(@Valid @RequestBody VideoTagDto.Request requestDto) {
        VideoTag savedVideoTag = videoTagService.createVideoTag(requestDto); // Pass IDs
        return new ResponseEntity<>(convertToDto(savedVideoTag), HttpStatus.CREATED);
    }
    @DeleteMapping("/video/{videoId}/tag/{tagId}")
    public ResponseEntity<Void> deleteVideoTag(
            @PathVariable Integer videoId,
            @PathVariable Integer tagId) {
        videoTagService.deleteVideoTag(videoId, tagId); // Pass IDs
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
    private VideoTagDto.Response convertToDto(VideoTag videoTag) {
        VideoDto.Response videoDto = new VideoDto.Response(
                videoTag.getVideo().getId(),
                (videoTag.getVideo().getUser() != null) ? new UserDto.Response(
                        videoTag.getVideo().getUser().getId(),
                        videoTag.getVideo().getUser().getUsername(),
                        videoTag.getVideo().getUser().getEmail(),
                        videoTag.getVideo().getUser().getRole(),
                        videoTag.getVideo().getUser().getCreatedAt(),
                        videoTag.getVideo().getUser().getUpdatedAt()
                ) : null,
                videoTag.getVideo().getTitle(),
                videoTag.getVideo().getDescription(),
                videoTag.getVideo().getUrl(),
                videoTag.getVideo().getThumbnailUrl(),
                videoTag.getVideo().getDuration(),
                videoTag.getVideo().getStatus(),
                videoTag.getVideo().getViews(),
                videoTag.getVideo().getLikes(),
                videoTag.getVideo().getDislikes(),
                videoTag.getVideo().getCreatedAt(),
                videoTag.getVideo().getUpdatedAt(),
                (videoTag.getVideo().getModule() != null) ? new ModuleDto.Response( // Handle null module
                        videoTag.getVideo().getModule().getId(),
                        null, // No CourseDto needed
                        videoTag.getVideo().getModule().getTitle(),
                        videoTag.getVideo().getModule().getDescription(),
                        videoTag.getVideo().getModule().getPosition(),
                        videoTag.getVideo().getModule().getCreatedAt(),
                        videoTag.getVideo().getModule().getUpdatedAt()
                ) : null
        );
        TagDto.Response tagDto = new TagDto.Response(
                videoTag.getTag().getId(),
                videoTag.getTag().getName(),
                videoTag.getTag().getCreatedAt(),
                videoTag.getTag().getUpdatedAt()

        );

        return new VideoTagDto.Response(videoDto, tagDto, videoTag.getCreatedAt()); // Use the nested DTOs
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
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.CONFLICT); // 409 Conflict
    }
}