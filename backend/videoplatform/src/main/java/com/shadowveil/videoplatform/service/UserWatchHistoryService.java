package com.shadowveil.videoplatform.service;

import com.shadowveil.videoplatform.dto.UserWatchHistoryDto;
import com.shadowveil.videoplatform.entity.User;
import com.shadowveil.videoplatform.entity.UserWatchHistory;
import com.shadowveil.videoplatform.entity.Video;
import com.shadowveil.videoplatform.repository.UserWatchHistoryRepository;
import com.shadowveil.videoplatform.repository.UserRepository;
import com.shadowveil.videoplatform.repository.VideoRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class UserWatchHistoryService {

    private final UserWatchHistoryRepository userWatchHistoryRepository;
    private final UserRepository userRepository;
    private final VideoRepository videoRepository;

    @Autowired
    public UserWatchHistoryService(
            UserWatchHistoryRepository userWatchHistoryRepository,
            UserRepository userRepository,
            VideoRepository videoRepository) {
        this.userWatchHistoryRepository = userWatchHistoryRepository;
        this.userRepository = userRepository;
        this.videoRepository = videoRepository;
    }

    public List<UserWatchHistory> getAllWatchHistory() {
        return userWatchHistoryRepository.findAll();
    }

    public UserWatchHistory getWatchHistoryById(Integer id) {
        return userWatchHistoryRepository.findById(id).orElseThrow(()-> new EntityNotFoundException("User watch history not found with ID: "+ id));
    }

    public List<UserWatchHistory> getWatchHistoryByUserId(Integer userId) {
        return userWatchHistoryRepository.findByUserId(userId);
    }

    public List<UserWatchHistory> getWatchHistoryByVideoId(Integer videoId) {
        return userWatchHistoryRepository.findByVideoId(videoId);
    }
    public List<UserWatchHistory> getWatchHistoryByUserIdAndVideoId(Integer userId, Integer videoId){
        return userWatchHistoryRepository.findByUserIdAndVideoId(userId, videoId);
    }

    @Transactional
    public UserWatchHistory createWatchHistoryEntry(UserWatchHistoryDto.Request requestDto) {
        // Validate user and video
        User user = userRepository.findById(requestDto.userId())
                .orElseThrow(() -> new EntityNotFoundException("User with ID " + requestDto.userId() + " not found."));
        Video video = videoRepository.findById(requestDto.videoId())
                .orElseThrow(() -> new EntityNotFoundException("Video with ID " + requestDto.videoId() + " not found."));

        // Check for existing entry (and update it if found)
        Optional<UserWatchHistory> existingEntry = userWatchHistoryRepository.findOneByUserIdAndVideoId(
                requestDto.userId(), requestDto.videoId());

        UserWatchHistory historyEntry;
        if (existingEntry.isPresent()) {
            // Update existing entry
            historyEntry = existingEntry.get();
            // Only update durationWatched if provided in the DTO
            if(requestDto.durationWatched() != null) {
                historyEntry.setDurationWatched(requestDto.durationWatched());
            }
        } else {
            // Create new entry
            historyEntry = new UserWatchHistory();
            historyEntry.setUser(user);
            historyEntry.setVideo(video);
            // Only set durationWatched if provided in the DTO
            if(requestDto.durationWatched() != null){
                historyEntry.setDurationWatched(requestDto.durationWatched());
            }
        }

        return userWatchHistoryRepository.save(historyEntry);
    }

    @Transactional
    public UserWatchHistory updateWatchHistoryEntry(Integer id, UserWatchHistoryDto.UpdateRequest requestDto) {
        UserWatchHistory historyEntry = userWatchHistoryRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Watch history entry not found with id: " + id));

        // Update only durationWatched
        historyEntry.setDurationWatched(requestDto.durationWatched());

        return userWatchHistoryRepository.save(historyEntry);

    }

    @Transactional
    public void deleteWatchHistoryEntry(Integer id) {
        UserWatchHistory userWatchHistory = userWatchHistoryRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User watch history not found with id: " + id));
        userWatchHistoryRepository.delete(userWatchHistory);
    }
}