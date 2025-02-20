// File: src/main/java/com/shadowveil/videoplatform/repository/UserWatchHistoryRepository.java
package com.shadowveil.videoplatform.repository;

import com.shadowveil.videoplatform.entity.UserWatchHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserWatchHistoryRepository extends JpaRepository<UserWatchHistory, Integer> {
    List<UserWatchHistory> findByUserId(Integer userId);
    List<UserWatchHistory> findByVideoId(Integer videoId);
    List<UserWatchHistory> findByUserIdAndVideoId(Integer userId, Integer videoId);

    // Optional, but useful for checking/preventing duplicates:
    Optional<UserWatchHistory> findOneByUserIdAndVideoId(Integer userId, Integer videoId);
}