//(No changes needed, well defined)
package com.shadowveil.videoplatform.repository;

import com.shadowveil.videoplatform.entity.VideoSubtitle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface VideoSubtitleRepository extends JpaRepository<VideoSubtitle, Integer> {
    List<VideoSubtitle> findByVideoId(Integer videoId);
    List<VideoSubtitle> findByVideoIdAndLanguage(Integer videoId, String language);
}