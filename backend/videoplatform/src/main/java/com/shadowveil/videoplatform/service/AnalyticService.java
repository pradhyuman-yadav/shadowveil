package com.shadowveil.videoplatform.service;

import com.shadowveil.videoplatform.entity.Analytic;
import com.shadowveil.videoplatform.repository.AnalyticRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AnalyticService {

    private final AnalyticRepository analyticRepository;

    public AnalyticService(AnalyticRepository analyticRepository) {
        this.analyticRepository = analyticRepository;
    }

    public List<Analytic> getAllAnalytics() {
        return analyticRepository.findAll();
    }

    public Optional<Analytic> getAnalyticById(Integer id) {
        return analyticRepository.findById(id);
    }

    public Analytic createAnalytic(Analytic analytic) {
        // You can include additional business logic here if needed
        return analyticRepository.save(analytic);
    }

    public Analytic updateAnalytic(Integer id, Analytic analyticDetails) {
        return analyticRepository.findById(id).map(analytic -> {
            analytic.setVideo(analyticDetails.getVideo());
            analytic.setUser(analyticDetails.getUser());
            analytic.setViewDuration(analyticDetails.getViewDuration());
            analytic.setIpAddress(analyticDetails.getIpAddress());
            analytic.setUserAgent(analyticDetails.getUserAgent());
            analytic.setWatchedAt(analyticDetails.getWatchedAt());
            return analyticRepository.save(analytic);
        }).orElseThrow(() -> new RuntimeException("Analytic not found with id " + id));
    }

    public void deleteAnalytic(Integer id) {
        analyticRepository.deleteById(id);
    }

    public List<Analytic> getAnalyticsByVideoId(Integer videoId) {
        return analyticRepository.findByVideo_Id(videoId);
    }

    public List<Analytic> getAnalyticsByUserId(Integer userId) {
        return analyticRepository.findByUser_Id(userId);
    }
}
