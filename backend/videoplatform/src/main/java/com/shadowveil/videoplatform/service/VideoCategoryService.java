package com.shadowveil.videoplatform.service;

import com.shadowveil.videoplatform.entity.VideoCategory;
import com.shadowveil.videoplatform.entity.VideoCategoryId;
import com.shadowveil.videoplatform.repository.VideoCategoryRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class VideoCategoryService {

    private final VideoCategoryRepository videoCategoryRepository;

    public VideoCategoryService(VideoCategoryRepository videoCategoryRepository) {
        this.videoCategoryRepository = videoCategoryRepository;
    }

    // Retrieve all video-category associations.
    public List<VideoCategory> getAllVideoCategories() {
        return videoCategoryRepository.findAll();
    }

    // Retrieve a single video-category association by its composite ID.
    public Optional<VideoCategory> getVideoCategoryById(VideoCategoryId id) {
        return videoCategoryRepository.findById(id);
    }

    // Create a new video-category association.
    public VideoCategory createVideoCategory(VideoCategory videoCategory) {
        return videoCategoryRepository.save(videoCategory);
    }

    // Update an existing video-category association.
    public VideoCategory updateVideoCategory(VideoCategoryId id, VideoCategory videoCategoryDetails) {
        return videoCategoryRepository.findById(id).map(videoCategory -> {
            // Update fields that are not part of the composite key.
            // For example, if you want to update a timestamp field, etc.
            // In this case, we assume only the createdAt field exists (typically not updated) so this method may simply return the existing record.
            // Adjust accordingly if you add additional updatable fields.
            return videoCategoryRepository.save(videoCategory);
        }).orElseThrow(() -> new RuntimeException("VideoCategory not found with id: " + id));
    }

    // Delete a video-category association by its composite ID.
    public void deleteVideoCategory(VideoCategoryId id) {
        videoCategoryRepository.deleteById(id);
    }

    // Retrieve all video-category associations for a specific video.
    public List<VideoCategory> getVideoCategoriesByVideoId(Integer videoId) {
        return videoCategoryRepository.findByVideo_Id(videoId);
    }

    // Retrieve all video-category associations for a specific category.
    public List<VideoCategory> getVideoCategoriesByCategoryId(Integer categoryId) {
        return videoCategoryRepository.findByCategory_Id(categoryId);
    }
}
