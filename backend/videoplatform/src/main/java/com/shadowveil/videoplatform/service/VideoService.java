//Corrected VideoService
package com.shadowveil.videoplatform.service;

import com.shadowveil.videoplatform.dto.ModuleDto;
import com.shadowveil.videoplatform.dto.UserDto;
import com.shadowveil.videoplatform.dto.VideoDto; // Import the VideoDto
import com.shadowveil.videoplatform.entity.Module;
import com.shadowveil.videoplatform.entity.User;
import com.shadowveil.videoplatform.entity.Video;
import com.shadowveil.videoplatform.exception.ResourceNotFoundException;
import com.shadowveil.videoplatform.repository.ModuleRepository;
import com.shadowveil.videoplatform.repository.UserRepository;
import com.shadowveil.videoplatform.repository.VideoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class VideoService {

    private final VideoRepository videoRepository;
    private final UserRepository userRepository; // Inject UserRepository
    private final ModuleRepository moduleRepository;
    private final ModuleService moduleService; // Inject ModuleService
    private final UserService userService;

    @Autowired
    public VideoService(VideoRepository videoRepository, UserRepository userRepository, ModuleRepository moduleRepository, ModuleService moduleService, UserService userService) {
        this.videoRepository = videoRepository;
        this.userRepository = userRepository;
        this.moduleRepository = moduleRepository;
        this.moduleService = moduleService; // Initialize ModuleService
        this.userService = userService;
    }

    @Transactional(readOnly = true)
    public List<VideoDto.Response> getAllVideos() {
        return videoRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Optional<VideoDto.Response> getVideoById(Integer id) {
        return videoRepository.findById(id)
                .map(this::convertToDto);
    }

    @Transactional(readOnly = true)
    public List<VideoDto.Response> getVideosByUserId(Integer userId) {
        return videoRepository.findByUser_Id(userId).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }
    @Transactional(readOnly = true)
    public List<VideoDto.Response> getVideosByModuleId(Integer moduleId) {
        return videoRepository.findByModuleId(moduleId).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public Video createVideo(VideoDto.Request videoDto) {
        User user = null;
        if(videoDto.userId() != null){
            user = userRepository.findById(videoDto.userId())
                    .orElseThrow(() -> new ResourceNotFoundException("User with ID " + videoDto.userId() + " not found."));
        }

        Module module = null;
        // Validate module (if provided)
        if (videoDto.moduleId() != null) {
            module = moduleRepository.findById(videoDto.moduleId())
                    .orElseThrow(() -> new ResourceNotFoundException("Module with ID " + videoDto.moduleId() + " not found."));
        }

        Video video = new Video();
        video.setUser(user); // Set User
        video.setTitle(videoDto.title());
        video.setDescription(videoDto.description());
        video.setUrl(videoDto.url());
        video.setThumbnailUrl(videoDto.thumbnailUrl());
        video.setDuration(videoDto.duration());
        video.setStatus(videoDto.status());
        video.setModule(module);  // Set the Module (can be null)
        video.setCreatedAt(Instant.now());
        video.setUpdatedAt(Instant.now());
        return videoRepository.save(video);
    }

    @Transactional
    public Video updateVideo(Integer id, VideoDto.Request videoDto) {
        Video video = videoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Video not found with id " + id));

        // Update User (if provided and different)
        if (videoDto.userId() != null && (video.getUser() == null || !video.getUser().getId().equals(videoDto.userId()))) {
            User newUser = userRepository.findById(videoDto.userId())
                    .orElseThrow(() -> new ResourceNotFoundException("User with ID " + videoDto.userId() + " not found."));
            video.setUser(newUser);
        }


        // Update Module (if provided and different)
        if(videoDto.moduleId() != null && (video.getModule() == null || !video.getModule().getId().equals(videoDto.moduleId()))){
            Module module = moduleRepository.findById(videoDto.moduleId())
                    .orElseThrow(()-> new ResourceNotFoundException("Module with ID: " + videoDto.moduleId() + " does not exists"));
            video.setModule(module);
        }

        // Update scalar fields
        if (videoDto.title() != null) video.setTitle(videoDto.title());
        if (videoDto.description() != null) video.setDescription(videoDto.description());
        if (videoDto.url() != null) video.setUrl(videoDto.url());
        if (videoDto.thumbnailUrl() != null) video.setThumbnailUrl(videoDto.thumbnailUrl());
        if (videoDto.duration() != null) video.setDuration(videoDto.duration());
        if (videoDto.status() != null) video.setStatus(videoDto.status());

        video.setUpdatedAt(Instant.now());
        return videoRepository.save(video);
    }

    @Transactional
    public void deleteVideo(Integer id) {
        if (!videoRepository.existsById(id)) {
            throw new ResourceNotFoundException("Video with ID " + id + " does not exist");
        }
        videoRepository.deleteById(id);
    }

    // --- DTO Conversion Methods (Private) ---

    public VideoDto.Response convertToDto(Video video) {

        UserDto.Response userDto = null;
        if(video.getUser() != null){
            userDto = userService.convertToDto(video.getUser()); // Uses UserService
        }

        ModuleDto.Response moduleDto = null;
        if (video.getModule() != null) {
            moduleDto = moduleService.convertToDto(video.getModule()); // Uses ModuleService
        }

        return new VideoDto.Response(
                video.getId(),
                userDto,
                video.getTitle(),
                video.getDescription(),
                video.getUrl(),
                video.getThumbnailUrl(),
                video.getDuration(),
                video.getStatus(),
                video.getViews(),
                video.getLikes(),
                video.getDislikes(),
                video.getCreatedAt(),
                video.getUpdatedAt(),
                moduleDto // Correctly using ModuleDto.Response
        );
    }
}