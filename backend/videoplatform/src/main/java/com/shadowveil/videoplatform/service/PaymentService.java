package com.shadowveil.videoplatform.service;

import com.shadowveil.videoplatform.entity.Payment;
import com.shadowveil.videoplatform.repository.PaymentRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PaymentService {

    private final PaymentRepository paymentRepository;

    public PaymentService(PaymentRepository paymentRepository) {
        this.paymentRepository = paymentRepository;
    }

    // Retrieve all payments.
    public List<Payment> getAllPayments() {
        return paymentRepository.findAll();
    }

    // Retrieve a payment by its ID.
    public Optional<Payment> getPaymentById(Integer id) {
        return paymentRepository.findById(id);
    }

    // Create a new payment.
    public Payment createPayment(Payment payment) {
        // Additional business logic (if any) goes here.
        return paymentRepository.save(payment);
    }

    // Update an existing payment.
    public Payment updatePayment(Integer id, Payment paymentDetails) {
        return paymentRepository.findById(id).map(payment -> {
            payment.setUser(paymentDetails.getUser());
            payment.setSubscription(paymentDetails.getSubscription());
            payment.setAmount(paymentDetails.getAmount());
            payment.setCurrency(paymentDetails.getCurrency());
            payment.setStatus(paymentDetails.getStatus());
            payment.setPaymentMethod(paymentDetails.getPaymentMethod());
            // Usually, the createdAt timestamp is not updated. Adjust if necessary.
            return paymentRepository.save(payment);
        }).orElseThrow(() -> new RuntimeException("Payment not found with id " + id));
    }

    // Delete a payment by its ID.
    public void deletePayment(Integer id) {
        paymentRepository.deleteById(id);
    }

    // Retrieve payments by a given user ID.
    public List<Payment> getPaymentsByUserId(Integer userId) {
        return paymentRepository.findByUser_Id(userId);
    }

    // Retrieve payments by a given subscription ID.
    public List<Payment> getPaymentsBySubscriptionId(Integer subscriptionId) {
        return paymentRepository.findBySubscription_Id(subscriptionId);
    }
}
