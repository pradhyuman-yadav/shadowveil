package com.shadowveil.videoplatform.controller;

import com.shadowveil.videoplatform.dto.ModuleDto;
import com.shadowveil.videoplatform.dto.UserDto;
import com.shadowveil.videoplatform.dto.VideoDto;
import com.shadowveil.videoplatform.dto.VideoSubtitleDto;
import com.shadowveil.videoplatform.entity.VideoSubtitle;
import com.shadowveil.videoplatform.service.VideoSubtitleService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/video-subtitles")
public class VideoSubtitleController {

    private final VideoSubtitleService videoSubtitleService;

    @Autowired
    public VideoSubtitleController(VideoSubtitleService videoSubtitleService) {
        this.videoSubtitleService = videoSubtitleService;
    }

    @GetMapping
    public ResponseEntity<List<VideoSubtitleDto.Response>> getAllSubtitles() {
        List<VideoSubtitle> subtitles = videoSubtitleService.getAllSubtitles();
        List<VideoSubtitleDto.Response> responseDtos = subtitles.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
        return new ResponseEntity<>(responseDtos, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<VideoSubtitleDto.Response> getSubtitleById(@PathVariable Integer id) {
        VideoSubtitle subtitle = videoSubtitleService.getSubtitleById(id);
        return new ResponseEntity<>(convertToDto(subtitle), HttpStatus.OK);
    }

    @GetMapping("/video/{videoId}")
    public ResponseEntity<List<VideoSubtitleDto.Response>> getSubtitlesByVideoId(@PathVariable Integer videoId) {
        List<VideoSubtitle> subtitles = videoSubtitleService.getSubtitlesByVideoId(videoId);
        List<VideoSubtitleDto.Response> responseDtos = subtitles.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
        return new ResponseEntity<>(responseDtos, HttpStatus.OK);
    }

    @GetMapping("/video/{videoId}/language/{language}")
    public ResponseEntity<List<VideoSubtitleDto.Response>> getSubtitlesByVideoIdAndLanguage(
            @PathVariable Integer videoId,
            @PathVariable String language) {

        List<VideoSubtitle> subtitles = videoSubtitleService.getSubtitlesByVideoIdAndLanguage(videoId, language);
        List<VideoSubtitleDto.Response> responseDtos = subtitles.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
        return new ResponseEntity<>(responseDtos, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<VideoSubtitleDto.Response> createSubtitle(@Valid @RequestBody VideoSubtitleDto.Request requestDto) {
        VideoSubtitle savedSubtitle = videoSubtitleService.createSubtitle(requestDto);
        return new ResponseEntity<>(convertToDto(savedSubtitle), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<VideoSubtitleDto.Response> updateSubtitle(
            @PathVariable Integer id,
            @Valid @RequestBody VideoSubtitleDto.UpdateRequest requestDto) { // Use UpdateRequest DTO
        VideoSubtitle updatedSubtitle = videoSubtitleService.updateSubtitle(id, requestDto);
        return new ResponseEntity<>(convertToDto(updatedSubtitle), HttpStatus.OK);

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSubtitle(@PathVariable Integer id) {
        videoSubtitleService.deleteSubtitle(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    private VideoSubtitleDto.Response convertToDto(VideoSubtitle subtitle) {
        VideoDto.Response videoDto = new VideoDto.Response(
                subtitle.getVideo().getId(),
                (subtitle.getVideo().getUser() != null) ? new UserDto.Response(
                        subtitle.getVideo().getUser().getId(),
                        subtitle.getVideo().getUser().getUsername(),
                        subtitle.getVideo().getUser().getEmail(),
                        subtitle.getVideo().getUser().getRole(),
                        subtitle.getVideo().getUser().getCreatedAt(),
                        subtitle.getVideo().getUser().getUpdatedAt()
                ) : null,
                subtitle.getVideo().getTitle(),
                subtitle.getVideo().getDescription(),
                subtitle.getVideo().getUrl(),
                subtitle.getVideo().getThumbnailUrl(),
                subtitle.getVideo().getDuration(),
                subtitle.getVideo().getStatus(),
                subtitle.getVideo().getViews(),
                subtitle.getVideo().getLikes(),
                subtitle.getVideo().getDislikes(),
                subtitle.getVideo().getCreatedAt(),
                subtitle.getVideo().getUpdatedAt(),
                (subtitle.getVideo().getModule() != null) ? new ModuleDto.Response( // Handle null module
                        subtitle.getVideo().getModule().getId(),
                        null, // No CourseDto needed
                        subtitle.getVideo().getModule().getTitle(),
                        subtitle.getVideo().getModule().getDescription(),
                        subtitle.getVideo().getModule().getPosition(),
                        subtitle.getVideo().getModule().getCreatedAt(),
                        subtitle.getVideo().getModule().getUpdatedAt()
                ) : null
        );

        return new VideoSubtitleDto.Response(
                subtitle.getId(),
                videoDto, // Nested DTO
                subtitle.getLanguage(),
                subtitle.getSubtitleUrl(),
                subtitle.getCreatedAt()
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
}