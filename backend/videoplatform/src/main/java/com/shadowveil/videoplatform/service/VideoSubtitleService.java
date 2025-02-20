package com.shadowveil.videoplatform.service;

import com.shadowveil.videoplatform.dto.VideoSubtitleDto;
import com.shadowveil.videoplatform.entity.Video;
import com.shadowveil.videoplatform.entity.VideoSubtitle;
import com.shadowveil.videoplatform.repository.VideoRepository;
import com.shadowveil.videoplatform.repository.VideoSubtitleRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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

    public VideoSubtitle getSubtitleById(Integer id) {
        return videoSubtitleRepository.findById(id).orElseThrow(()-> new EntityNotFoundException("Video subtitle not found with ID: " + id));
    }

    public List<VideoSubtitle> getSubtitlesByVideoId(Integer videoId) {
        return videoSubtitleRepository.findByVideoId(videoId);
    }

    public List<VideoSubtitle> getSubtitlesByVideoIdAndLanguage(Integer videoId, String language) {
        return videoSubtitleRepository.findByVideoIdAndLanguage(videoId, language);
    }


    @Transactional
    public VideoSubtitle createSubtitle(VideoSubtitleDto.Request requestDto) {
        // Validate that the video exists
        Video video = videoRepository.findById(requestDto.videoId())
                .orElseThrow(() -> new EntityNotFoundException("Video with ID " + requestDto.videoId() + " not found."));

        VideoSubtitle subtitle = new VideoSubtitle();
        subtitle.setVideo(video);
        subtitle.setLanguage(requestDto.language());
        subtitle.setSubtitleUrl(requestDto.subtitleUrl());
        return videoSubtitleRepository.save(subtitle);
    }

    @Transactional
    public VideoSubtitle updateSubtitle(Integer id, VideoSubtitleDto.UpdateRequest requestDto) {
        VideoSubtitle existingSubtitle = videoSubtitleRepository.findById(id)
                .orElseThrow(()-> new EntityNotFoundException("Video Subtitle with id " + id+ " not found"));

        // Update fields
        existingSubtitle.setLanguage(requestDto.language());
        existingSubtitle.setSubtitleUrl(requestDto.subtitleUrl());
        return videoSubtitleRepository.save(existingSubtitle);
    }

    @Transactional
    public void deleteSubtitle(Integer id) {
        VideoSubtitle videoSubtitle = videoSubtitleRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Video subtitle not found with id: " + id));
        videoSubtitleRepository.delete(videoSubtitle);
    }
}