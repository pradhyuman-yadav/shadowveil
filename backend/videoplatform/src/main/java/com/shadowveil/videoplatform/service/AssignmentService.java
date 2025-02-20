package com.shadowveil.videoplatform.service;

import com.shadowveil.videoplatform.dto.AssignmentDto;
import com.shadowveil.videoplatform.dto.CourseDto;
import com.shadowveil.videoplatform.entity.Assignment;
import com.shadowveil.videoplatform.entity.Course;
import com.shadowveil.videoplatform.exception.ResourceNotFoundException;
import com.shadowveil.videoplatform.repository.AssignmentRepository;
import com.shadowveil.videoplatform.repository.CourseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AssignmentService {

    private final AssignmentRepository assignmentRepository;
    private final CourseRepository courseRepository;
    private final CourseService courseService; // Inject CourseService

    @Autowired
    public AssignmentService(AssignmentRepository assignmentRepository, CourseRepository courseRepository, CourseService courseService) {
        this.assignmentRepository = assignmentRepository;
        this.courseRepository = courseRepository;
        this.courseService = courseService; // Initialize CourseService
    }

    @Transactional(readOnly = true)
    public List<AssignmentDto.Response> getAllAssignments() {
        return assignmentRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Optional<AssignmentDto.Response> getAssignmentById(Integer id) {
        return assignmentRepository.findById(id)
                .map(this::convertToDto);
    }

    @Transactional(readOnly = true)
    public List<AssignmentDto.Response> getAssignmentsByCourseId(Integer courseId) {
        return assignmentRepository.findByCourseId(courseId).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public Assignment createAssignment(AssignmentDto.Request assignmentDto) {
        Course course = courseRepository.findById(assignmentDto.courseId())
                .orElseThrow(() -> new ResourceNotFoundException("Course with ID " + assignmentDto.courseId() + " not found."));

        Assignment assignment = new Assignment();
        assignment.setCourse(course);
        assignment.setTitle(assignmentDto.title());
        assignment.setDescription(assignmentDto.description());
        assignment.setDueDate(assignmentDto.dueDate());
        assignment.setCreatedAt(Instant.now());
        assignment.setUpdatedAt(Instant.now());
        return assignmentRepository.save(assignment);
    }

    @Transactional
    public Assignment updateAssignment(Integer id, AssignmentDto.Request assignmentDto) {
        Assignment assignment = assignmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Assignment with ID " + id + " not found"));

        if (assignmentDto.courseId() != null && !assignment.getCourse().getId().equals(assignmentDto.courseId())) {
            Course newCourse = courseRepository.findById(assignmentDto.courseId())
                    .orElseThrow(() -> new ResourceNotFoundException("Course with ID " + assignmentDto.courseId() + " not found."));
            assignment.setCourse(newCourse);
        }

        if(assignmentDto.title() != null) assignment.setTitle(assignmentDto.title());
        if(assignmentDto.description() != null) assignment.setDescription(assignmentDto.description());
        if(assignmentDto.dueDate() != null) assignment.setDueDate(assignmentDto.dueDate());
        assignment.setUpdatedAt(Instant.now());
        return assignmentRepository.save(assignment);
    }

    @Transactional
    public void deleteAssignment(Integer id) {
        if (!assignmentRepository.existsById(id)) {
            throw new ResourceNotFoundException("Assignment with ID " + id + " not found");
        }
        assignmentRepository.deleteById(id);
    }

    public AssignmentDto.Response convertToDto(Assignment assignment) {
        CourseDto.Response courseDto = null;
        if(assignment.getCourse() != null){
            courseDto = courseService.convertToDto(assignment.getCourse()); // Use CourseService
        }
        return new AssignmentDto.Response(
                assignment.getId(),
                courseDto, // Use CourseService to get DTO
                assignment.getTitle(),
                assignment.getDescription(),
                assignment.getDueDate(),
                assignment.getCreatedAt(),
                assignment.getUpdatedAt()
        );
    }
}