package com.shadowveil.videoplatform.controller;

import com.shadowveil.videoplatform.dto.ModuleDto;
import com.shadowveil.videoplatform.dto.UserDto;
import com.shadowveil.videoplatform.dto.VideoDto;
import com.shadowveil.videoplatform.dto.VideoTranscriptDto;
import com.shadowveil.videoplatform.entity.VideoTranscript;
import com.shadowveil.videoplatform.service.VideoTranscriptService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/video-transcripts")
public class VideoTranscriptController {

    private final VideoTranscriptService videoTranscriptService;

    @Autowired
    public VideoTranscriptController(VideoTranscriptService videoTranscriptService) {
        this.videoTranscriptService = videoTranscriptService;
    }

    @GetMapping
    public ResponseEntity<List<VideoTranscriptDto.Response>> getAllTranscripts() {
        List<VideoTranscript> transcripts = videoTranscriptService.getAllTranscripts();
        List<VideoTranscriptDto.Response> responseDtos = transcripts.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
        return new ResponseEntity<>(responseDtos, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<VideoTranscriptDto.Response> getTranscriptById(@PathVariable Integer id) {
        VideoTranscript transcript = videoTranscriptService.getTranscriptById(id);
        return new ResponseEntity<>(convertToDto(transcript), HttpStatus.OK);
    }

    @GetMapping("/video/{videoId}")
    public ResponseEntity<List<VideoTranscriptDto.Response>> getTranscriptsByVideoId(@PathVariable Integer videoId) {
        List<VideoTranscript> transcripts = videoTranscriptService.getTranscriptsByVideoId(videoId);
        List<VideoTranscriptDto.Response> responseDtos = transcripts.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
        return new ResponseEntity<>(responseDtos, HttpStatus.OK);
    }

    @GetMapping("/video/{videoId}/language/{language}")
    public ResponseEntity<List<VideoTranscriptDto.Response>> getTranscriptByVideoIdAndLanguage(
            @PathVariable Integer videoId,
            @PathVariable String language) {
        List<VideoTranscript> transcripts = videoTranscriptService.getTranscriptsByVideoIdAndLanguage(videoId, language);

        List<VideoTranscriptDto.Response> responseDtos = transcripts.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
        return new ResponseEntity<>(responseDtos, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<VideoTranscriptDto.Response> createTranscript(@Valid @RequestBody VideoTranscriptDto.Request requestDto) {
        VideoTranscript savedTranscript = videoTranscriptService.createTranscript(requestDto);
        return new ResponseEntity<>(convertToDto(savedTranscript), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<VideoTranscriptDto.Response> updateTranscript(
            @PathVariable Integer id,
            @Valid @RequestBody VideoTranscriptDto.UpdateRequest requestDto) { // Use UpdateRequest DTO
        VideoTranscript updatedTranscript = videoTranscriptService.updateTranscript(id, requestDto);
        return new ResponseEntity<>(convertToDto(updatedTranscript), HttpStatus.OK);

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTranscript(@PathVariable Integer id) {
        videoTranscriptService.deleteTranscript(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    private VideoTranscriptDto.Response convertToDto(VideoTranscript transcript) {
        VideoDto.Response videoDto = new VideoDto.Response(
                transcript.getVideo().getId(),
                (transcript.getVideo().getUser() != null) ? new UserDto.Response(
                        transcript.getVideo().getUser().getId(),
                        transcript.getVideo().getUser().getUsername(),
                        transcript.getVideo().getUser().getEmail(),
                        transcript.getVideo().getUser().getRole(),
                        transcript.getVideo().getUser().getCreatedAt(),
                        transcript.getVideo().getUser().getUpdatedAt()
                ) : null,
                transcript.getVideo().getTitle(),
                transcript.getVideo().getDescription(),
                transcript.getVideo().getUrl(),
                transcript.getVideo().getThumbnailUrl(),
                transcript.getVideo().getDuration(),
                transcript.getVideo().getStatus(),
                transcript.getVideo().getViews(),
                transcript.getVideo().getLikes(),
                transcript.getVideo().getDislikes(),
                transcript.getVideo().getCreatedAt(),
                transcript.getVideo().getUpdatedAt(),
                (transcript.getVideo().getModule() != null) ? new ModuleDto.Response( // Handle null module
                        transcript.getVideo().getModule().getId(),
                        null, // No CourseDto needed
                        transcript.getVideo().getModule().getTitle(),
                        transcript.getVideo().getModule().getDescription(),
                        transcript.getVideo().getModule().getPosition(),
                        transcript.getVideo().getModule().getCreatedAt(),
                        transcript.getVideo().getModule().getUpdatedAt()
                ) : null
        );

        return new VideoTranscriptDto.Response(
                transcript.getId(),
                videoDto, // Nested DTO
                transcript.getLanguage(),
                transcript.getTranscript(),
                transcript.getCreatedAt(),
                transcript.getUpdatedAt()
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