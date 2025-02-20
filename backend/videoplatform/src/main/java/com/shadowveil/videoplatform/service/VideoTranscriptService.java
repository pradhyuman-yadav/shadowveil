// File: src/main/java/com/shadowveil/videoplatform/service/VideoTranscriptService.java
package com.shadowveil.videoplatform.service;

import com.shadowveil.videoplatform.entity.Video;
import com.shadowveil.videoplatform.entity.VideoTranscript;
import com.shadowveil.videoplatform.repository.VideoRepository;
import com.shadowveil.videoplatform.repository.VideoTranscriptRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Service
public class VideoTranscriptService {

    private final VideoTranscriptRepository videoTranscriptRepository;
    private final VideoRepository videoRepository; // Inject VideoRepository

    @Autowired
    public VideoTranscriptService(VideoTranscriptRepository videoTranscriptRepository, VideoRepository videoRepository) {
        this.videoTranscriptRepository = videoTranscriptRepository;
        this.videoRepository = videoRepository;
    }

    public List<VideoTranscript> getAllTranscripts() {
        return videoTranscriptRepository.findAll();
    }

    public Optional<VideoTranscript> getTranscriptById(Integer id) {
        return videoTranscriptRepository.findById(id);
    }

    public List<VideoTranscript> getTranscriptsByVideoId(Integer videoId) {
        return videoTranscriptRepository.findByVideoId(videoId);
    }
    public List<VideoTranscript> getTranscriptsByVideoIdAndLanguage(Integer videoId, String language){
        return videoTranscriptRepository.findByVideoIdAndLanguage(videoId, language);
    }

    @Transactional
    public VideoTranscript createTranscript(VideoTranscript transcript) {
        // Validate that the video exists
        Optional<Video> video = videoRepository.findById(transcript.getVideo().getId());
        if (video.isEmpty()) {
            throw new IllegalArgumentException("Video with ID " + transcript.getVideo().getId() + " not found.");
        }

        transcript.setVideo(video.get());
        transcript.setUpdatedAt(Instant.now());
        return videoTranscriptRepository.save(transcript);
    }

    @Transactional
    public VideoTranscript updateTranscript(Integer id, VideoTranscript transcriptDetails) {
        Optional<VideoTranscript> optionalTranscript = videoTranscriptRepository.findById(id);
        if (optionalTranscript.isPresent()) {
            VideoTranscript existingTranscript = optionalTranscript.get();
            // Validate video if video is being changed
            if(transcriptDetails.getVideo() != null && !transcriptDetails.getVideo().getId().equals(existingTranscript.getVideo().getId())){
                Optional<Video> video = videoRepository.findById(transcriptDetails.getVideo().getId());
                if(video.isEmpty()){
                    throw new IllegalArgumentException("Video with ID " + transcriptDetails.getVideo().getId()+ " does not exists");
                }
                existingTranscript.setVideo(video.get());
            }

            existingTranscript.setLanguage(transcriptDetails.getLanguage());
            existingTranscript.setTranscript(transcriptDetails.getTranscript());
            existingTranscript.setUpdatedAt(Instant.now()); // Update the timestamp
            return videoTranscriptRepository.save(existingTranscript);

        } else {
            return null; // Or throw an exception
        }
    }

    @Transactional
    public void deleteTranscript(Integer id) {
        if(videoTranscriptRepository.existsById(id)){
            videoTranscriptRepository.deleteById(id);
        }
        else{
            throw new IllegalArgumentException("Video Transcript with ID " + id+ " does not exists");
        }

    }
}