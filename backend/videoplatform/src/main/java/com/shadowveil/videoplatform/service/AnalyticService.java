package com.shadowveil.videoplatform.service;

import com.shadowveil.videoplatform.dto.AnalyticDto;
import com.shadowveil.videoplatform.dto.UserDto;
import com.shadowveil.videoplatform.dto.VideoDto;
import com.shadowveil.videoplatform.entity.Analytic;
import com.shadowveil.videoplatform.entity.User;
import com.shadowveil.videoplatform.entity.Video;
import com.shadowveil.videoplatform.exception.ResourceNotFoundException;
import com.shadowveil.videoplatform.repository.AnalyticRepository;
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
public class AnalyticService {

    private final AnalyticRepository analyticRepository;
    private final VideoRepository videoRepository; // Inject VideoRepository
    private final UserRepository userRepository;    // Inject UserRepository
    private final VideoService videoService;
    private final UserService userService;


    @Autowired
    public AnalyticService(AnalyticRepository analyticRepository, VideoRepository videoRepository,
                           UserRepository userRepository, VideoService videoService, UserService userService) {
        this.analyticRepository = analyticRepository;
        this.videoRepository = videoRepository;
        this.userRepository = userRepository;
        this.videoService = videoService;
        this.userService = userService;
    }

    @Transactional(readOnly = true)
    public List<AnalyticDto.Response> getAllAnalytics() {
        return analyticRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Optional<AnalyticDto.Response> getAnalyticById(Integer id) {
        return analyticRepository.findById(id)
                .map(this::convertToDto);
    }
    @Transactional(readOnly = true)
    public List<AnalyticDto.Response> getAnalyticsByVideoId(Integer videoId) {
        return analyticRepository.findByVideo_Id(videoId).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<AnalyticDto.Response> getAnalyticsByUserId(Integer userId) {
        return analyticRepository.findByUser_Id(userId).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public Analytic createAnalytic(AnalyticDto.Request analyticDto) {
        Video video = videoRepository.findById(analyticDto.videoId())
                .orElseThrow(() -> new ResourceNotFoundException("Video with ID " + analyticDto.videoId() + " not found."));

        User user = null; // Initialize to null
        if (analyticDto.userId() != null) { // Check for null before trying to find
            user = userRepository.findById(analyticDto.userId())
                    .orElseThrow(() -> new ResourceNotFoundException("User with ID " + analyticDto.userId() + " not found"));
        }


        Analytic analytic = new Analytic();
        analytic.setVideo(video);
        analytic.setUser(user); // Can be null
        analytic.setViewDuration(analyticDto.viewDuration());
        analytic.setIpAddress(analyticDto.ipAddress());
        analytic.setUserAgent(analyticDto.userAgent());
        analytic.setWatchedAt(Instant.now()); // Or get from DTO if provided
        return analyticRepository.save(analytic);
    }

    @Transactional
    public Analytic updateAnalytic(Integer id, AnalyticDto.Request analyticDto) {
        Analytic analytic = analyticRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Analytic not found with id " + id));

        // Update Video (if provided and different)
        if(analyticDto.videoId() != null && (analytic.getVideo() == null || !analytic.getVideo().getId().equals(analyticDto.videoId()))){
            Video video = videoRepository.findById(analyticDto.videoId())
                    .orElseThrow(()-> new ResourceNotFoundException("Video with ID: " + analyticDto.videoId() + " does not exists"));
            analytic.setVideo(video);
        }

        // Update User (if provided and different)
        if(analyticDto.userId() != null && (analytic.getUser() == null || !analytic.getUser().getId().equals(analyticDto.userId()))){
            User user = userRepository.findById(analyticDto.userId())
                    .orElseThrow(() -> new ResourceNotFoundException("User with ID: " + analyticDto.userId() + " not found."));
            analytic.setUser(user);
        }

        // Update scalar fields.  Use .orElse() to handle nulls safely.
        if(analyticDto.viewDuration() != null) analytic.setViewDuration(analyticDto.viewDuration());
        if(analyticDto.ipAddress() != null) analytic.setIpAddress(analyticDto.ipAddress());
        if(analyticDto.userAgent() != null) analytic.setUserAgent(analyticDto.userAgent());
        // You might not allow updating watchedAt, depending on your requirements
        return analyticRepository.save(analytic);
    }

    @Transactional
    public void deleteAnalytic(Integer id) {
        if (!analyticRepository.existsById(id)) {
            throw new ResourceNotFoundException("Analytic with ID " + id + " not found");
        }
        analyticRepository.deleteById(id);
    }



    public AnalyticDto.Response convertToDto(Analytic analytic) {
        VideoDto.Response videoDto = null;
        if (analytic.getVideo() != null) {
            videoDto = videoService.convertToDto(analytic.getVideo()); // Use VideoService
        }

        UserDto.Response userDto = null;
        if (analytic.getUser() != null) {
            userDto = userService.convertToDto(analytic.getUser()); // Use UserService
        }

        return new AnalyticDto.Response(
                analytic.getId(),
                videoDto,
                userDto,
                analytic.getViewDuration(),
                analytic.getIpAddress(),
                analytic.getUserAgent(),
                analytic.getWatchedAt()
        );
    }
}