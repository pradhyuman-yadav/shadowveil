// src/main/java/com/shadowveil/videoplatform/controller/NotificationController.java
package com.shadowveil.videoplatform.controller;

import com.shadowveil.videoplatform.dto.NotificationDto;
import com.shadowveil.videoplatform.dto.UserDto; // Import UserDto
import com.shadowveil.videoplatform.entity.Notification;
import com.shadowveil.videoplatform.service.NotificationService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/notifications")
public class NotificationController {

    private final NotificationService notificationService;

    @Autowired
    public NotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @GetMapping
    public ResponseEntity<List<NotificationDto.Response>> getAllNotifications() {
        List<Notification> notifications = notificationService.getAllNotifications();
        List<NotificationDto.Response> responseDtos = notifications.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
        return new ResponseEntity<>(responseDtos, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<NotificationDto.Response> getNotificationById(@PathVariable Integer id) {
        Notification notification = notificationService.getNotificationById(id);
        return new ResponseEntity<>(convertToDto(notification), HttpStatus.OK);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<NotificationDto.Response>> getNotificationsByUserId(@PathVariable Integer userId) {
        List<Notification> notifications = notificationService.getNotificationsByUserId(userId);
        List<NotificationDto.Response> responseDtos = notifications.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
        return new ResponseEntity<>(responseDtos, HttpStatus.OK);
    }

    @GetMapping("/user/{userId}/status/{isRead}")
    public ResponseEntity<List<NotificationDto.Response>> getNotificationsByUserIdAndReadStatus(
            @PathVariable Integer userId,
            @PathVariable Boolean isRead) {
        List<Notification> notifications = notificationService.getNotificationsByUserIdAndIsRead(userId, isRead);
        List<NotificationDto.Response> responseDtos = notifications.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
        return new ResponseEntity<>(responseDtos, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<NotificationDto.Response> createNotification(@Valid @RequestBody NotificationDto.Request requestDto) {
        Notification notification = notificationService.createNotification(requestDto);
        return new ResponseEntity<>(convertToDto(notification), HttpStatus.CREATED);
    }

    @PutMapping("/{id}/read") // More specific endpoint for marking as read
    public ResponseEntity<NotificationDto.Response> updateNotificationIsRead(
            @PathVariable Integer id,
            @Valid @RequestBody NotificationDto.UpdateIsReadRequest updateDto) {
        Notification updatedNotification = notificationService.updateNotification(id, updateDto);
        return new ResponseEntity<>(convertToDto(updatedNotification), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteNotification(@PathVariable Integer id) {
        notificationService.deleteNotification(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    // Helper method to convert Notification entity to NotificationDto.Response
    private NotificationDto.Response convertToDto(Notification notification) {
        // Correctly create the nested UserDto.Response
        UserDto.Response userDto = new UserDto.Response(
                notification.getUser().getId(),
                notification.getUser().getUsername(),
                notification.getUser().getEmail(),
                notification.getUser().getRole(), // Include the role
                notification.getUser().getCreatedAt(),
                notification.getUser().getUpdatedAt()
        );

        return new NotificationDto.Response(
                notification.getId(),
                userDto,
                notification.getMessage(),
                notification.getIsRead(),
                notification.getCreatedAt()
        );
    }

    // Exception handler for EntityNotFoundException
    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<String> handleEntityNotFoundException(EntityNotFoundException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
    }
}