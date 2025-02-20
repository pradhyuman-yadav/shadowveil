package com.shadowveil.videoplatform.service;

import com.shadowveil.videoplatform.dto.VideoTranscriptDto;
import com.shadowveil.videoplatform.entity.Video;
import com.shadowveil.videoplatform.entity.VideoTranscript;
import com.shadowveil.videoplatform.repository.VideoRepository;
import com.shadowveil.videoplatform.repository.VideoSubtitleRepository;
import com.shadowveil.videoplatform.repository.VideoTranscriptRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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

    public VideoTranscript getTranscriptById(Integer id) {
        return videoTranscriptRepository.findById(id).orElseThrow(()-> new EntityNotFoundException("Video transcript not found with ID: " + id));
    }

    public List<VideoTranscript> getTranscriptsByVideoId(Integer videoId) {
        return videoTranscriptRepository.findByVideoId(videoId);
    }

    public List<VideoTranscript> getTranscriptsByVideoIdAndLanguage(Integer videoId, String language) {
        return videoTranscriptRepository.findByVideoIdAndLanguage(videoId, language);
    }


    @Transactional
    public VideoTranscript createTranscript(VideoTranscriptDto.Request requestDto) {
        // Validate that the video exists
        Video video = videoRepository.findById(requestDto.videoId())
                .orElseThrow(() -> new EntityNotFoundException("Video with ID " + requestDto.videoId() + " not found."));

        VideoTranscript transcript = new VideoTranscript();
        transcript.setVideo(video);
        transcript.setLanguage(requestDto.language());
        transcript.setTranscript(requestDto.transcript());

        return videoTranscriptRepository.save(transcript);
    }

    @Transactional
    public VideoTranscript updateTranscript(Integer id, VideoTranscriptDto.UpdateRequest requestDto) {
        VideoTranscript existingTranscript = videoTranscriptRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Transcript not found with id: " + id));

        // Update fields
        existingTranscript.setLanguage(requestDto.language());
        existingTranscript.setTranscript(requestDto.transcript());

        return videoTranscriptRepository.save(existingTranscript);
    }

    @Transactional
    public void deleteTranscript(Integer id) {
        VideoTranscript videoTranscript = videoTranscriptRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Video transcript not found with id: " + id));
        videoTranscriptRepository.delete(videoTranscript);
    }
}