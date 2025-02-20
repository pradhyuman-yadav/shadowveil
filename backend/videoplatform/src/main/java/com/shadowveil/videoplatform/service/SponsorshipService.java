package com.shadowveil.videoplatform.service;

import com.shadowveil.videoplatform.dto.SponsorshipDto;
import com.shadowveil.videoplatform.entity.Sponsorship;
import com.shadowveil.videoplatform.entity.Video;
import com.shadowveil.videoplatform.repository.SponsorshipRepository;
import com.shadowveil.videoplatform.repository.VideoRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class SponsorshipService {

    private final SponsorshipRepository sponsorshipRepository;
    private final VideoRepository videoRepository; // Inject VideoRepository

    @Autowired
    public SponsorshipService(SponsorshipRepository sponsorshipRepository, VideoRepository videoRepository) {
        this.sponsorshipRepository = sponsorshipRepository;
        this.videoRepository = videoRepository;
    }

    public List<Sponsorship> getAllSponsorships() {
        return sponsorshipRepository.findAll();
    }

    public Sponsorship getSponsorshipById(Integer id) {
        return sponsorshipRepository.findById(id).orElseThrow(()-> new EntityNotFoundException("Sponsorship not found with ID: " + id));
    }

    public List<Sponsorship> getSponsorshipsByVideoId(Integer videoId) {
        return sponsorshipRepository.findByVideoId(videoId);
    }

    @Transactional
    public Sponsorship createSponsorship(SponsorshipDto.Request requestDto) {
        // Validate that the video exists
        Video video = videoRepository.findById(requestDto.videoId())
                .orElseThrow(() -> new EntityNotFoundException("Video with ID " + requestDto.videoId() + " not found."));


        Sponsorship sponsorship = new Sponsorship();
        sponsorship.setVideo(video);
        sponsorship.setSponsorName(requestDto.sponsorName());
        sponsorship.setSponsorLogoUrl(requestDto.sponsorLogoUrl());
        sponsorship.setDetails(requestDto.details());

        return sponsorshipRepository.save(sponsorship);
    }

    @Transactional
    public Sponsorship updateSponsorship(Integer id, SponsorshipDto.UpdateRequest requestDto) {
        Sponsorship existingSponsorship = sponsorshipRepository.findById(id)
                .orElseThrow(()-> new EntityNotFoundException("Sponsorship with id " + id + " not found"));
        // Update fields.
        existingSponsorship.setSponsorName(requestDto.sponsorName());
        existingSponsorship.setSponsorLogoUrl(requestDto.sponsorLogoUrl());
        existingSponsorship.setDetails(requestDto.details());

        return sponsorshipRepository.save(existingSponsorship);

    }

    @Transactional
    public void deleteSponsorship(Integer id) {
        Sponsorship sponsorship = sponsorshipRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Sponsorship not found with id: " + id));
        sponsorshipRepository.delete(sponsorship);
    }
}