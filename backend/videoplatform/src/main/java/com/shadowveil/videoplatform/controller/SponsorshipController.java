// File: src/main/java/com/shadowveil/videoplatform/controller/SponsorshipController.java
package com.shadowveil.videoplatform.controller;

import com.shadowveil.videoplatform.entity.Sponsorship;
import com.shadowveil.videoplatform.service.SponsorshipService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/sponsorships")
public class SponsorshipController {

    private final SponsorshipService sponsorshipService;

    @Autowired
    public SponsorshipController(SponsorshipService sponsorshipService) {
        this.sponsorshipService = sponsorshipService;
    }

    @GetMapping
    public ResponseEntity<List<Sponsorship>> getAllSponsorships() {
        return new ResponseEntity<>(sponsorshipService.getAllSponsorships(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Sponsorship> getSponsorshipById(@PathVariable Integer id) {
        Optional<Sponsorship> sponsorship = sponsorshipService.getSponsorshipById(id);
        return sponsorship.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping("/video/{videoId}")
    public ResponseEntity<List<Sponsorship>> getSponsorshipsByVideoId(@PathVariable Integer videoId) {
        return new ResponseEntity<>(sponsorshipService.getSponsorshipsByVideoId(videoId), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Sponsorship> createSponsorship(@RequestBody Sponsorship sponsorship) {
        try {
            Sponsorship savedSponsorship = sponsorshipService.createSponsorship(sponsorship);
            return new ResponseEntity<>(savedSponsorship, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST); // 400 for invalid video
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Sponsorship> updateSponsorship(@PathVariable Integer id, @RequestBody Sponsorship sponsorshipDetails) {
        try {
            Sponsorship updatedSponsorship = sponsorshipService.updateSponsorship(id, sponsorshipDetails);
            return updatedSponsorship != null ? new ResponseEntity<>(updatedSponsorship, HttpStatus.OK)
                    : new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSponsorship(@PathVariable Integer id) {
        try{
            sponsorshipService.deleteSponsorship(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        catch (IllegalArgumentException e){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}