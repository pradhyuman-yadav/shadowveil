package com.shadowveil.videoplatform.controller;

import com.shadowveil.videoplatform.dto.CertificateDto;
import com.shadowveil.videoplatform.entity.Certificate;
import com.shadowveil.videoplatform.service.CertificateService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/certificates")
public class CertificateController {

    private final CertificateService certificateService;

    @Autowired
    public CertificateController(CertificateService certificateService) {
        this.certificateService = certificateService;
    }

    @GetMapping
    public ResponseEntity<List<CertificateDto.Response>> getAllCertificates() {
        List<CertificateDto.Response> certificates = certificateService.getAllCertificates();
        return new ResponseEntity<>(certificates, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CertificateDto.Response> getCertificateById(@PathVariable Integer id) {
        Optional<CertificateDto.Response> certificate = certificateService.getCertificateById(id);
        return certificate.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping("/enrollment/{enrollmentId}")
    public ResponseEntity<CertificateDto.Response> getCertificateByEnrollmentId(@PathVariable Integer enrollmentId) {
        Optional<CertificateDto.Response> certificate = certificateService.getCertificateByEnrollmentId(enrollmentId);
        return certificate.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping
    public ResponseEntity<CertificateDto.Response> createCertificate(@Valid @RequestBody CertificateDto.Request certificateDto) {
        Certificate createdCertificate = certificateService.createCertificate(certificateDto);
        CertificateDto.Response responseDto = certificateService.convertToDto(createdCertificate);
        return new ResponseEntity<>(responseDto, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CertificateDto.Response> updateCertificate(@PathVariable Integer id, @Valid @RequestBody CertificateDto.Request certificateDto) {
        Certificate updatedCertificate = certificateService.updateCertificate(id, certificateDto);
        CertificateDto.Response responseDto = certificateService.convertToDto(updatedCertificate);
        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCertificate(@PathVariable Integer id) {
        certificateService.deleteCertificate(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}