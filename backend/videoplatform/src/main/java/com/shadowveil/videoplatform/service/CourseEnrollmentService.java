package com.shadowveil.videoplatform.service;

import com.shadowveil.videoplatform.dto.CourseDto;
import com.shadowveil.videoplatform.dto.CourseEnrollmentDto;
import com.shadowveil.videoplatform.dto.UserDto;
import com.shadowveil.videoplatform.entity.Course;
import com.shadowveil.videoplatform.entity.CourseEnrollment;
import com.shadowveil.videoplatform.entity.User;
import com.shadowveil.videoplatform.exception.BadRequestException;
import com.shadowveil.videoplatform.exception.ResourceNotFoundException;
import com.shadowveil.videoplatform.repository.CourseEnrollmentRepository;
import com.shadowveil.videoplatform.repository.CourseRepository;
import com.shadowveil.videoplatform.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CourseEnrollmentService {
    private final CourseEnrollmentRepository courseEnrollmentRepository;
    private final CourseRepository courseRepository;
    private final UserRepository userRepository;
    private final CourseService courseService; // Inject for DTO conversion
    private final UserService userService; // Inject for DTO conversion

    @Autowired
    public CourseEnrollmentService(CourseEnrollmentRepository courseEnrollmentRepository,
                                   CourseRepository courseRepository,
                                   UserRepository userRepository, CourseService courseService, UserService userService){
        this.courseEnrollmentRepository = courseEnrollmentRepository;
        this.courseRepository = courseRepository;
        this.userRepository = userRepository;
        this.courseService = courseService;  // Initialize
        this.userService = userService; // Initialize
    }

    @Transactional(readOnly = true)
    public List<CourseEnrollmentDto.Response> getAllEnrollments(){
        return courseEnrollmentRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Optional<CourseEnrollmentDto.Response> getEnrollmentById(Integer id){
        return courseEnrollmentRepository.findById(id)
                .map(this::convertToDto);
    }

    @Transactional(readOnly = true)
    public List<CourseEnrollmentDto.Response> getEnrollmentByCourseId(Integer courseId){
        return courseEnrollmentRepository.findByCourseId(courseId).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<CourseEnrollmentDto.Response> getEnrollmentByUserId(Integer userId){
        return courseEnrollmentRepository.findByUserId(userId).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }
    @Transactional(readOnly = true)
    public List<CourseEnrollmentDto.Response> getEnrollmentByCourseIdAndUserId(Integer courseId, Integer userId) {
        return courseEnrollmentRepository.findByCourseIdAndUserId(courseId, userId).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }


    @Transactional
    public CourseEnrollment createEnrollment(CourseEnrollmentDto.Request enrollmentDto){
        Course course = courseRepository.findById(enrollmentDto.courseId())
                .orElseThrow(()-> new ResourceNotFoundException("Course with ID: " + enrollmentDto.courseId() + " not found"));

        User user = userRepository.findById(enrollmentDto.userId())
                .orElseThrow(()-> new ResourceNotFoundException("User with ID: "+ enrollmentDto.userId() + " not found"));

        // Check if the user has already enrolled for this course
        Optional<CourseEnrollment> existingEnrollment = courseEnrollmentRepository.findOneByCourseIdAndUserId(
                enrollmentDto.courseId(), enrollmentDto.userId());
        if (existingEnrollment.isPresent()) {
            throw new BadRequestException("User has already enrolled for this course.");
        }

        CourseEnrollment courseEnrollment = new CourseEnrollment();
        courseEnrollment.setCourse(course);
        courseEnrollment.setUser(user);
        return courseEnrollmentRepository.save(courseEnrollment);
    }

    @Transactional
    public CourseEnrollment updateEnrollment(Integer id, CourseEnrollmentDto.Request enrollmentDto){
        CourseEnrollment courseEnrollment = courseEnrollmentRepository.findById(id)
                .orElseThrow(()-> new ResourceNotFoundException("Course Enrollment with ID: " + id + " not found"));
        // Validate course and user if they are being changed
        if(enrollmentDto.courseId() != null && !courseEnrollment.getCourse().getId().equals(enrollmentDto.courseId())){
            Course course = courseRepository.findById(enrollmentDto.courseId())
                    .orElseThrow(()-> new ResourceNotFoundException("Course with ID: " + enrollmentDto.courseId() + " not found."));
            // Check for duplicate enrollment if changing course
            Optional<CourseEnrollment> existingEnrollment = courseEnrollmentRepository.findOneByCourseIdAndUserId(enrollmentDto.courseId(), courseEnrollment.getUser().getId());
            if(existingEnrollment.isPresent()){
                throw new BadRequestException("User has already enrolled for this course.");
            }
            courseEnrollment.setCourse(course);
        }

        if(enrollmentDto.userId() != null && !courseEnrollment.getUser().getId().equals(enrollmentDto.userId())){
            User user = userRepository.findById(enrollmentDto.userId())
                    .orElseThrow(() -> new ResourceNotFoundException("User with ID: " + enrollmentDto.userId() + " not found"));
            //check for duplicate
            Optional<CourseEnrollment> existingEnrollment = courseEnrollmentRepository.findOneByCourseIdAndUserId(courseEnrollment.getCourse().getId(), enrollmentDto.userId());
            if(existingEnrollment.isPresent()){
                throw new BadRequestException("User has already enrolled for this course");
            }
            courseEnrollment.setUser(user);
        }
        return courseEnrollmentRepository.save(courseEnrollment);
    }

    @Transactional
    public void deleteEnrollment(Integer id){
        if(!courseEnrollmentRepository.existsById(id)){
            throw new ResourceNotFoundException("Course Enrollment with ID: " + id + " not found");
        }
        courseEnrollmentRepository.deleteById(id);
    }

    public CourseEnrollmentDto.Response convertToDto(CourseEnrollment enrollment) {
        CourseDto.Response courseDto = null;
        if (enrollment.getCourse() != null) {
            courseDto = courseService.convertToDto(enrollment.getCourse()); // Use CourseService
        }
        UserDto.Response userDto = null;
        if(enrollment.getUser() != null){
            userDto = userService.convertToDto(enrollment.getUser()); // Use UserService
        }
        return new CourseEnrollmentDto.Response(
                enrollment.getId(),
                courseDto,
                userDto,
                enrollment.getEnrolledAt()
        );
    }
}