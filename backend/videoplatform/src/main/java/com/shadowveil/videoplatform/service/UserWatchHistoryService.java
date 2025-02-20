// File: src/main/java/com/shadowveil/videoplatform/service/UserWatchHistoryService.java
package com.shadowveil.videoplatform.service;

import com.shadowveil.videoplatform.entity.User;
import com.shadowveil.videoplatform.entity.UserWatchHistory;
import com.shadowveil.videoplatform.entity.Video;
import com.shadowveil.videoplatform.repository.UserWatchHistoryRepository;
import com.shadowveil.videoplatform.repository.UserRepository;
import com.shadowveil.videoplatform.repository.VideoRepository;
import org.springframework.beans.factory.annotation.Autowired;
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

    public Optional<UserWatchHistory> getWatchHistoryById(Integer id) {
        return userWatchHistoryRepository.findById(id);
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
    public UserWatchHistory createWatchHistoryEntry(UserWatchHistory historyEntry) {
        // Validate user and video
        Optional<User> user = userRepository.findById(historyEntry.getUser().getId());
        if (user.isEmpty()) {
            throw new IllegalArgumentException("User with ID " + historyEntry.getUser().getId() + " not found.");
        }
        Optional<Video> video = videoRepository.findById(historyEntry.getVideo().getId());
        if (video.isEmpty()) {
            throw new IllegalArgumentException("Video with ID " + historyEntry.getVideo().getId() + " not found.");
        }

        // Check for existing entry (optional - depends on your requirements)
        Optional<UserWatchHistory> existingEntry = userWatchHistoryRepository.findOneByUserIdAndVideoId(
                historyEntry.getUser().getId(), historyEntry.getVideo().getId());
        if (existingEntry.isPresent()) {
            // You can either:
            // 1. Throw an exception (prevent duplicates)
            // throw new IllegalArgumentException("User has already watched this video.");

            // 2. Update the existing entry (more common for watch history)
            UserWatchHistory current = existingEntry.get();
            current.setDurationWatched(historyEntry.getDurationWatched());
            // current.setWatchedAt(Instant.now()); // Removed to use DB default.
            return userWatchHistoryRepository.save(current);
        }

        historyEntry.setUser(user.get());
        historyEntry.setVideo(video.get());
        //historyEntry.setWatchedAt(Instant.now()); // Removed for default by DB
        return userWatchHistoryRepository.save(historyEntry);
    }

    @Transactional
    public UserWatchHistory updateWatchHistoryEntry(Integer id, UserWatchHistory historyDetails) {
        Optional<UserWatchHistory> optionalHistoryEntry = userWatchHistoryRepository.findById(id);
        if (optionalHistoryEntry.isPresent()) {
            UserWatchHistory existingEntry = optionalHistoryEntry.get();

            // Validate user and video if they are being changed
            if(historyDetails.getUser() != null && !historyDetails.getUser().getId().equals(existingEntry.getUser().getId())){
                Optional<User> user = userRepository.findById(historyDetails.getUser().getId());
                if(user.isEmpty()){
                    throw new IllegalArgumentException("User with ID " + historyDetails.getUser().getId()+ " does not exists");
                }
                // Check for duplicate on update
                Optional<UserWatchHistory> existingEntryNewUser = userWatchHistoryRepository.findOneByUserIdAndVideoId(historyDetails.getUser().getId(), existingEntry.getVideo().getId());
                if(existingEntryNewUser.isPresent()){
                    throw new IllegalArgumentException("User has already watch history for this video");
                }
                existingEntry.setUser(user.get());
            }
            if(historyDetails.getVideo() != null && !historyDetails.getVideo().getId().equals(existingEntry.getVideo().getId())){
                Optional<Video> video = videoRepository.findById(historyDetails.getVideo().getId());
                if(video.isEmpty()){
                    throw new IllegalArgumentException("Video with ID " + historyDetails.getVideo().getId() + " does not exists");
                }
                // Check for duplicate on update
                Optional<UserWatchHistory> existingEntryNewVideo = userWatchHistoryRepository.findOneByUserIdAndVideoId(existingEntry.getUser().getId(), historyDetails.getVideo().getId());
                if(existingEntryNewVideo.isPresent()){
                    throw new IllegalArgumentException("User has already watch history for this video");
                }
                existingEntry.setVideo(video.get());
            }
            existingEntry.setDurationWatched(historyDetails.getDurationWatched());
            // You might or might not allow updating watchedAt
            return userWatchHistoryRepository.save(existingEntry);

        } else {
            return null; // Or throw an exception
        }
    }

    @Transactional
    public void deleteWatchHistoryEntry(Integer id) {
        if(userWatchHistoryRepository.existsById(id)){
            userWatchHistoryRepository.deleteById(id);
        }
        else{
            throw new IllegalArgumentException("User Watch History with ID " + id + " does not exists");
        }
    }
}