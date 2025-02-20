package com.shadowveil.videoplatform.service;

import com.shadowveil.videoplatform.dto.PaymentDto;
import com.shadowveil.videoplatform.entity.Payment;
import com.shadowveil.videoplatform.entity.Subscription;
import com.shadowveil.videoplatform.entity.User;
import com.shadowveil.videoplatform.repository.PaymentRepository;
import com.shadowveil.videoplatform.repository.SubscriptionRepository;
import com.shadowveil.videoplatform.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final UserRepository userRepository; // Inject UserRepository
    private final SubscriptionRepository subscriptionRepository; // Inject

    @Autowired
    public PaymentService(PaymentRepository paymentRepository, UserRepository userRepository, SubscriptionRepository subscriptionRepository) {
        this.paymentRepository = paymentRepository;
        this.userRepository = userRepository;
        this.subscriptionRepository = subscriptionRepository;
    }

    public List<Payment> getAllPayments() {
        return paymentRepository.findAll();
    }

    public Payment getPaymentById(Integer id) {
        return paymentRepository.findById(id).orElseThrow(()-> new EntityNotFoundException("Payment not found with ID: "+ id));
    }

    @Transactional
    public Payment createPayment(PaymentDto.Request requestDto) {
        Payment payment = new Payment();

        // Handle optional user and subscription
        if (requestDto.userId() != null) {
            User user = userRepository.findById(requestDto.userId())
                    .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + requestDto.userId()));
            payment.setUser(user);
        }
        if (requestDto.subscriptionId() != null) {
            Subscription subscription = subscriptionRepository.findById(requestDto.subscriptionId())
                    .orElseThrow(() -> new EntityNotFoundException("Subscription not found with id: " + requestDto.subscriptionId()));
            payment.setSubscription(subscription);
        }

        payment.setAmount(requestDto.amount());
        payment.setCurrency(requestDto.currency());
        payment.setPaymentMethod(requestDto.paymentMethod());
        payment.setStatus("pending"); // Set initial status to pending

        return paymentRepository.save(payment);
    }

    @Transactional
    public Payment updatePayment(Integer id, PaymentDto.UpdateStatusRequest requestDto) {
        Payment payment = paymentRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Payment with id " + id + " not found."));
        payment.setStatus(requestDto.status());
        return paymentRepository.save(payment);

    }

    @Transactional // Added Transactional to the delete method.
    public void deletePayment(Integer id) {
        Payment payment = paymentRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Payment not found with id: " + id));
        paymentRepository.delete(payment);
    }

    public List<Payment> getPaymentsByUserId(Integer userId) {
        return paymentRepository.findByUser_Id(userId);
    }

    public List<Payment> getPaymentsBySubscriptionId(Integer subscriptionId) {
        return paymentRepository.findBySubscription_Id(subscriptionId);
    }
}