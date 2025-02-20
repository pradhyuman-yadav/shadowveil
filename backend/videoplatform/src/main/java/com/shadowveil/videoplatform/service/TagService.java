package com.shadowveil.videoplatform.service;

import com.shadowveil.videoplatform.dto.TagDto;
import com.shadowveil.videoplatform.entity.Tag;
import com.shadowveil.videoplatform.repository.TagRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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

    public Tag getTagById(Integer id) {
        return tagRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Tag not found with ID: " + id));
    }

    public Tag getTagByName(String name) {
        return tagRepository.findByName(name).orElseThrow(()-> new EntityNotFoundException("Tag not found with Name: " + name));
    }

    public List<Tag> searchTagsByName(String name) {
        return tagRepository.findByNameContainingIgnoreCase(name);
    }

    @Transactional
    public Tag createTag(TagDto.Request requestDto) {
        // Check for duplicate tag names (handle the unique constraint)
        if (tagRepository.findByName(requestDto.name()).isPresent()) {
            throw new DataIntegrityViolationException("A tag with the name '" + requestDto.name() + "' already exists.");
        }

        Tag tag = new Tag();
        tag.setName(requestDto.name());
        // createdAt and updatedAt are handled by the database/Hibernate

        return tagRepository.save(tag);
    }

    @Transactional
    public Tag updateTag(Integer id, TagDto.UpdateRequest requestDto) {
        Tag existingTag = tagRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Tag not found with id: " + id));


        // Check for duplicate tag names if the name is being changed
        if (!requestDto.name().equals(existingTag.getName()) && tagRepository.findByName(requestDto.name()).isPresent()) {
            throw new DataIntegrityViolationException("A tag with the name '" + requestDto.name() + "' already exists.");
        }

        existingTag.setName(requestDto.name());
        // updatedAt is handled automatically by @UpdateTimestamp

        return tagRepository.save(existingTag);

    }

    @Transactional
    public void deleteTag(Integer id) {
        Tag tag = tagRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Tag not found with id: " + id));
        tagRepository.delete(tag);
    }
}