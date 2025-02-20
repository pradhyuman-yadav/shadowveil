package com.shadowveil.videoplatform.controller;

import com.shadowveil.videoplatform.dto.TagDto;
import com.shadowveil.videoplatform.entity.Tag;
import com.shadowveil.videoplatform.service.TagService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/tags")
public class TagController {

    private final TagService tagService;

    @Autowired
    public TagController(TagService tagService) {
        this.tagService = tagService;
    }

    @GetMapping
    public ResponseEntity<List<TagDto.Response>> getAllTags() {
        List<Tag> tags = tagService.getAllTags();
        List<TagDto.Response> responseDtos = tags.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
        return new ResponseEntity<>(responseDtos, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TagDto.Response> getTagById(@PathVariable Integer id) {
        Tag tag = tagService.getTagById(id);
        return new ResponseEntity<>(convertToDto(tag), HttpStatus.OK);
    }

    @GetMapping("/name/{name}")
    public ResponseEntity<TagDto.Response> getTagByName(@PathVariable String name) {
        Tag tag = tagService.getTagByName(name);
        return new ResponseEntity<>(convertToDto(tag), HttpStatus.OK);
    }

    @GetMapping("/search")
    public ResponseEntity<List<TagDto.Response>> searchTags(@RequestParam String name) {
        List<Tag> tags = tagService.searchTagsByName(name);
        List<TagDto.Response> responseDtos = tags.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
        return new ResponseEntity<>(responseDtos, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<TagDto.Response> createTag(@Valid @RequestBody TagDto.Request requestDto) {
        Tag savedTag = tagService.createTag(requestDto);
        return new ResponseEntity<>(convertToDto(savedTag), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TagDto.Response> updateTag(
            @PathVariable Integer id,
            @Valid @RequestBody TagDto.UpdateRequest requestDto) { // Use UpdateRequest DTO
        Tag updatedTag = tagService.updateTag(id, requestDto);
        return new ResponseEntity<>(convertToDto(updatedTag), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTag(@PathVariable Integer id) {
        tagService.deleteTag(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
    private TagDto.Response convertToDto(Tag tag){
        return new TagDto.Response(
                tag.getId(),
                tag.getName(),
                tag.getCreatedAt(),
                tag.getUpdatedAt()
        );
    }
    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<String> handleEntityNotFoundException(EntityNotFoundException ex){
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
    }
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<String> handleDataIntegrityViolationException(DataIntegrityViolationException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.CONFLICT); // 409 Conflict
    }
    @ExceptionHandler(IllegalArgumentException.class)
    public  ResponseEntity<String> handleIllegalArgumentException(IllegalArgumentException ex){
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }
}