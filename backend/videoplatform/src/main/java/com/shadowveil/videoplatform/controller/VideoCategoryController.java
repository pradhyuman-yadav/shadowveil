package com.shadowveil.videoplatform.controller;

import com.shadowveil.videoplatform.dto.CategoryDto;
import com.shadowveil.videoplatform.dto.VideoCategoryDto;
import com.shadowveil.videoplatform.dto.VideoDto;
import com.shadowveil.videoplatform.dto.UserDto;
import com.shadowveil.videoplatform.dto.ModuleDto;
import com.shadowveil.videoplatform.entity.VideoCategory;
import com.shadowveil.videoplatform.entity.VideoCategoryId;
import com.shadowveil.videoplatform.service.VideoCategoryService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/video-categories")
public class VideoCategoryController {

    private final VideoCategoryService videoCategoryService;

    @Autowired
    public VideoCategoryController(VideoCategoryService videoCategoryService) {
        this.videoCategoryService = videoCategoryService;
    }

    @GetMapping
    public ResponseEntity<List<VideoCategoryDto.Response>> getAllVideoCategories() {
        List<VideoCategory> videoCategories = videoCategoryService.getAllVideoCategories();
        List<VideoCategoryDto.Response> responseDtos = videoCategories.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(responseDtos);
    }

    @GetMapping("/{videoId}/{categoryId}")
    public ResponseEntity<VideoCategoryDto.Response> getVideoCategoryById(
            @PathVariable Integer videoId,
            @PathVariable Integer categoryId) {
        VideoCategoryId id = new VideoCategoryId(videoId, categoryId);
        VideoCategory videoCategory = videoCategoryService.getVideoCategoryById(id);
        return ResponseEntity.ok(convertToDto(videoCategory));
    }

    @PostMapping
    public ResponseEntity<VideoCategoryDto.Response> createVideoCategory(@Valid @RequestBody VideoCategoryDto.Request requestDto) {
        VideoCategory createdVideoCategory = videoCategoryService.createVideoCategory(requestDto);
        return new ResponseEntity<>(convertToDto(createdVideoCategory), HttpStatus.CREATED);
    }
    @PutMapping("/{videoId}/{categoryId}")
    public ResponseEntity<VideoCategoryDto.Response> updateVideoCategory(
            @PathVariable Integer videoId,
            @PathVariable Integer categoryId,
            @Valid @RequestBody VideoCategoryDto.Request requestDto)
    {
        VideoCategoryId id = new VideoCategoryId(videoId, categoryId);
        VideoCategory updatedVideoCategory = videoCategoryService.updateVideoCategory(id, requestDto);
        return  ResponseEntity.ok(convertToDto(updatedVideoCategory));
    }

    @DeleteMapping("/{videoId}/{categoryId}")
    public ResponseEntity<Void> deleteVideoCategory(
            @PathVariable Integer videoId,
            @PathVariable Integer categoryId) {
        VideoCategoryId id = new VideoCategoryId(videoId, categoryId);
        videoCategoryService.deleteVideoCategory(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/video/{videoId}")
    public ResponseEntity<List<VideoCategoryDto.Response>> getVideoCategoriesByVideoId(@PathVariable Integer videoId) {
        List<VideoCategory> videoCategories = videoCategoryService.getVideoCategoriesByVideoId(videoId);
        List<VideoCategoryDto.Response> responseDtos = videoCategories.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(responseDtos);
    }

    @GetMapping("/category/{categoryId}")
    public ResponseEntity<List<VideoCategoryDto.Response>> getVideoCategoriesByCategoryId(@PathVariable Integer categoryId) {
        List<VideoCategory> videoCategories = videoCategoryService.getVideoCategoriesByCategoryId(categoryId);
        List<VideoCategoryDto.Response> responseDtos = videoCategories.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(responseDtos);
    }

    private VideoCategoryDto.Response convertToDto(VideoCategory videoCategory) {
        VideoDto.Response videoDto = new VideoDto.Response(
                videoCategory.getVideo().getId(),
                (videoCategory.getVideo().getUser() != null) ? new UserDto.Response(
                        videoCategory.getVideo().getUser().getId(),
                        videoCategory.getVideo().getUser().getUsername(),
                        videoCategory.getVideo().getUser().getEmail(),
                        videoCategory.getVideo().getUser().getRole(),
                        videoCategory.getVideo().getUser().getCreatedAt(),
                        videoCategory.getVideo().getUser().getUpdatedAt()
                ) : null,
                videoCategory.getVideo().getTitle(),
                videoCategory.getVideo().getDescription(),
                videoCategory.getVideo().getUrl(),
                videoCategory.getVideo().getThumbnailUrl(),
                videoCategory.getVideo().getDuration(),
                videoCategory.getVideo().getStatus(),
                videoCategory.getVideo().getViews(),
                videoCategory.getVideo().getLikes(),
                videoCategory.getVideo().getDislikes(),
                videoCategory.getVideo().getCreatedAt(),
                videoCategory.getVideo().getUpdatedAt(),
                (videoCategory.getVideo().getModule() != null) ? new ModuleDto.Response( // Handle null module
                        videoCategory.getVideo().getModule().getId(),
                        null, // No CourseDto needed
                        videoCategory.getVideo().getModule().getTitle(),
                        videoCategory.getVideo().getModule().getDescription(),
                        videoCategory.getVideo().getModule().getPosition(),
                        videoCategory.getVideo().getModule().getCreatedAt(),
                        videoCategory.getVideo().getModule().getUpdatedAt()
                ) : null
        );

        CategoryDto.Response categoryDto = new CategoryDto.Response(
                videoCategory.getCategory().getId(),
                videoCategory.getCategory().getName(),
                videoCategory.getCategory().getDescription(),
                videoCategory.getCategory().getCreatedAt()
        );


        return new VideoCategoryDto.Response(videoDto, categoryDto, videoCategory.getCreatedAt());
    }
    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<String> handleEntityNotFoundException(EntityNotFoundException ex){
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
    }
}