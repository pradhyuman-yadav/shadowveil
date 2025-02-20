// src/main/java/com/shadowveil/videoplatform/repository/NotificationRepository.java
package com.shadowveil.videoplatform.repository;

import com.shadowveil.videoplatform.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Integer> {
    List<Notification> findByUserId(Integer userId);
    List<Notification> findByUserIdAndIsRead(Integer userId, Boolean isRead);
    // You could add pagination here if you expect many notifications per user:
    // List<Notification> findByUserId(Integer userId, Pageable pageable);
}