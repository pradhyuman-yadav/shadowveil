package com.shadowveil.videoplatform.controller;

import com.shadowveil.videoplatform.dto.AnalyticDto;
import com.shadowveil.videoplatform.entity.Analytic;
import com.shadowveil.videoplatform.service.AnalyticService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/analytics")
public class AnalyticController {

    private final AnalyticService analyticService;

    @Autowired
    public AnalyticController(AnalyticService analyticService) {
        this.analyticService = analyticService;
    }

    @GetMapping
    public ResponseEntity<List<AnalyticDto.Response>> getAllAnalytics() {
        List<AnalyticDto.Response> analytics = analyticService.getAllAnalytics();
        return ResponseEntity.ok(analytics);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AnalyticDto.Response> getAnalyticById(@PathVariable Integer id) {
        Optional<AnalyticDto.Response> analytic = analyticService.getAnalyticById(id);
        return analytic.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping
    public ResponseEntity<AnalyticDto.Response> createAnalytic(@Valid @RequestBody AnalyticDto.Request analyticDto) {
        Analytic createdAnalytic = analyticService.createAnalytic(analyticDto);
        AnalyticDto.Response responseDto = analyticService.convertToDto(createdAnalytic);
        return new ResponseEntity<>(responseDto, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<AnalyticDto.Response> updateAnalytic(@PathVariable Integer id, @Valid @RequestBody AnalyticDto.Request analyticDto) {
        Analytic updatedAnalytic = analyticService.updateAnalytic(id, analyticDto);
        AnalyticDto.Response responseDto = analyticService.convertToDto(updatedAnalytic);
        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAnalytic(@PathVariable Integer id) {
        analyticService.deleteAnalytic(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/video/{videoId}")
    public ResponseEntity<List<AnalyticDto.Response>> getAnalyticsByVideoId(@PathVariable Integer videoId) {
        List<AnalyticDto.Response> analytics = analyticService.getAnalyticsByVideoId(videoId);
        return ResponseEntity.ok(analytics);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<AnalyticDto.Response>> getAnalyticsByUserId(@PathVariable Integer userId) {
        List<AnalyticDto.Response> analytics = analyticService.getAnalyticsByUserId(userId);
        return ResponseEntity.ok(analytics);
    }
}