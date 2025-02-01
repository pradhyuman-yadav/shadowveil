package com.shadowveil.videoplatform.controller;

import com.shadowveil.videoplatform.entity.Analytic;
import com.shadowveil.videoplatform.service.AnalyticService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/analytics")
public class AnalyticController {

    private final AnalyticService analyticService;

    public AnalyticController(AnalyticService analyticService) {
        this.analyticService = analyticService;
    }

    // GET /api/analytics
    @GetMapping
    public ResponseEntity<List<Analytic>> getAllAnalytics() {
        List<Analytic> analytics = analyticService.getAllAnalytics();
        return ResponseEntity.ok(analytics);
    }

    // GET /api/analytics/{id}
    @GetMapping("/{id}")
    public ResponseEntity<Analytic> getAnalyticById(@PathVariable Integer id) {
        return analyticService.getAnalyticById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // POST /api/analytics
    @PostMapping
    public ResponseEntity<Analytic> createAnalytic(@RequestBody Analytic analytic) {
        Analytic createdAnalytic = analyticService.createAnalytic(analytic);
        return ResponseEntity.ok(createdAnalytic);
    }

    // PUT /api/analytics/{id}
    @PutMapping("/{id}")
    public ResponseEntity<Analytic> updateAnalytic(@PathVariable Integer id, @RequestBody Analytic analytic) {
        try {
            Analytic updatedAnalytic = analyticService.updateAnalytic(id, analytic);
            return ResponseEntity.ok(updatedAnalytic);
        } catch (RuntimeException ex) {
            return ResponseEntity.notFound().build();
        }
    }

    // DELETE /api/analytics/{id}
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAnalytic(@PathVariable Integer id) {
        analyticService.deleteAnalytic(id);
        return ResponseEntity.noContent().build();
    }

    // GET /api/analytics/video/{videoId}
    @GetMapping("/video/{videoId}")
    public ResponseEntity<List<Analytic>> getAnalyticsByVideoId(@PathVariable Integer videoId) {
        List<Analytic> analytics = analyticService.getAnalyticsByVideoId(videoId);
        return ResponseEntity.ok(analytics);
    }

    // GET /api/analytics/user/{userId}
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Analytic>> getAnalyticsByUserId(@PathVariable Integer userId) {
        List<Analytic> analytics = analyticService.getAnalyticsByUserId(userId);
        return ResponseEntity.ok(analytics);
    }
}
