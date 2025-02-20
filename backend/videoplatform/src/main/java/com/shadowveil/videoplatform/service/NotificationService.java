// src/main/java/com/shadowveil/videoplatform/service/NotificationService.java
package com.shadowveil.videoplatform.service;

import com.shadowveil.videoplatform.dto.NotificationDto;
import com.shadowveil.videoplatform.entity.Notification;
import com.shadowveil.videoplatform.entity.User;
import com.shadowveil.videoplatform.repository.NotificationRepository;
import com.shadowveil.videoplatform.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class NotificationService {

    private final NotificationRepository notificationRepository;
    private final UserRepository userRepository;

    @Autowired
    public NotificationService(NotificationRepository notificationRepository, UserRepository userRepository) {
        this.notificationRepository = notificationRepository;
        this.userRepository = userRepository;
    }

    public List<Notification> getAllNotifications() {
        return notificationRepository.findAll();
    }

    public Notification getNotificationById(Integer id) {
        return notificationRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Notification not found with id: " + id));
    }

    public List<Notification> getNotificationsByUserId(Integer userId) {
        return notificationRepository.findByUserId(userId);
    }

    public List<Notification> getNotificationsByUserIdAndIsRead(Integer userId, Boolean isRead) {
        return notificationRepository.findByUserIdAndIsRead(userId, isRead);
    }

    @Transactional
    public Notification createNotification(NotificationDto.Request requestDto) {
        User user = userRepository.findById(requestDto.userId())
                .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + requestDto.userId()));

        Notification notification = new Notification();
        notification.setUser(user);
        notification.setMessage(requestDto.message());
        notification.setIsRead(false); // Always false on creation
        // createdAt is handled by the database

        return notificationRepository.save(notification);
    }


    @Transactional
    public Notification updateNotification(Integer id, NotificationDto.UpdateIsReadRequest updateDto) {
        Notification notification = notificationRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Notification not found with id: " + id));

        notification.setIsRead(updateDto.isRead());
        return notificationRepository.save(notification);
    }
    @Transactional
    public void deleteNotification(Integer id) {
        Notification notification = notificationRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Notification not found with id: " + id));
        notificationRepository.delete(notification);
    }
}