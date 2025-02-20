// src/main/java/com/shadowveil/videoplatform/controller/ReportController.java
package com.shadowveil.videoplatform.controller;

import com.shadowveil.videoplatform.dto.ReportDto;
import com.shadowveil.videoplatform.dto.UserDto;
import com.shadowveil.videoplatform.entity.Report;
import com.shadowveil.videoplatform.service.ReportService;
import com.shadowveil.videoplatform.Util.ReportStatus;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/reports")
public class ReportController {

    private final ReportService reportService;

    @Autowired
    public ReportController(ReportService reportService) {
        this.reportService = reportService;
    }

    @GetMapping
    public ResponseEntity<List<ReportDto.Response>> getAllReports() {
        List<Report> reports = reportService.getAllReports();
        List<ReportDto.Response> responseDtos = reports.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
        return new ResponseEntity<>(responseDtos, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReportDto.Response> getReportById(@PathVariable Integer id) {
        Report report = reportService.getReportById(id);
        return new ResponseEntity<>(convertToDto(report), HttpStatus.OK);
    }

    @GetMapping("/reporter/{reporterId}")
    public ResponseEntity<List<ReportDto.Response>> getReportsByReporterId(@PathVariable Integer reporterId) {
        List<Report> reports = reportService.getReportsByReporterId(reporterId);
        List<ReportDto.Response> responseDtos = reports.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
        return new ResponseEntity<>(responseDtos, HttpStatus.OK);
    }

    @GetMapping("/entity/{reportedType}/{reportedId}")
    public ResponseEntity<List<ReportDto.Response>> getReportsByReportedEntity(
            @PathVariable String reportedType,
            @PathVariable Integer reportedId) {
        List<Report> reports = reportService.getReportsByReportedEntity(reportedType, reportedId);
        List<ReportDto.Response> responseDtos = reports.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
        return new ResponseEntity<>(responseDtos, HttpStatus.OK);
    }

    @GetMapping("/status/{status}") // Keep as String in the path variable
    public ResponseEntity<List<ReportDto.Response>> getReportsByStatus(@PathVariable String status) {
        try {
            ReportStatus reportStatus = ReportStatus.valueOf(status.toUpperCase()); // Convert to enum
            List<Report> reports = reportService.getReportsByStatus(reportStatus);
            List<ReportDto.Response> responseDtos = reports.stream()
                    .map(this::convertToDto)
                    .collect(Collectors.toList());
            return new ResponseEntity<>(responseDtos, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST); // Invalid status value
        }
    }

    @PostMapping
    public ResponseEntity<ReportDto.Response> createReport(@Valid @RequestBody ReportDto.Request requestDto) {
        Report savedReport = reportService.createReport(requestDto);
        return new ResponseEntity<>(convertToDto(savedReport), HttpStatus.CREATED);

    }

    @PutMapping("/{id}/status") // Specific endpoint for status updates
    public ResponseEntity<ReportDto.Response> updateReportStatus(
            @PathVariable Integer id,
            @Valid @RequestBody ReportDto.UpdateStatusRequest updateDto) { // Use the DTO
        Report updatedReport = reportService.updateReport(id, updateDto);
        return new ResponseEntity<>(convertToDto(updatedReport), HttpStatus.OK);

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReport(@PathVariable Integer id) {
        reportService.deleteReport(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    private ReportDto.Response convertToDto(Report report) {
        UserDto.Response userDto = new UserDto.Response(
                report.getReporter().getId(),
                report.getReporter().getUsername(),
                report.getReporter().getEmail(),
                report.getReporter().getRole(),
                report.getReporter().getCreatedAt(),
                report.getReporter().getUpdatedAt()
        );

        return new ReportDto.Response(
                report.getId(),
                userDto,
                report.getReportedType(),
                report.getReportedId(),
                report.getReason(),
                report.getCreatedAt(),
                report.getStatus() // Return as String
        );
    }
    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<String> handleEntityNotFoundException(EntityNotFoundException ex){
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
    }
    @ExceptionHandler(IllegalArgumentException.class)
    public  ResponseEntity<String> handleIllegalArgumentException(IllegalArgumentException ex){
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }
}