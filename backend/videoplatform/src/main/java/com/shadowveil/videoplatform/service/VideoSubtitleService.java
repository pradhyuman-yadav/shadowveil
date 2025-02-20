// File: src/main/java/com/shadowveil/videoplatform/service/VideoSubtitleService.java
package com.shadowveil.videoplatform.service;

import com.shadowveil.videoplatform.entity.Video;
import com.shadowveil.videoplatform.entity.VideoSubtitle;
import com.shadowveil.videoplatform.repository.VideoRepository;
import com.shadowveil.videoplatform.repository.VideoSubtitleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class VideoSubtitleService {

    private final VideoSubtitleRepository videoSubtitleRepository;
    private final VideoRepository videoRepository; // Inject VideoRepository

    @Autowired
    public VideoSubtitleService(VideoSubtitleRepository videoSubtitleRepository, VideoRepository videoRepository) {
        this.videoSubtitleRepository = videoSubtitleRepository;
        this.videoRepository = videoRepository;
    }

    public List<VideoSubtitle> getAllSubtitles() {
        return videoSubtitleRepository.findAll();
    }

    public Optional<VideoSubtitle> getSubtitleById(Integer id) {
        return videoSubtitleRepository.findById(id);
    }

    public List<VideoSubtitle> getSubtitlesByVideoId(Integer videoId) {
        return videoSubtitleRepository.findByVideoId(videoId);
    }

    public List<VideoSubtitle> getSubtitlesByVideoIdAndLanguage(Integer videoId, String language) {
        return videoSubtitleRepository.findByVideoIdAndLanguage(videoId, language);
    }


    @Transactional
    public VideoSubtitle createSubtitle(VideoSubtitle subtitle) {
        // Validate that the video exists
        Optional<Video> video = videoRepository.findById(subtitle.getVideo().getId());
        if (video.isEmpty()) {
            throw new IllegalArgumentException("Video with ID " + subtitle.getVideo().getId() + " not found.");
        }

        subtitle.setVideo(video.get());
        // subtitle.setCreatedAt(Instant.now()); // Removed to use DB defaults
        return videoSubtitleRepository.save(subtitle);
    }

    @Transactional
    public VideoSubtitle updateSubtitle(Integer id, VideoSubtitle subtitleDetails) {
        Optional<VideoSubtitle> optionalSubtitle = videoSubtitleRepository.findById(id);
        if (optionalSubtitle.isPresent()) {
            VideoSubtitle existingSubtitle = optionalSubtitle.get();

            // Validate video if it's being changed
            if(subtitleDetails.getVideo() != null && !subtitleDetails.getVideo().getId().equals(existingSubtitle.getVideo().getId())){
                Optional<Video> video = videoRepository.findById(subtitleDetails.getVideo().getId());
                if(video.isEmpty()){
                    throw new IllegalArgumentException("Video with ID " + subtitleDetails.getVideo().getId() + " does not exists");
                }
                existingSubtitle.setVideo(video.get());
            }

            existingSubtitle.setLanguage(subtitleDetails.getLanguage());
            existingSubtitle.setSubtitleUrl(subtitleDetails.getSubtitleUrl());
            // You typically wouldn't update createdAt
            return videoSubtitleRepository.save(existingSubtitle);
        } else {
            return null; // Or throw an exception
        }
    }

    @Transactional
    public void deleteSubtitle(Integer id) {
        if(videoSubtitleRepository.existsById(id)){
            videoSubtitleRepository.deleteById(id);
        }
        else{
            throw new IllegalArgumentException("Video Subtitle with ID " + id+ " does not exists");
        }
    }
}