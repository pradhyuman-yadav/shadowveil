//File: src/main/java/com/shadowveil/videoplatform/service/ReportService.java
package com.shadowveil.videoplatform.service;

import com.shadowveil.videoplatform.entity.*;
import com.shadowveil.videoplatform.repository.*;
import com.shadowveil.videoplatform.Util.ReportStatus;
import com.shadowveil.videoplatform.Util.ReactionType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

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

    public Optional<Report> getReportById(Integer id) {
        return reportRepository.findById(id);
    }

    public List<Report> getReportsByReporterId(Integer reporterId) {
        return reportRepository.findByReporterId(reporterId);
    }

    public List<Report> getReportsByReportedEntity(String reportedType, Integer reportedId) {
        return reportRepository.findByReportedTypeAndReportedId(reportedType, reportedId);
    }

    public List<Report> getReportByStatus(String status){
        return reportRepository.findByStatus(status);
    }

    @Transactional
    public Report createReport(Report report) {
        // 1. Validate that the reporter (user) exists.
        Optional<User> reporter = userRepository.findById(report.getReporter().getId());
        if (reporter.isEmpty()) {
            throw new IllegalArgumentException("Reporter with ID " + report.getReporter().getId() + " not found.");
        }
        report.setReporter(reporter.get());

        // 2. Validate that the reported entity exists, based on reportedType.
        switch (report.getReportedType()) {
            case "video":
                if (videoRepository.findById(report.getReportedId()).isEmpty()) {
                    throw new IllegalArgumentException("Video with ID " + report.getReportedId() + " not found.");
                }
                break;
            case "comment":
                if (commentRepository.findById(report.getReportedId()).isEmpty()) {
                    throw new IllegalArgumentException("Comment with ID " + report.getReportedId() + " not found.");
                }
                break;
            // ... add cases for other reportable entities (e.g., "user", "playlist")
            default:
                throw new IllegalArgumentException("Invalid reportedType: " + report.getReportedType());
        }

        // 3. Set default status if not provided.
        if (report.getStatus() == null) {
            report.setStatus(String.valueOf(ReportStatus.PENDING)); // Use the enum
        }

        // 4. Save the report
        return reportRepository.save(report);
    }

    @Transactional
    public Report updateReport(Integer id, Report reportDetails) {
        Optional<Report> optionalReport = reportRepository.findById(id);
        if (optionalReport.isPresent()) {
            Report existingReport = optionalReport.get();

            // Validate reporter, if reporter is being changed.
            if(reportDetails.getReporter() != null && !reportDetails.getReporter().getId().equals(existingReport.getReporter().getId())){
                Optional<User> reporter = userRepository.findById(reportDetails.getReporter().getId());
                if(reporter.isEmpty()){
                    throw new IllegalArgumentException("Reporter with ID " + reportDetails.getReporter().getId()+ " does not exists");
                }
                existingReport.setReporter(reporter.get());
            }
            // Validate reported entity if it's being changed
            if(!reportDetails.getReportedType().equals(existingReport.getReportedType()) || !reportDetails.getReportedId().equals(existingReport.getReportedId())){
                switch (reportDetails.getReportedType()){
                    case "video":
                        if (videoRepository.findById(reportDetails.getReportedId()).isEmpty()) {
                            throw new IllegalArgumentException("Video with ID " + reportDetails.getReportedId() + " not found.");
                        }
                        break;
                    case "comment":
                        if (commentRepository.findById(reportDetails.getReportedId()).isEmpty()) {
                            throw new IllegalArgumentException("Comment with ID " + reportDetails.getReportedId() + " not found.");
                        }
                        break;
                    // ... add cases for other reportable entities (e.g., "user", "playlist")
                    default:
                        throw new IllegalArgumentException("Invalid reportedType: " + reportDetails.getReportedType());
                }
            }

            existingReport.setReportedType(reportDetails.getReportedType());
            existingReport.setReportedId(reportDetails.getReportedId());
            existingReport.setReason(reportDetails.getReason());
            existingReport.setStatus(reportDetails.getStatus()); // Use the enum here as well

            return reportRepository.save(existingReport);
        } else {
            return null; // Or throw an exception
        }
    }

    @Transactional
    public void deleteReport(Integer id) {
        if(reportRepository.existsById(id)){
            reportRepository.deleteById(id);
        }
        else{
            throw new IllegalArgumentException("Report with ID " + id + " does not exists");
        }
    }
    public List<Report> getReportsByStatus(ReportStatus status) { // Use the enum type
        return reportRepository.findByStatus(status.name()); // Use .name()
    }
}