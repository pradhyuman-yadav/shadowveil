// File: src/main/java/com/shadowveil/videoplatform/service/NotificationService.java
package com.shadowveil.videoplatform.service;

import com.shadowveil.videoplatform.entity.Notification;
import com.shadowveil.videoplatform.entity.User;
import com.shadowveil.videoplatform.repository.NotificationRepository;
import com.shadowveil.videoplatform.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class NotificationService {

    private final NotificationRepository notificationRepository;
    private final UserRepository userRepository; // Inject UserRepository

    @Autowired
    public NotificationService(NotificationRepository notificationRepository, UserRepository userRepository) {
        this.notificationRepository = notificationRepository;
        this.userRepository = userRepository;
    }

    public List<Notification> getAllNotifications() {
        return notificationRepository.findAll();
    }

    public Optional<Notification> getNotificationById(Integer id) {
        return notificationRepository.findById(id);
    }

    public List<Notification> getNotificationsByUserId(Integer userId) {
        return notificationRepository.findByUserId(userId);
    }

    public List<Notification> getNotificationsByUserIdAndIsRead(Integer userId, Boolean isRead) {
        return notificationRepository.findByUserIdAndIsRead(userId, isRead);
    }

    @Transactional
    public Notification createNotification(Notification notification) {
        // Validate that the user exists
        Optional<User> user = userRepository.findById(notification.getUser().getId());
        if (user.isEmpty()) {
            throw new IllegalArgumentException("User with ID " + notification.getUser().getId() + " not found.");
        }

        notification.setUser(user.get());
        notification.setIsRead(false); // Set isRead to false by default
        // notification.setCreatedAt(Instant.now());// Removed to use DB default
        return notificationRepository.save(notification);
    }

    @Transactional
    public Notification updateNotification(Integer id, Notification notificationDetails) {
        Optional<Notification> optionalNotification = notificationRepository.findById(id);
        if (optionalNotification.isPresent()) {
            Notification existingNotification = optionalNotification.get();

            // Validate user, if user is being changed
            if(notificationDetails.getUser() != null && !notificationDetails.getUser().getId().equals(existingNotification.getUser().getId())){
                Optional<User> user = userRepository.findById(notificationDetails.getUser().getId());
                if(user.isEmpty()){
                    throw new IllegalArgumentException("User with ID " + notificationDetails.getUser().getId() + " does not exists");
                }
                existingNotification.setUser(user.get());
            }

            existingNotification.setMessage(notificationDetails.getMessage());
            existingNotification.setIsRead(notificationDetails.getIsRead()); // Allow updating isRead
            // You typically wouldn't update createdAt

            return notificationRepository.save(existingNotification);
        } else {
            return null; // Or throw an exception
        }
    }

    @Transactional
    public void deleteNotification(Integer id) {
        if(notificationRepository.existsById(id)){
            notificationRepository.deleteById(id);
        }
        else{
            throw new IllegalArgumentException("Notification with ID " + id + " does not exists");
        }

    }
}