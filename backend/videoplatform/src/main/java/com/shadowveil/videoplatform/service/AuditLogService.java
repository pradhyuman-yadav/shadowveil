package com.shadowveil.videoplatform.service;

import com.shadowveil.videoplatform.dto.AuditLogDto;
import com.shadowveil.videoplatform.dto.UserDto;
import com.shadowveil.videoplatform.entity.AuditLog;
import com.shadowveil.videoplatform.entity.User;
import com.shadowveil.videoplatform.exception.ResourceNotFoundException;
import com.shadowveil.videoplatform.repository.AuditLogRepository;
import com.shadowveil.videoplatform.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AuditLogService {

    private final AuditLogRepository auditLogRepository;
    private final UserRepository userRepository;
    private final UserService userService; // Inject for DTO conversion

    @Autowired
    public AuditLogService(AuditLogRepository auditLogRepository, UserRepository userRepository, UserService userService) {
        this.auditLogRepository = auditLogRepository;
        this.userRepository = userRepository;
        this.userService = userService; // Initialize
    }

    @Transactional(readOnly = true)
    public List<AuditLogDto.Response> getAllAuditLogs() {
        return auditLogRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Optional<AuditLogDto.Response> getAuditLogById(Integer id) {
        return auditLogRepository.findById(id)
                .map(this::convertToDto);
    }

    @Transactional(readOnly = true)
    public List<AuditLogDto.Response> getAuditLogsByAdminId(Integer adminId) {
        return auditLogRepository.findByAdminId(adminId).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public AuditLog createAuditLog(AuditLogDto.Request auditLogDto) {
        User admin = userRepository.findById(auditLogDto.adminId())
                .orElseThrow(() -> new ResourceNotFoundException("Admin user with ID " + auditLogDto.adminId() + " not found."));

        AuditLog auditLog = new AuditLog();
        auditLog.setAdmin(admin);
        auditLog.setAction(auditLogDto.action());
        auditLog.setDetails(auditLogDto.details());
        // createdAt will be set by @CreationTimestamp
        return auditLogRepository.save(auditLog);
    }

    @Transactional
    public AuditLog updateAuditLog(Integer id, AuditLogDto.Request auditLogDto) {
        AuditLog auditLog = auditLogRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Audit log with ID " + id + " not found"));

        // Validate admin user if it's being changed.
        if (auditLogDto.adminId() != null && !auditLog.getAdmin().getId().equals(auditLogDto.adminId())) {
            User newAdmin = userRepository.findById(auditLogDto.adminId())
                    .orElseThrow(() -> new ResourceNotFoundException("Admin user with ID " + auditLogDto.adminId() + " not found."));
            auditLog.setAdmin(newAdmin);
        }

        if(auditLogDto.action() != null)  auditLog.setAction(auditLogDto.action());
        if(auditLogDto.details() != null) auditLog.setDetails(auditLogDto.details());
        return auditLogRepository.save(auditLog);
    }

    @Transactional
    public void deleteAuditLog(Integer id) {
        if (!auditLogRepository.existsById(id)) {
            throw new ResourceNotFoundException("Audit log with ID " + id + " not found");
        }
        auditLogRepository.deleteById(id);
    }

    public AuditLogDto.Response convertToDto(AuditLog auditLog) {
        UserDto.Response userDto = null;
        if(auditLog.getAdmin() != null){
            userDto = userService.convertToDto(auditLog.getAdmin()); // Use UserService
        }
        return new AuditLogDto.Response(
                auditLog.getId(),
                userDto, // Use UserService to get the DTO
                auditLog.getAction(),
                auditLog.getDetails(),
                auditLog.getCreatedAt()
        );
    }
}