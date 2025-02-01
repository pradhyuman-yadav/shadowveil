package com.shadowveil.videoplatform.repository;

import com.shadowveil.videoplatform.entity.Subscription;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SubscriptionRepository extends JpaRepository<Subscription, Integer> {

    // Retrieve subscriptions for a specific user.
    List<Subscription> findByUser_Id(Integer userId);
}
