package com.shadowveil.videoplatform.service;

import com.shadowveil.videoplatform.entity.Tag;
import com.shadowveil.videoplatform.entity.Video;
import com.shadowveil.videoplatform.entity.VideoTag;
import com.shadowveil.videoplatform.entity.VideoTagId;
import com.shadowveil.videoplatform.repository.TagRepository;
import com.shadowveil.videoplatform.repository.VideoRepository;
import com.shadowveil.videoplatform.repository.VideoTagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

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

    public List<VideoTag> getVideoTagByVideoIdAndTagId(Integer videoId, Integer tagId){
        return videoTagRepository.findByVideoIdAndTagId(videoId, tagId);
    }

    @Transactional
    public VideoTag createVideoTag(Integer videoId, Integer tagId) { // Changed signature
        // Validate video and tag
        Optional<Video> video = videoRepository.findById(videoId);  // Use parameters
        if (video.isEmpty()) {
            throw new IllegalArgumentException("Video with ID " + videoId + " not found.");
        }
        Optional<Tag> tag = tagRepository.findById(tagId);      // Use parameters
        if (tag.isEmpty()) {
            throw new IllegalArgumentException("Tag with ID " + tagId + " not found.");
        }

        // Check for duplicates (using the more efficient findOneBy... method)
        if (!videoTagRepository.findByVideoIdAndTagId(videoId, tagId).isEmpty()) {
            throw new IllegalArgumentException("Video tag with video ID " + videoId + " and tag ID " + tagId + " already exists.");
        }


        // Create the VideoTag object
        VideoTag videoTag = new VideoTag();
        videoTag.setVideo(video.get());
        videoTag.setTag(tag.get());

        // Create and set the composite ID (using the constructor!)
        VideoTagId id = new VideoTagId(videoId, tagId); // Use the constructor!
        videoTag.setId(id);


        return videoTagRepository.save(videoTag);
    }


    @Transactional
    public void deleteVideoTag(Integer videoId, Integer tagId) {
        if(videoTagRepository.findByVideoIdAndTagId(videoId, tagId).isEmpty()){
            throw new IllegalArgumentException("Video Tag with video ID " + videoId+ " and tag ID " + tagId+ " does not exists");
        }
        videoTagRepository.deleteByVideoIdAndTagId(videoId, tagId);
    }
}