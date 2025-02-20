package com.shadowveil.videoplatform.service;

import com.shadowveil.videoplatform.dto.VideoCategoryDto;
import com.shadowveil.videoplatform.entity.Category;
import com.shadowveil.videoplatform.entity.Video;
import com.shadowveil.videoplatform.entity.VideoCategory;
import com.shadowveil.videoplatform.entity.VideoCategoryId;
import com.shadowveil.videoplatform.repository.CategoryRepository;
import com.shadowveil.videoplatform.repository.VideoCategoryRepository;
import com.shadowveil.videoplatform.repository.VideoRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class VideoCategoryService {

    private final VideoCategoryRepository videoCategoryRepository;
    private final VideoRepository videoRepository; // Inject
    private final CategoryRepository categoryRepository; // Inject

    @Autowired
    public VideoCategoryService(VideoCategoryRepository videoCategoryRepository,
                                VideoRepository videoRepository, CategoryRepository categoryRepository) {
        this.videoCategoryRepository = videoCategoryRepository;
        this.videoRepository = videoRepository;
        this.categoryRepository = categoryRepository;
    }

    public List<VideoCategory> getAllVideoCategories() {
        return videoCategoryRepository.findAll();
    }

    public VideoCategory getVideoCategoryById(VideoCategoryId id) {
        return videoCategoryRepository.findById(id).orElseThrow(()-> new EntityNotFoundException("VideoCategory not found with id: " + id));
    }

    @Transactional
    public VideoCategory createVideoCategory(VideoCategoryDto.Request requestDto) {
        // Validate video and category
        Video video = videoRepository.findById(requestDto.videoId())
                .orElseThrow(() -> new EntityNotFoundException("Video not found with id: " + requestDto.videoId()));
        Category category = categoryRepository.findById(requestDto.categoryId())
                .orElseThrow(() -> new EntityNotFoundException("Category not found with id: " + requestDto.categoryId()));

        VideoCategoryId id = new VideoCategoryId(requestDto.videoId(), requestDto.categoryId());
        VideoCategory videoCategory = new VideoCategory(id, video, category, null); // createdAt by DB

        return videoCategoryRepository.save(videoCategory);
    }

    @Transactional
    public VideoCategory updateVideoCategory(VideoCategoryId id, VideoCategoryDto.Request requestDto) {
        VideoCategory videoCategory = videoCategoryRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("VideoCategory not found with id " + id));

        // Validate if new video and category IDs are provided.
        if(!requestDto.videoId().equals(videoCategory.getVideo().getId())){
            Video video = videoRepository.findById(requestDto.videoId())
                    .orElseThrow(() -> new EntityNotFoundException("Video with id " + requestDto.videoId() + " not found"));
            videoCategory.setVideo(video);
        }
        if(!requestDto.categoryId().equals(videoCategory.getCategory().getId())){
            Category category = categoryRepository.findById(requestDto.categoryId())
                    .orElseThrow(()-> new EntityNotFoundException("Category with id " + requestDto.categoryId() + " not found"));
            videoCategory.setCategory(category);
        }
        videoCategory.setId(new VideoCategoryId(requestDto.videoId(), requestDto.categoryId()));
        return videoCategoryRepository.save(videoCategory);
    }


    @Transactional
    public void deleteVideoCategory(VideoCategoryId id) {
        VideoCategory videoCategory = videoCategoryRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("VideoCategory not found with id: " + id));
        videoCategoryRepository.delete(videoCategory);
    }

    public List<VideoCategory> getVideoCategoriesByVideoId(Integer videoId) {
        return videoCategoryRepository.findByVideo_Id(videoId);
    }

    public List<VideoCategory> getVideoCategoriesByCategoryId(Integer categoryId) {
        return videoCategoryRepository.findByCategory_Id(categoryId);
    }
}