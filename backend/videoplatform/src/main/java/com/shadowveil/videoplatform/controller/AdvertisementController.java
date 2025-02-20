package com.shadowveil.videoplatform.controller;

import com.shadowveil.videoplatform.dto.AdvertisementDto;
import com.shadowveil.videoplatform.entity.Advertisement;
import com.shadowveil.videoplatform.service.AdvertisementService;
import jakarta.validation.Valid; // Import for validation
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/advertisements")
public class AdvertisementController {

    private final AdvertisementService advertisementService;

    @Autowired
    public AdvertisementController(AdvertisementService advertisementService) {
        this.advertisementService = advertisementService;
    }

    @GetMapping
    public ResponseEntity<List<AdvertisementDto.Response>> getAllAdvertisements() {
        List<AdvertisementDto.Response> advertisements = advertisementService.getAllAdvertisements();
        return new ResponseEntity<>(advertisements, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AdvertisementDto.Response> getAdvertisementById(@PathVariable Integer id) {
        Optional<AdvertisementDto.Response> advertisement = advertisementService.getAdvertisementById(id);
        return advertisement.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping
    public ResponseEntity<AdvertisementDto.Response> createAdvertisement(@Valid @RequestBody AdvertisementDto.Request advertisementDto) {
        Advertisement createdAdvertisement = advertisementService.createAdvertisement(advertisementDto);
        AdvertisementDto.Response responseDto = advertisementService.convertToDto(createdAdvertisement);
        return new ResponseEntity<>(responseDto, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<AdvertisementDto.Response> updateAdvertisement(@PathVariable Integer id, @Valid @RequestBody AdvertisementDto.Request advertisementDto) {
        Advertisement updatedAdvertisement = advertisementService.updateAdvertisement(id, advertisementDto);
        AdvertisementDto.Response responseDto = advertisementService.convertToDto(updatedAdvertisement);
        return new ResponseEntity<>(responseDto,HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAdvertisement(@PathVariable Integer id) {
        advertisementService.deleteAdvertisement(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}