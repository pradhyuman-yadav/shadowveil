package com.shadowveil.videoplatform.service;

import com.shadowveil.videoplatform.entity.Subscription;
import com.shadowveil.videoplatform.repository.SubscriptionRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SubscriptionService {

    private final SubscriptionRepository subscriptionRepository;

    public SubscriptionService(SubscriptionRepository subscriptionRepository) {
        this.subscriptionRepository = subscriptionRepository;
    }

    // Retrieve all subscriptions.
    public List<Subscription> getAllSubscriptions() {
        return subscriptionRepository.findAll();
    }

    // Retrieve a single subscription by its ID.
    public Optional<Subscription> getSubscriptionById(Integer id) {
        return subscriptionRepository.findById(id);
    }

    // Create a new subscription.
    public Subscription createSubscription(Subscription subscription) {
        return subscriptionRepository.save(subscription);
    }

    // Update an existing subscription.
    public Subscription updateSubscription(Integer id, Subscription subscriptionDetails) {
        return subscriptionRepository.findById(id).map(subscription -> {
            subscription.setUser(subscriptionDetails.getUser());
            subscription.setPlan(subscriptionDetails.getPlan());
            subscription.setStartDate(subscriptionDetails.getStartDate());
            subscription.setEndDate(subscriptionDetails.getEndDate());
            subscription.setStatus(subscriptionDetails.getStatus());
            return subscriptionRepository.save(subscription);
        }).orElseThrow(() -> new RuntimeException("Subscription not found with id " + id));
    }

    // Delete a subscription by its ID.
    public void deleteSubscription(Integer id) {
        subscriptionRepository.deleteById(id);
    }

    // Retrieve subscriptions by a given user ID.
    public List<Subscription> getSubscriptionsByUserId(Integer userId) {
        return subscriptionRepository.findByUser_Id(userId);
    }
}
