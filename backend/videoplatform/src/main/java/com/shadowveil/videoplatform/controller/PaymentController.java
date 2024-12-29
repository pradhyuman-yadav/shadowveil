package com.shadowveil.videoplatform.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/payments")
class PaymentController {

    @PostMapping("/create-session")
    public ResponseEntity<String> createPaymentSession() {
        return ResponseEntity.ok("Payment session created");
    }

    @GetMapping("/subscriptions")
    public ResponseEntity<String> getSubscriptions() {
        return ResponseEntity.ok("User subscription details");
    }

    @PostMapping("/cancel")
    public ResponseEntity<String> cancelSubscription() {
        return ResponseEntity.ok("Subscription cancelled");
    }
}

