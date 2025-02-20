// src/main/java/com/shadowveil/videoplatform/controller/PaymentController.java
package com.shadowveil.videoplatform.controller;

import com.shadowveil.videoplatform.dto.PaymentDto;
import com.shadowveil.videoplatform.dto.SubscriptionDto;
import com.shadowveil.videoplatform.dto.UserDto;
import com.shadowveil.videoplatform.entity.Payment;
import com.shadowveil.videoplatform.service.PaymentService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/payments")
public class PaymentController {

    private final PaymentService paymentService;

    @Autowired
    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @GetMapping
    public ResponseEntity<List<PaymentDto.Response>> getAllPayments() {
        List<Payment> payments = paymentService.getAllPayments();
        List<PaymentDto.Response> responseDtos = payments.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(responseDtos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PaymentDto.Response> getPaymentById(@PathVariable Integer id) {
        Payment payment = paymentService.getPaymentById(id);
        return ResponseEntity.ok(convertToDto(payment));
    }

    @PostMapping
    public ResponseEntity<PaymentDto.Response> createPayment(@Valid @RequestBody PaymentDto.Request requestDto) {
        Payment createdPayment = paymentService.createPayment(requestDto);
        return new ResponseEntity<>(convertToDto(createdPayment), HttpStatus.CREATED);
    }

    // PUT /api/payments/{id}/status  <- More specific endpoint
    @PutMapping("/{id}/status")
    public ResponseEntity<PaymentDto.Response> updatePaymentStatus(
            @PathVariable Integer id,
            @Valid @RequestBody PaymentDto.UpdateStatusRequest requestDto) {
        Payment updatedPayment = paymentService.updatePayment(id, requestDto);
        return ResponseEntity.ok(convertToDto(updatedPayment));
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePayment(@PathVariable Integer id) {
        paymentService.deletePayment(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<PaymentDto.Response>> getPaymentsByUserId(@PathVariable Integer userId) {
        List<Payment> payments = paymentService.getPaymentsByUserId(userId);
        List<PaymentDto.Response> responseDtos = payments.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(responseDtos);
    }

    @GetMapping("/subscription/{subscriptionId}")
    public ResponseEntity<List<PaymentDto.Response>> getPaymentsBySubscriptionId(@PathVariable Integer subscriptionId) {
        List<Payment> payments = paymentService.getPaymentsBySubscriptionId(subscriptionId);
        List<PaymentDto.Response> responseDtos = payments.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(responseDtos);
    }

    // Helper method to convert Payment entity to PaymentDto.Response
    private PaymentDto.Response convertToDto(Payment payment) {
        // 1. Handle the UserDto.Response (potentially null)
        UserDto.Response userDto = (payment.getUser() != null) ?
                new UserDto.Response(
                        payment.getUser().getId(),
                        payment.getUser().getUsername(),
                        payment.getUser().getEmail(),
                        payment.getUser().getRole(),
                        payment.getUser().getCreatedAt(),
                        payment.getUser().getUpdatedAt()
                ) : null;

        // 2. Handle the SubscriptionDto.Response (potentially null)
        SubscriptionDto.Response subscriptionDto = (payment.getSubscription() != null) ?
                new SubscriptionDto.Response(
                        payment.getSubscription().getId(),
                        null, //  We don't have UserDto Here.
                        payment.getSubscription().getPlan(),
                        payment.getSubscription().getStartDate(),
                        payment.getSubscription().getEndDate(),
                        payment.getSubscription().getStatus()
                ) : null;

        // 3. Create and return the PaymentDto.Response
        return new PaymentDto.Response(
                payment.getId(),
                userDto,         // The correctly created UserDto.Response
                subscriptionDto, // The correctly created SubscriptionDto.Response
                payment.getAmount(),
                payment.getCurrency(),
                payment.getStatus(),
                payment.getPaymentMethod(),
                payment.getCreatedAt()
        );
    }
    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<String> handleEntityNotFoundException(EntityNotFoundException ex){
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
    }
}