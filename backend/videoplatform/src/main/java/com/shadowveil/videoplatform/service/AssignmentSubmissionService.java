package com.shadowveil.videoplatform.service;

import com.shadowveil.videoplatform.dto.AssignmentDto;
import com.shadowveil.videoplatform.dto.AssignmentSubmissionDto;
import com.shadowveil.videoplatform.dto.UserDto;
import com.shadowveil.videoplatform.entity.Assignment;
import com.shadowveil.videoplatform.entity.AssignmentSubmission;
import com.shadowveil.videoplatform.entity.User;
import com.shadowveil.videoplatform.exception.BadRequestException;
import com.shadowveil.videoplatform.exception.ResourceNotFoundException;
import com.shadowveil.videoplatform.repository.AssignmentRepository;
import com.shadowveil.videoplatform.repository.AssignmentSubmissionRepository;
import com.shadowveil.videoplatform.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AssignmentSubmissionService {

    private final AssignmentSubmissionRepository assignmentSubmissionRepository;
    private final AssignmentRepository assignmentRepository;
    private final UserRepository userRepository;
    private final UserService userService;
    private final AssignmentService assignmentService; // Inject AssignmentService
    private final CourseService courseService; // Inject CourseService


    @Autowired
    public AssignmentSubmissionService(
            AssignmentSubmissionRepository assignmentSubmissionRepository,
            AssignmentRepository assignmentRepository,
            UserRepository userRepository, UserService userService, AssignmentService assignmentService, CourseService courseService) {
        this.assignmentSubmissionRepository = assignmentSubmissionRepository;
        this.assignmentRepository = assignmentRepository;
        this.userRepository = userRepository;
        this.userService = userService;
        this.assignmentService = assignmentService; // Initialize
        this.courseService = courseService;
    }

    @Transactional(readOnly = true)
    public List<AssignmentSubmissionDto.Response> getAllSubmissions() {
        return assignmentSubmissionRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Optional<AssignmentSubmissionDto.Response> getSubmissionById(Integer id) {
        return assignmentSubmissionRepository.findById(id)
                .map(this::convertToDto);
    }
    @Transactional(readOnly = true)
    public List<AssignmentSubmissionDto.Response> getSubmissionsByAssignmentId(Integer assignmentId) {
        return assignmentSubmissionRepository.findByAssignmentId(assignmentId).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<AssignmentSubmissionDto.Response> getSubmissionsByUserId(Integer userId) {
        return assignmentSubmissionRepository.findByUserId(userId).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<AssignmentSubmissionDto.Response> getSubmissionsByAssignmentIdAndUserId(Integer assignmentId, Integer userId) {
        return assignmentSubmissionRepository.findByAssignmentIdAndUserId(assignmentId, userId).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public AssignmentSubmission createSubmission(AssignmentSubmissionDto.Request submissionDto) {
        Assignment assignment = assignmentRepository.findById(submissionDto.assignmentId())
                .orElseThrow(() -> new ResourceNotFoundException("Assignment with ID " + submissionDto.assignmentId() + " not found."));

        User user = userRepository.findById(submissionDto.userId())
                .orElseThrow(() -> new ResourceNotFoundException("User with ID " + submissionDto.userId() + " not found."));


        List<AssignmentSubmission> existingSubmissions = assignmentSubmissionRepository.findByAssignmentIdAndUserId(
                submissionDto.assignmentId(), submissionDto.userId());

        if (!existingSubmissions.isEmpty()) {
            throw new BadRequestException("User has already submitted for this assignment."); // Use the correct exception
        }

        AssignmentSubmission submission = new AssignmentSubmission();
        submission.setAssignment(assignment);
        submission.setUser(user);
        submission.setSubmissionText(submissionDto.submissionText());
        submission.setGrade(submissionDto.grade());
        // submission.setSubmittedAt(Instant.now());  // Removed this
        return assignmentSubmissionRepository.save(submission);
    }

    @Transactional
    public AssignmentSubmission updateSubmission(Integer id, AssignmentSubmissionDto.Request submissionDto) {
        AssignmentSubmission submission = assignmentSubmissionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Assignment submission with ID " + id + " not found"));

        // Validate and update Assignment (if provided and different)
        if (submissionDto.assignmentId() != null && !submission.getAssignment().getId().equals(submissionDto.assignmentId())) {
            Assignment newAssignment = assignmentRepository.findById(submissionDto.assignmentId())
                    .orElseThrow(() -> new ResourceNotFoundException("Assignment with ID " + submissionDto.assignmentId() + " not found"));
            submission.setAssignment(newAssignment);
        }

        // Validate and update User (if provided and different)
        if (submissionDto.userId() != null && !submission.getUser().getId().equals(submissionDto.userId())) {
            User newUser = userRepository.findById(submissionDto.userId())
                    .orElseThrow(() -> new ResourceNotFoundException("User with ID " + submissionDto.userId() + " not found."));
            submission.setUser(newUser);
        }

        // Update other fields
        if(submissionDto.submissionText() != null) submission.setSubmissionText(submissionDto.submissionText());
        if(submissionDto.grade() != null)  submission.setGrade(submissionDto.grade());


        return assignmentSubmissionRepository.save(submission);
    }

    @Transactional
    public void deleteSubmission(Integer id) {
        if (!assignmentSubmissionRepository.existsById(id)) {
            throw new ResourceNotFoundException("Assignment Submission with ID " + id + " not found");
        }
        assignmentSubmissionRepository.deleteById(id);
    }

    public AssignmentSubmissionDto.Response convertToDto(AssignmentSubmission submission) {
        AssignmentDto.Response assignmentDto = null;
        if(submission.getAssignment() != null){
            assignmentDto = convertToDto(submission.getAssignment()); // Now uses the injected AssignmentService
        }

        UserDto.Response userDto = null;
        if(submission.getUser() != null){
            userDto = userService.convertToDto(submission.getUser());
        }

        return new AssignmentSubmissionDto.Response(
                submission.getId(),
                assignmentDto, // Use AssignmentService
                userDto,       // Use UserService
                submission.getSubmissionText(),
                submission.getGrade(),
                submission.getSubmittedAt()
        );
    }

    //Added to convert Assignment entity to AssignmentDto.Response
    public AssignmentDto.Response convertToDto(Assignment assignment) {
        return new AssignmentDto.Response(
                assignment.getId(),
                courseService.convertToDto(assignment.getCourse()), // Uses injected service
                assignment.getTitle(),
                assignment.getDescription(),
                assignment.getDueDate(),
                assignment.getCreatedAt(),
                assignment.getUpdatedAt()
        );
    }
}