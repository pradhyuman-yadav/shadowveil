// File: src/main/java/com/shadowveil/videoplatform/controller/ReportController.java
package com.shadowveil.videoplatform.controller;

import com.shadowveil.videoplatform.entity.Report;
import com.shadowveil.videoplatform.service.ReportService;
import com.shadowveil.videoplatform.Util.ReportStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/reports")
public class ReportController {

    private final ReportService reportService;

    @Autowired
    public ReportController(ReportService reportService) {
        this.reportService = reportService;
    }

    @GetMapping
    public ResponseEntity<List<Report>> getAllReports() {
        return new ResponseEntity<>(reportService.getAllReports(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Report> getReportById(@PathVariable Integer id) {
        Optional<Report> report = reportService.getReportById(id);
        return report.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping("/reporter/{reporterId}")
    public ResponseEntity<List<Report>> getReportsByReporterId(@PathVariable Integer reporterId) {
        return new ResponseEntity<>(reportService.getReportsByReporterId(reporterId), HttpStatus.OK);
    }

    // Endpoint to get reports for a specific entity (e.g., all reports for video ID 5)
    @GetMapping("/entity/{reportedType}/{reportedId}")
    public ResponseEntity<List<Report>> getReportsByReportedEntity(
            @PathVariable String reportedType,
            @PathVariable Integer reportedId) {
        return new ResponseEntity<>(reportService.getReportsByReportedEntity(reportedType, reportedId), HttpStatus.OK);
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<Report>> getReportsByStatus(@PathVariable String status) { // Keep as String
        try{
            ReportStatus reportStatus = ReportStatus.valueOf(status.toUpperCase());
            return new ResponseEntity<>(reportService.getReportsByStatus(reportStatus), HttpStatus.OK);
        }
        catch (IllegalArgumentException e){
            return  new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }


    @PostMapping
    public ResponseEntity<Report> createReport(@RequestBody Report report) {
        try {
            Report savedReport = reportService.createReport(report);
            return new ResponseEntity<>(savedReport, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST); // 400 for invalid reporter/entity
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Report> updateReport(@PathVariable Integer id, @RequestBody Report reportDetails) {
        try {
            Report updatedReport = reportService.updateReport(id, reportDetails);
            return updatedReport != null ? new ResponseEntity<>(updatedReport, HttpStatus.OK)
                    : new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReport(@PathVariable Integer id) {
        try{
            reportService.deleteReport(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        catch (IllegalArgumentException e){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}