package com.shadowveil.videoplatform.controller;

import com.shadowveil.videoplatform.dto.SubscriptionDto;
import com.shadowveil.videoplatform.dto.UserDto;
import com.shadowveil.videoplatform.entity.Subscription;
import com.shadowveil.videoplatform.service.SubscriptionService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/subscriptions")
public class SubscriptionController {

    private final SubscriptionService subscriptionService;

    @Autowired
    public SubscriptionController(SubscriptionService subscriptionService) {
        this.subscriptionService = subscriptionService;
    }

    @GetMapping
    public ResponseEntity<List<SubscriptionDto.Response>> getAllSubscriptions() {
        List<Subscription> subscriptions = subscriptionService.getAllSubscriptions();
        List<SubscriptionDto.Response> responseDtos = subscriptions.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(responseDtos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<SubscriptionDto.Response> getSubscriptionById(@PathVariable Integer id) {
        Subscription subscription = subscriptionService.getSubscriptionById(id);
        return ResponseEntity.ok(convertToDto(subscription));
    }

    @PostMapping
    public ResponseEntity<SubscriptionDto.Response> createSubscription(@Valid @RequestBody SubscriptionDto.Request requestDto) {
        Subscription createdSubscription = subscriptionService.createSubscription(requestDto);
        return new ResponseEntity<>(convertToDto(createdSubscription), HttpStatus.CREATED);
    }

    // PUT /api/subscriptions/{id}
    @PutMapping("/{id}")
    public ResponseEntity<SubscriptionDto.Response> updateSubscription(
            @PathVariable Integer id,
            @Valid @RequestBody SubscriptionDto.UpdateRequest updateRequestDto) { // Use UpdateRequest DTO
        Subscription updatedSubscription = subscriptionService.updateSubscription(id, updateRequestDto);
        return ResponseEntity.ok(convertToDto(updatedSubscription));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSubscription(@PathVariable Integer id) {
        subscriptionService.deleteSubscription(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<SubscriptionDto.Response>> getSubscriptionsByUserId(@PathVariable Integer userId) {
        List<Subscription> subscriptions = subscriptionService.getSubscriptionsByUserId(userId);
        List<SubscriptionDto.Response> responseDtos = subscriptions.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(responseDtos);
    }
    // Helper method to convert Subscription entity to SubscriptionDto.Response
    private SubscriptionDto.Response convertToDto(Subscription subscription) {
        UserDto.Response userDto = (subscription.getUser() != null) ?
                new UserDto.Response(subscription.getUser().getId(), subscription.getUser().getUsername(), subscription.getUser().getEmail(), subscription.getUser().getRole(), subscription.getUser().getCreatedAt(), subscription.getUser().getUpdatedAt()) :
                null; // Handle potentially null user

        return new SubscriptionDto.Response(
                subscription.getId(),
                userDto,
                subscription.getPlan(),
                subscription.getStartDate(),
                subscription.getEndDate(), // Correct getter
                subscription.getStatus()
        );
    }
    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<String> handleEntityNotFoundException(EntityNotFoundException ex){
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
    }
}