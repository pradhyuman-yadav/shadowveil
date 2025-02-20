package com.shadowveil.videoplatform.repository;

import com.shadowveil.videoplatform.entity.Certificate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CertificateRepository extends JpaRepository<Certificate, Integer> {
    Optional<Certificate> findByEnrollmentId(Integer enrollmentId);
}