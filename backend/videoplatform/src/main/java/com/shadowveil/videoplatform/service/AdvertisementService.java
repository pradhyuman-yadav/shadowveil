package com.shadowveil.videoplatform.service;

import com.shadowveil.videoplatform.dto.AdvertisementDto;
import com.shadowveil.videoplatform.entity.Advertisement;
import com.shadowveil.videoplatform.exception.ResourceNotFoundException;
import com.shadowveil.videoplatform.repository.AdvertisementRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AdvertisementService {

    private final AdvertisementRepository advertisementRepository;

    @Autowired
    public AdvertisementService(AdvertisementRepository advertisementRepository) {
        this.advertisementRepository = advertisementRepository;
    }

    @Transactional(readOnly = true)
    public List<AdvertisementDto.Response> getAllAdvertisements() {
        return advertisementRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Optional<AdvertisementDto.Response> getAdvertisementById(Integer id) {
        return advertisementRepository.findById(id)
                .map(this::convertToDto);
    }

    @Transactional
    public Advertisement createAdvertisement(AdvertisementDto.Request advertisementDto) {
        Advertisement advertisement = new Advertisement();
        advertisement.setTitle(advertisementDto.title());
        advertisement.setContent(advertisementDto.content());
        advertisement.setImageUrl(advertisementDto.imageUrl());
        advertisement.setTargetUrl(advertisementDto.targetUrl());
        advertisement.setStartDate(advertisementDto.startDate());
        advertisement.setEndDate(advertisementDto.endDate());
        // createdAt will be handled by @CreationTimestamp
        return advertisementRepository.save(advertisement);
    }

    @Transactional
    public Advertisement updateAdvertisement(Integer id, AdvertisementDto.Request advertisementDto) {
        Advertisement advertisement = advertisementRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Advertisement not found with id " + id));

        if(advertisementDto.title() != null) advertisement.setTitle(advertisementDto.title());
        if(advertisementDto.content() != null) advertisement.setContent(advertisementDto.content());
        if(advertisementDto.imageUrl() != null) advertisement.setImageUrl(advertisementDto.imageUrl());
        if(advertisementDto.targetUrl() != null) advertisement.setTargetUrl(advertisementDto.targetUrl());
        if(advertisementDto.startDate() != null) advertisement.setStartDate(advertisementDto.startDate());
        if(advertisementDto.endDate() != null) advertisement.setEndDate(advertisementDto.endDate());

        return advertisementRepository.save(advertisement);
    }

    @Transactional
    public void deleteAdvertisement(Integer id) {
        if (!advertisementRepository.existsById(id)) {
            throw new ResourceNotFoundException("Advertisement with ID " + id + " not found");
        }
        advertisementRepository.deleteById(id);
    }

    public AdvertisementDto.Response convertToDto(Advertisement advertisement) {
        return new AdvertisementDto.Response(
                advertisement.getId(),
                advertisement.getTitle(),
                advertisement.getContent(),
                advertisement.getImageUrl(),
                advertisement.getTargetUrl(),
                advertisement.getStartDate(),
                advertisement.getEndDate(),
                advertisement.getCreatedAt()
        );
    }
}