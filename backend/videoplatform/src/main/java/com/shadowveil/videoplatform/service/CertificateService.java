package com.shadowveil.videoplatform.service;

import com.shadowveil.videoplatform.dto.CertificateDto;
import com.shadowveil.videoplatform.dto.CourseEnrollmentDto;
import com.shadowveil.videoplatform.entity.Certificate;
import com.shadowveil.videoplatform.entity.CourseEnrollment;
import com.shadowveil.videoplatform.exception.BadRequestException;
import com.shadowveil.videoplatform.exception.ResourceNotFoundException;
import com.shadowveil.videoplatform.repository.CertificateRepository;
import com.shadowveil.videoplatform.repository.CourseEnrollmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CertificateService {

    private final CertificateRepository certificateRepository;
    private final CourseEnrollmentRepository courseEnrollmentRepository;
    private final CourseEnrollmentService courseEnrollmentService; // Inject for DTO conversion

    @Autowired
    public CertificateService(CertificateRepository certificateRepository,
                              CourseEnrollmentRepository courseEnrollmentRepository, CourseEnrollmentService courseEnrollmentService) {
        this.certificateRepository = certificateRepository;
        this.courseEnrollmentRepository = courseEnrollmentRepository;
        this.courseEnrollmentService = courseEnrollmentService;
    }

    @Transactional(readOnly = true)
    public List<CertificateDto.Response> getAllCertificates() {
        return certificateRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Optional<CertificateDto.Response> getCertificateById(Integer id) {
        return certificateRepository.findById(id)
                .map(this::convertToDto);
    }

    @Transactional(readOnly = true)
    public Optional<CertificateDto.Response> getCertificateByEnrollmentId(Integer enrollmentId) {
        return certificateRepository.findByEnrollmentId(enrollmentId)
                .map(this::convertToDto);
    }

    @Transactional
    public Certificate createCertificate(CertificateDto.Request certificateDto) {
        CourseEnrollment enrollment = courseEnrollmentRepository.findById(certificateDto.enrollmentId())
                .orElseThrow(() -> new ResourceNotFoundException("CourseEnrollment with ID " + certificateDto.enrollmentId() + " not found."));

        // Check if a certificate already exists
        Optional<Certificate> existingCertificate = certificateRepository.findByEnrollmentId(certificateDto.enrollmentId());
        if (existingCertificate.isPresent()) {
            throw new BadRequestException("A certificate already exists for enrollment ID " + certificateDto.enrollmentId());
        }

        Certificate certificate = new Certificate();
        certificate.setEnrollment(enrollment);
        certificate.setCertificateUrl(certificateDto.certificateUrl()); // Can be null initially
        return certificateRepository.save(certificate);
    }

    @Transactional
    public Certificate updateCertificate(Integer id, CertificateDto.Request certificateDto) {
        Certificate certificate = certificateRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Certificate with ID " + id + " not found"));
        //check for enrollment
        if(certificateDto.enrollmentId() != null && !certificate.getEnrollment().getId().equals(certificateDto.enrollmentId())){
            CourseEnrollment enrollment = courseEnrollmentRepository.findById(certificateDto.enrollmentId())
                    .orElseThrow(()-> new ResourceNotFoundException("Course Enrollment with ID " + certificateDto.enrollmentId() + " does not exists"));
            certificate.setEnrollment(enrollment);
        }

        if(certificateDto.certificateUrl() != null) certificate.setCertificateUrl(certificateDto.certificateUrl());
        return certificateRepository.save(certificate);
    }

    @Transactional
    public void deleteCertificate(Integer id) {
        if (!certificateRepository.existsById(id)) {
            throw new ResourceNotFoundException("Certificate with ID " + id + " not found");
        }
        certificateRepository.deleteById(id);
    }

    public CertificateDto.Response convertToDto(Certificate certificate) {
        CourseEnrollmentDto.Response enrollmentDto = null;
        if (certificate.getEnrollment() != null) {
            enrollmentDto = courseEnrollmentService.convertToDto(certificate.getEnrollment()); // Use CourseEnrollmentService
        }

        return new CertificateDto.Response(
                certificate.getId(),
                enrollmentDto,
                certificate.getCertificateUrl(),
                certificate.getIssuedAt()
        );
    }
}