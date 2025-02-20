package com.shadowveil.videoplatform.repository;

import com.shadowveil.videoplatform.entity.Advertisement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdvertisementRepository extends JpaRepository<Advertisement, Integer> {
    // You can add custom query methods here if needed, e.g.,
    // List<Advertisement> findByTitleContaining(String title);
}