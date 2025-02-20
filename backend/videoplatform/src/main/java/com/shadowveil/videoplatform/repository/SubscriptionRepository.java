//(No changes needed, it is well-defined)
package com.shadowveil.videoplatform.repository;

import com.shadowveil.videoplatform.entity.Subscription;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SubscriptionRepository extends JpaRepository<Subscription, Integer> {
    List<Subscription> findByUser_Id(Integer userId);
    // Add pagination if needed:
    // List<Subscription> findByUser_Id(Integer userId, Pageable pageable);
}