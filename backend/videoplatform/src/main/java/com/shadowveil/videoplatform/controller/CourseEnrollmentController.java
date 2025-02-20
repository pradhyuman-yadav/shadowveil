package com.shadowveil.videoplatform.controller;

import com.shadowveil.videoplatform.dto.CourseEnrollmentDto;
import com.shadowveil.videoplatform.entity.CourseEnrollment;
import com.shadowveil.videoplatform.service.CourseEnrollmentService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/course-enrollments")
public class CourseEnrollmentController {
    private final CourseEnrollmentService courseEnrollmentService;

    @Autowired
    public CourseEnrollmentController(CourseEnrollmentService courseEnrollmentService){
        this.courseEnrollmentService = courseEnrollmentService;
    }

    @GetMapping
    public ResponseEntity<List<CourseEnrollmentDto.Response>> getAllEnrollments(){
        List<CourseEnrollmentDto.Response> courseEnrollments = courseEnrollmentService.getAllEnrollments();
        return new ResponseEntity<>(courseEnrollments, HttpStatus.OK);
    }
    @GetMapping("/{id}")
    public ResponseEntity<CourseEnrollmentDto.Response> getEnrollmentById(@PathVariable Integer id){
        Optional<CourseEnrollmentDto.Response> courseEnrollment = courseEnrollmentService.getEnrollmentById(id);
        return courseEnrollment.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(()-> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping("/course/{courseId}")
    public ResponseEntity<List<CourseEnrollmentDto.Response>> getEnrollmentByCourseId(@PathVariable Integer courseId){
        List<CourseEnrollmentDto.Response> courseEnrollments = courseEnrollmentService.getEnrollmentByCourseId(courseId);
        return new ResponseEntity<>(courseEnrollments,HttpStatus.OK);
    }
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<CourseEnrollmentDto.Response>> getEnrollmentByUserId(@PathVariable Integer userId){
        List<CourseEnrollmentDto.Response> courseEnrollments = courseEnrollmentService.getEnrollmentByUserId(userId);
        return new ResponseEntity<>(courseEnrollments, HttpStatus.OK);
    }

    @GetMapping("/course/{courseId}/user/{userId}")
    public ResponseEntity<List<CourseEnrollmentDto.Response>> getEnrollmentsByCourseAndUser(
            @PathVariable Integer courseId,
            @PathVariable Integer userId) {
        List<CourseEnrollmentDto.Response> courseEnrollments = courseEnrollmentService.getEnrollmentByCourseIdAndUserId(courseId,userId);
        return new ResponseEntity<>(courseEnrollments, HttpStatus.OK);
    }


    @PostMapping
    public ResponseEntity<CourseEnrollmentDto.Response> createEnrollment(@Valid @RequestBody CourseEnrollmentDto.Request enrollmentDto){
        CourseEnrollment courseEnrollment = courseEnrollmentService.createEnrollment(enrollmentDto);
        CourseEnrollmentDto.Response responseDto = courseEnrollmentService.convertToDto(courseEnrollment);
        return new ResponseEntity<>(responseDto, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CourseEnrollmentDto.Response> updateEnrollment(@PathVariable Integer id, @Valid @RequestBody CourseEnrollmentDto.Request enrollmentDto){
        CourseEnrollment courseEnrollment = courseEnrollmentService.updateEnrollment(id, enrollmentDto);
        CourseEnrollmentDto.Response responseDto = courseEnrollmentService.convertToDto(courseEnrollment);
        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEnrollment(@PathVariable Integer id){
        courseEnrollmentService.deleteEnrollment(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}