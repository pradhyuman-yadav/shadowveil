package com.shadowveil.videoplatform.service;

import com.shadowveil.videoplatform.dto.SubscriptionDto;
import com.shadowveil.videoplatform.entity.Subscription;
import com.shadowveil.videoplatform.entity.User;
import com.shadowveil.videoplatform.repository.SubscriptionRepository;
import com.shadowveil.videoplatform.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Service
public class SubscriptionService {

    private final SubscriptionRepository subscriptionRepository;
    private final UserRepository userRepository; // Inject UserRepository

    @Autowired
    public SubscriptionService(SubscriptionRepository subscriptionRepository, UserRepository userRepository) {
        this.subscriptionRepository = subscriptionRepository;
        this.userRepository = userRepository;
    }

    public List<Subscription> getAllSubscriptions() {
        return subscriptionRepository.findAll();
    }

    public Subscription getSubscriptionById(Integer id) {
        return subscriptionRepository.findById(id).orElseThrow(()-> new EntityNotFoundException("Subscription not found with ID: "+ id));
    }

    @Transactional
    public Subscription createSubscription(SubscriptionDto.Request requestDto) {
        Subscription subscription = new Subscription();

        // Handle optional user association
        if (requestDto.userId() != null) {
            User user = userRepository.findById(requestDto.userId())
                    .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + requestDto.userId()));
            subscription.setUser(user);
        }

        subscription.setPlan(requestDto.plan());
        subscription.setStartDate(requestDto.startDate());
        // Calculate endDate, if needed (e.g., one month from startDate)
        // Example: subscription.setEndDate(requestDto.startDate().plus(1, ChronoUnit.MONTHS));
        subscription.setStatus("active"); // Set initial status

        return subscriptionRepository.save(subscription);
    }

    @Transactional
    public Subscription updateSubscription(Integer id, SubscriptionDto.UpdateRequest updateRequestDto) {
        Subscription subscription = subscriptionRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Subscription not found with id: " + id));

        // Update fields.  Only update what's provided in the DTO.
        subscription.setPlan(updateRequestDto.plan());
        subscription.setStartDate(updateRequestDto.startDate());

        // Handle optional endDate update
        if (updateRequestDto.endDate() != null) {
            subscription.setEndDate(updateRequestDto.endDate());
        }

        subscription.setStatus(updateRequestDto.status());


        return subscriptionRepository.save(subscription);
    }

    @Transactional // Added Transactional
    public void deleteSubscription(Integer id) {
        Subscription subscription = subscriptionRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Subscription not found with id: " + id));
        subscriptionRepository.delete(subscription);
    }

    public List<Subscription> getSubscriptionsByUserId(Integer userId) {
        return subscriptionRepository.findByUser_Id(userId);
    }
}