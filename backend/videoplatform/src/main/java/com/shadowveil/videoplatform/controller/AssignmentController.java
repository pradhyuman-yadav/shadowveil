package com.shadowveil.videoplatform.controller;

import com.shadowveil.videoplatform.dto.AssignmentDto;
import com.shadowveil.videoplatform.entity.Assignment;
import com.shadowveil.videoplatform.service.AssignmentService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/assignments")
public class AssignmentController {

    private final AssignmentService assignmentService;

    @Autowired
    public AssignmentController(AssignmentService assignmentService) {
        this.assignmentService = assignmentService;
    }

    @GetMapping
    public ResponseEntity<List<AssignmentDto.Response>> getAllAssignments() {
        List<AssignmentDto.Response> assignments = assignmentService.getAllAssignments();
        return new ResponseEntity<>(assignments, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AssignmentDto.Response> getAssignmentById(@PathVariable Integer id) {
        Optional<AssignmentDto.Response> assignment = assignmentService.getAssignmentById(id);
        return assignment.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping("/course/{courseId}")
    public ResponseEntity<List<AssignmentDto.Response>> getAssignmentsByCourseId(@PathVariable Integer courseId) {
        List<AssignmentDto.Response> assignments = assignmentService.getAssignmentsByCourseId(courseId);
        return new ResponseEntity<>(assignments, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<AssignmentDto.Response> createAssignment(@Valid @RequestBody AssignmentDto.Request assignmentDto) {
        Assignment createdAssignment = assignmentService.createAssignment(assignmentDto);
        AssignmentDto.Response responseDto = assignmentService.convertToDto(createdAssignment);
        return new ResponseEntity<>(responseDto, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<AssignmentDto.Response> updateAssignment(@PathVariable Integer id, @Valid @RequestBody AssignmentDto.Request assignmentDto) {
        Assignment updatedAssignment = assignmentService.updateAssignment(id, assignmentDto);
        AssignmentDto.Response responseDto = assignmentService.convertToDto(updatedAssignment);
        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAssignment(@PathVariable Integer id) {
        assignmentService.deleteAssignment(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}