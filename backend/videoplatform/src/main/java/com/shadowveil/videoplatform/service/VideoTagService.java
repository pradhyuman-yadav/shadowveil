package com.shadowveil.videoplatform.service;

import com.shadowveil.videoplatform.dto.VideoTagDto;
import com.shadowveil.videoplatform.entity.Tag;
import com.shadowveil.videoplatform.entity.Video;
import com.shadowveil.videoplatform.entity.VideoTag;
import com.shadowveil.videoplatform.entity.VideoTagId;
import com.shadowveil.videoplatform.repository.TagRepository;
import com.shadowveil.videoplatform.repository.VideoRepository;
import com.shadowveil.videoplatform.repository.VideoTagRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class VideoTagService {

    private final VideoTagRepository videoTagRepository;
    private final VideoRepository videoRepository;
    private final TagRepository tagRepository;

    @Autowired
    public VideoTagService(VideoTagRepository videoTagRepository, VideoRepository videoRepository, TagRepository tagRepository) {
        this.videoTagRepository = videoTagRepository;
        this.videoRepository = videoRepository;
        this.tagRepository = tagRepository;
    }

    public List<VideoTag> getAllVideoTags() {
        return videoTagRepository.findAll();
    }

    // Remove this method.  We no longer get by the composite ID directly.
    // public Optional<VideoTag> getVideoTagById(VideoTagId id) {
    //     return videoTagRepository.findById(id);
    // }

    public List<VideoTag> getVideoTagsByVideoId(Integer videoId) {
        return videoTagRepository.findByVideoId(videoId);
    }

    public List<VideoTag> getVideoTagsByTagId(Integer tagId) {
        return videoTagRepository.findByTagId(tagId);
    }
    public List<VideoTag> getVideoTagsByVideoIdAndTagId(Integer videoId, Integer tagId) {
        return videoTagRepository.findByVideoIdAndTagId(videoId,tagId);
    }

    @Transactional
    public VideoTag createVideoTag(VideoTagDto.Request requestDto) { // Changed signature
        // Validate video and tag
        Video video = videoRepository.findById(requestDto.videoId())  // Use parameters
                .orElseThrow(() -> new EntityNotFoundException("Video with ID " + requestDto.videoId() + " not found."));
        Tag tag = tagRepository.findById(requestDto.tagId())      // Use parameters
                .orElseThrow(() -> new EntityNotFoundException("Tag with ID " + requestDto.tagId() + " not found."));

        // Check for duplicates (using the more efficient findOneBy... method)
        if (videoTagRepository.findOneByVideoIdAndTagId(requestDto.videoId(), requestDto.tagId()).isPresent()) {
            throw new DataIntegrityViolationException("Video tag with video ID " + requestDto.videoId() + " and tag ID " + requestDto.tagId() + " already exists.");
        }


        // Create the VideoTag object
        VideoTag videoTag = new VideoTag();
        videoTag.setVideo(video);
        videoTag.setTag(tag);

        // Create and set the composite ID (using the constructor!)
        VideoTagId id = new VideoTagId(requestDto.videoId(), requestDto.tagId()); // Use the constructor!
        videoTag.setId(id);


        return videoTagRepository.save(videoTag);
    }



    @Transactional
    public void deleteVideoTag(Integer videoId, Integer tagId) {
        if(videoTagRepository.findByVideoIdAndTagId(videoId, tagId).isEmpty()){
            throw new EntityNotFoundException("Video Tag with video ID " + videoId+ " and tag ID " + tagId+ " does not exists");
        }
        videoTagRepository.deleteByVideoIdAndTagId(videoId, tagId);
    }
}