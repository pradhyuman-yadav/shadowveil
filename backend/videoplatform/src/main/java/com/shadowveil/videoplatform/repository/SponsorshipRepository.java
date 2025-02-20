// (No changes needed, well-defined)
package com.shadowveil.videoplatform.repository;

import com.shadowveil.videoplatform.entity.Sponsorship;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface SponsorshipRepository extends JpaRepository<Sponsorship, Integer> {
    List<Sponsorship> findByVideoId(Integer videoId);
}