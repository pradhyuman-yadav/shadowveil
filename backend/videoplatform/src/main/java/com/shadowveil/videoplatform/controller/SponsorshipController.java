package com.shadowveil.videoplatform.controller;

import com.shadowveil.videoplatform.dto.SponsorshipDto;
import com.shadowveil.videoplatform.dto.UserDto;
import com.shadowveil.videoplatform.dto.VideoDto;
import com.shadowveil.videoplatform.dto.ModuleDto;
import com.shadowveil.videoplatform.entity.Sponsorship;
import com.shadowveil.videoplatform.service.SponsorshipService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/sponsorships")
public class SponsorshipController {

    private final SponsorshipService sponsorshipService;

    @Autowired
    public SponsorshipController(SponsorshipService sponsorshipService) {
        this.sponsorshipService = sponsorshipService;
    }

    @GetMapping
    public ResponseEntity<List<SponsorshipDto.Response>> getAllSponsorships() {
        List<Sponsorship> sponsorships = sponsorshipService.getAllSponsorships();
        List<SponsorshipDto.Response> responseDtos = sponsorships.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(responseDtos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<SponsorshipDto.Response> getSponsorshipById(@PathVariable Integer id) {
        Sponsorship sponsorship = sponsorshipService.getSponsorshipById(id);
        return ResponseEntity.ok(convertToDto(sponsorship));
    }

    @GetMapping("/video/{videoId}")
    public ResponseEntity<List<SponsorshipDto.Response>> getSponsorshipsByVideoId(@PathVariable Integer videoId) {
        List<Sponsorship> sponsorships = sponsorshipService.getSponsorshipsByVideoId(videoId);
        List<SponsorshipDto.Response> responseDtos = sponsorships.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(responseDtos);
    }

    @PostMapping
    public ResponseEntity<SponsorshipDto.Response> createSponsorship(@Valid @RequestBody SponsorshipDto.Request requestDto) {
        Sponsorship savedSponsorship = sponsorshipService.createSponsorship(requestDto);
        return new ResponseEntity<>(convertToDto(savedSponsorship), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<SponsorshipDto.Response> updateSponsorship(
            @PathVariable Integer id,
            @Valid @RequestBody SponsorshipDto.UpdateRequest requestDto) { // Use UpdateRequest
        Sponsorship updatedSponsorship = sponsorshipService.updateSponsorship(id, requestDto);
        return ResponseEntity.ok(convertToDto(updatedSponsorship));

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSponsorship(@PathVariable Integer id) {
        sponsorshipService.deleteSponsorship(id);
        return ResponseEntity.noContent().build();
    }

    private SponsorshipDto.Response convertToDto(Sponsorship sponsorship) {

        VideoDto.Response videoDto = new VideoDto.Response(
                sponsorship.getVideo().getId(),
                (sponsorship.getVideo().getUser() != null) ? new UserDto.Response(
                        sponsorship.getVideo().getUser().getId(),
                        sponsorship.getVideo().getUser().getUsername(),
                        sponsorship.getVideo().getUser().getEmail(),
                        sponsorship.getVideo().getUser().getRole(),
                        sponsorship.getVideo().getUser().getCreatedAt(),
                        sponsorship.getVideo().getUser().getUpdatedAt()
                ) : null,
                sponsorship.getVideo().getTitle(),
                sponsorship.getVideo().getDescription(),
                sponsorship.getVideo().getUrl(),
                sponsorship.getVideo().getThumbnailUrl(),
                sponsorship.getVideo().getDuration(),
                sponsorship.getVideo().getStatus(),
                sponsorship.getVideo().getViews(),
                sponsorship.getVideo().getLikes(),
                sponsorship.getVideo().getDislikes(),
                sponsorship.getVideo().getCreatedAt(),
                sponsorship.getVideo().getUpdatedAt(),
                (sponsorship.getVideo().getModule() != null) ? new ModuleDto.Response( // Handle null module
                        sponsorship.getVideo().getModule().getId(),
                        null, // No CourseDto needed
                        sponsorship.getVideo().getModule().getTitle(),
                        sponsorship.getVideo().getModule().getDescription(),
                        sponsorship.getVideo().getModule().getPosition(),
                        sponsorship.getVideo().getModule().getCreatedAt(),
                        sponsorship.getVideo().getModule().getUpdatedAt()
                ) : null
        );
        return new SponsorshipDto.Response(
                sponsorship.getId(),
                videoDto,
                sponsorship.getSponsorName(),
                sponsorship.getSponsorLogoUrl(),
                sponsorship.getDetails(),
                sponsorship.getCreatedAt()
        );
    }
    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<String> handleEntityNotFoundException(EntityNotFoundException ex){
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
    }
}