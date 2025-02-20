//(No change needed, well-defined)
package com.shadowveil.videoplatform.repository;

import com.shadowveil.videoplatform.entity.VideoTranscript;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface VideoTranscriptRepository extends JpaRepository<VideoTranscript, Integer> {
    List<VideoTranscript> findByVideoId(Integer videoId);
    List<VideoTranscript> findByVideoIdAndLanguage(Integer videoId, String language);
}