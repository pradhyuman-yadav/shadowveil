// File: src/main/java/com/shadowveil/videoplatform/service/TagService.java
package com.shadowveil.videoplatform.service;

import com.shadowveil.videoplatform.entity.Tag;
import com.shadowveil.videoplatform.repository.TagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Service
public class TagService {

    private final TagRepository tagRepository;

    @Autowired
    public TagService(TagRepository tagRepository) {
        this.tagRepository = tagRepository;
    }

    public List<Tag> getAllTags() {
        return tagRepository.findAll();
    }

    public Optional<Tag> getTagById(Integer id) {
        return tagRepository.findById(id);
    }

    public Optional<Tag> getTagByName(String name) {
        return tagRepository.findByName(name);
    }

    public List<Tag> searchTagsByName(String name) {
        return tagRepository.findByNameContainingIgnoreCase(name);
    }

    @Transactional
    public Tag createTag(Tag tag) {
        // Check for duplicate tag names (even though there's a DB constraint, handle it gracefully)
        Optional<Tag> existingTag = tagRepository.findByName(tag.getName());
        if (existingTag.isPresent()) {
            throw new IllegalArgumentException("A tag with the name '" + tag.getName() + "' already exists.");
        }
        tag.setUpdatedAt(Instant.now());
        return tagRepository.save(tag);
    }

    @Transactional
    public Tag updateTag(Integer id, Tag tagDetails) {
        Optional<Tag> optionalTag = tagRepository.findById(id);
        if (optionalTag.isPresent()) {
            Tag existingTag = optionalTag.get();

            // Check for duplicate tag names if the name is being changed
            if (!tagDetails.getName().equals(existingTag.getName())) {
                Optional<Tag> existingTagWithName = tagRepository.findByName(tagDetails.getName());
                if (existingTagWithName.isPresent()) {
                    throw new IllegalArgumentException("A tag with the name '" + tagDetails.getName() + "' already exists.");
                }
            }

            existingTag.setName(tagDetails.getName());
            existingTag.setUpdatedAt(Instant.now());
            return tagRepository.save(existingTag);
        } else {
            return null; // Or throw an exception
        }
    }

    @Transactional
    public void deleteTag(Integer id) {
        if(tagRepository.existsById(id)){
            tagRepository.deleteById(id);
        }
        else{
            throw new IllegalArgumentException("Tag with ID " + id + " does not exists");
        }
    }
}