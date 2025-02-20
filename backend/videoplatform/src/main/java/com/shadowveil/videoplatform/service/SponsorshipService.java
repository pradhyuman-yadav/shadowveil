// File: src/main/java/com/shadowveil/videoplatform/service/SponsorshipService.java
package com.shadowveil.videoplatform.service;

import com.shadowveil.videoplatform.entity.Sponsorship;
import com.shadowveil.videoplatform.entity.Video;
import com.shadowveil.videoplatform.repository.SponsorshipRepository;
import com.shadowveil.videoplatform.repository.VideoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

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

    public Optional<Sponsorship> getSponsorshipById(Integer id) {
        return sponsorshipRepository.findById(id);
    }

    public List<Sponsorship> getSponsorshipsByVideoId(Integer videoId) {
        return sponsorshipRepository.findByVideoId(videoId);
    }

    @Transactional
    public Sponsorship createSponsorship(Sponsorship sponsorship) {
        // Validate that the video exists
        Optional<Video> video = videoRepository.findById(sponsorship.getVideo().getId());
        if (video.isEmpty()) {
            throw new IllegalArgumentException("Video with ID " + sponsorship.getVideo().getId() + " not found.");
        }

        sponsorship.setVideo(video.get());
        //sponsorship.setCreatedAt(Instant.now());// Removed to use DB default
        return sponsorshipRepository.save(sponsorship);
    }

    @Transactional
    public Sponsorship updateSponsorship(Integer id, Sponsorship sponsorshipDetails) {
        Optional<Sponsorship> optionalSponsorship = sponsorshipRepository.findById(id);
        if (optionalSponsorship.isPresent()) {
            Sponsorship existingSponsorship = optionalSponsorship.get();

            // Validate video, if the video is being changed
            if(sponsorshipDetails.getVideo() != null && !sponsorshipDetails.getVideo().getId().equals(existingSponsorship.getVideo().getId())){
                Optional<Video> video = videoRepository.findById(sponsorshipDetails.getVideo().getId());
                if(video.isEmpty()){
                    throw new IllegalArgumentException("Video with ID " + sponsorshipDetails.getVideo().getId() + " does not exists");
                }
                existingSponsorship.setVideo(video.get());
            }

            existingSponsorship.setSponsorName(sponsorshipDetails.getSponsorName());
            existingSponsorship.setSponsorLogoUrl(sponsorshipDetails.getSponsorLogoUrl());
            existingSponsorship.setDetails(sponsorshipDetails.getDetails());
            // You might not allow updating createdAt

            return sponsorshipRepository.save(existingSponsorship);
        } else {
            return null; // Or throw an exception
        }
    }

    @Transactional
    public void deleteSponsorship(Integer id) {
        if(sponsorshipRepository.existsById(id)){
            sponsorshipRepository.deleteById(id);
        }
        else{
            throw new IllegalArgumentException("Sponsorship with ID " + id + " does not exists");
        }
    }
}