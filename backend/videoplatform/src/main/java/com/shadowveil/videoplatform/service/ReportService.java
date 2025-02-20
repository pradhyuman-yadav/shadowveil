package com.shadowveil.videoplatform.service;

import com.shadowveil.videoplatform.dto.ReportDto;
import com.shadowveil.videoplatform.entity.*;
import com.shadowveil.videoplatform.repository.*;
import com.shadowveil.videoplatform.Util.ReportStatus;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ReportService {

    private final ReportRepository reportRepository;
    private final UserRepository userRepository;
    // Inject *all* repositories for entities that can be reported.
    private final VideoRepository videoRepository;
    private final CommentRepository commentRepository;
    // ... add other repositories as needed (e.g., PlaylistRepository, etc.)

    @Autowired
    public ReportService(ReportRepository reportRepository, UserRepository userRepository,
                         VideoRepository videoRepository, CommentRepository commentRepository /*, ... other repositories*/) {
        this.reportRepository = reportRepository;
        this.userRepository = userRepository;
        this.videoRepository = videoRepository;
        this.commentRepository = commentRepository;
        // ... initialize other repositories
    }

    public List<Report> getAllReports() {
        return reportRepository.findAll();
    }

    public Report getReportById(Integer id) {
        return reportRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Report not found with ID: " + id));
    }

    public List<Report> getReportsByReporterId(Integer reporterId) {
        return reportRepository.findByReporterId(reporterId);
    }

    public List<Report> getReportsByReportedEntity(String reportedType, Integer reportedId) {
        return reportRepository.findByReportedTypeAndReportedId(reportedType, reportedId);
    }
    public List<Report> getReportsByStatus(ReportStatus status) { // Use the enum
        return reportRepository.findByStatus(status.name()); // Convert enum to string
    }

    @Transactional
    public Report createReport(ReportDto.Request requestDto) {
        // 1. Validate that the reporter (user) exists.
        User reporter = userRepository.findById(requestDto.reporterId())
                .orElseThrow(() -> new EntityNotFoundException("Reporter with ID " + requestDto.reporterId() + " not found."));

        // 2. Validate that the reported entity exists, based on reportedType.
        switch (requestDto.reportedType()) {
            case "video":
                videoRepository.findById(requestDto.reportedId())
                        .orElseThrow(() -> new EntityNotFoundException("Video with ID " + requestDto.reportedId() + " not found."));
                break;
            case "comment":
                commentRepository.findById(requestDto.reportedId())
                        .orElseThrow(() -> new EntityNotFoundException("Comment with ID " + requestDto.reportedId() + " not found."));
                break;
            // ... add cases for other reportable entities (e.g., "user", "playlist")
            default:
                throw new IllegalArgumentException("Invalid reportedType: " + requestDto.reportedType());
        }

        // 3. Create and populate the Report entity
        Report report = new Report();
        report.setReporter(reporter); // Set the User entity
        report.setReportedType(requestDto.reportedType());
        report.setReportedId(requestDto.reportedId());
        report.setReason(requestDto.reason());
        report.setStatus(ReportStatus.PENDING.name()); // Use the enum, convert to String

        // 4. Save the report
        return reportRepository.save(report);
    }

    @Transactional
    public Report updateReport(Integer id, ReportDto.UpdateStatusRequest requestDto) {
        Report existingReport = reportRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Report with id " + id + " not found."));

        // Update only the status, using the enum value
        existingReport.setStatus(requestDto.status().name());
        return reportRepository.save(existingReport);
    }


    @Transactional
    public void deleteReport(Integer id) {
        Report report = reportRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Report not found with id: " + id));
        reportRepository.delete(report);
    }
}