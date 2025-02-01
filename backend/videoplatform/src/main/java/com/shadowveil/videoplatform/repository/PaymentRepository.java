package com.shadowveil.videoplatform.repository;

import com.shadowveil.videoplatform.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Integer> {

    // Retrieve payments for a specific user.
    List<Payment> findByUser_Id(Integer userId);

    // Retrieve payments for a specific subscription.
    List<Payment> findBySubscription_Id(Integer subscriptionId);
}
