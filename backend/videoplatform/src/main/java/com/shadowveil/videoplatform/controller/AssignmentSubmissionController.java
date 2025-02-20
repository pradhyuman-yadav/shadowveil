package com.shadowveil.videoplatform.controller;

import com.shadowveil.videoplatform.dto.AssignmentSubmissionDto;
import com.shadowveil.videoplatform.entity.AssignmentSubmission;
import com.shadowveil.videoplatform.service.AssignmentSubmissionService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/assignment-submissions")
public class AssignmentSubmissionController {

    private final AssignmentSubmissionService assignmentSubmissionService;

    @Autowired
    public AssignmentSubmissionController(AssignmentSubmissionService assignmentSubmissionService) {
        this.assignmentSubmissionService = assignmentSubmissionService;
    }

    @GetMapping
    public ResponseEntity<List<AssignmentSubmissionDto.Response>> getAllSubmissions() {
        List<AssignmentSubmissionDto.Response> submissions = assignmentSubmissionService.getAllSubmissions();
        return new ResponseEntity<>(submissions, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AssignmentSubmissionDto.Response> getSubmissionById(@PathVariable Integer id) {
        Optional<AssignmentSubmissionDto.Response> submission = assignmentSubmissionService.getSubmissionById(id);
        return submission.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping("/assignment/{assignmentId}")
    public ResponseEntity<List<AssignmentSubmissionDto.Response>> getSubmissionsByAssignmentId(@PathVariable Integer assignmentId) {
        List<AssignmentSubmissionDto.Response> submissions = assignmentSubmissionService.getSubmissionsByAssignmentId(assignmentId);
        return new ResponseEntity<>(submissions, HttpStatus.OK);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<AssignmentSubmissionDto.Response>> getSubmissionsByUserId(@PathVariable Integer userId) {
        List<AssignmentSubmissionDto.Response> submissions = assignmentSubmissionService.getSubmissionsByUserId(userId);
        return new ResponseEntity<>(submissions, HttpStatus.OK);
    }
    @GetMapping("/assignment/{assignmentId}/user/{userId}")
    public ResponseEntity<List<AssignmentSubmissionDto.Response>> getSubmissionsByAssignmentAndUser(
            @PathVariable Integer assignmentId,
            @PathVariable Integer userId) {
        List<AssignmentSubmissionDto.Response> assignmentSubmissions = assignmentSubmissionService.getSubmissionsByAssignmentIdAndUserId(assignmentId,userId);
        return new ResponseEntity<>(assignmentSubmissions, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<AssignmentSubmissionDto.Response> createSubmission(@Valid @RequestBody AssignmentSubmissionDto.Request submissionDto) {
        AssignmentSubmission createdSubmission = assignmentSubmissionService.createSubmission(submissionDto);
        AssignmentSubmissionDto.Response responseDto = assignmentSubmissionService.convertToDto(createdSubmission);
        return new ResponseEntity<>(responseDto, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<AssignmentSubmissionDto.Response> updateSubmission(@PathVariable Integer id, @Valid @RequestBody AssignmentSubmissionDto.Request submissionDto) {
        AssignmentSubmission updatedSubmission = assignmentSubmissionService.updateSubmission(id, submissionDto);
        AssignmentSubmissionDto.Response responseDto = assignmentSubmissionService.convertToDto(updatedSubmission);
        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSubmission(@PathVariable Integer id) {
        assignmentSubmissionService.deleteSubmission(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}