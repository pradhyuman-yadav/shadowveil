// (No changes needed here, it's well-defined)
package com.shadowveil.videoplatform.repository;

import com.shadowveil.videoplatform.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Integer> {

    List<Payment> findByUser_Id(Integer userId);
    List<Payment> findBySubscription_Id(Integer subscriptionId);
    // Consider adding pagination:
    // List<Payment> findByUser_Id(Integer userId, Pageable pageable);
}