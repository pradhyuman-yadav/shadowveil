package com.shadowveil.videoplatform.controller;

import com.shadowveil.videoplatform.dto.AuditLogDto;
import com.shadowveil.videoplatform.entity.AuditLog;
import com.shadowveil.videoplatform.service.AuditLogService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/audit-logs")
public class AuditLogController {

    private final AuditLogService auditLogService;

    @Autowired
    public AuditLogController(AuditLogService auditLogService) {
        this.auditLogService = auditLogService;
    }

    @GetMapping
    public ResponseEntity<List<AuditLogDto.Response>> getAllAuditLogs() {
        List<AuditLogDto.Response> auditLogs = auditLogService.getAllAuditLogs();
        return new ResponseEntity<>(auditLogs, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AuditLogDto.Response> getAuditLogById(@PathVariable Integer id) {
        Optional<AuditLogDto.Response> auditLog = auditLogService.getAuditLogById(id);
        return auditLog.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping("/admin/{adminId}")
    public ResponseEntity<List<AuditLogDto.Response>> getAuditLogsByAdminId(@PathVariable Integer adminId) {
        List<AuditLogDto.Response> auditLogs = auditLogService.getAuditLogsByAdminId(adminId);
        return new ResponseEntity<>(auditLogs, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<AuditLogDto.Response> createAuditLog(@Valid @RequestBody AuditLogDto.Request auditLogDto) {
        AuditLog createdAuditLog = auditLogService.createAuditLog(auditLogDto);
        AuditLogDto.Response responseDto = auditLogService.convertToDto(createdAuditLog);
        return new ResponseEntity<>(responseDto, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<AuditLogDto.Response> updateAuditLog(@PathVariable Integer id, @Valid @RequestBody AuditLogDto.Request auditLogDto) {
        AuditLog updatedAuditLog = auditLogService.updateAuditLog(id, auditLogDto);
        AuditLogDto.Response responseDto = auditLogService.convertToDto(updatedAuditLog);
        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAuditLog(@PathVariable Integer id) {
        auditLogService.deleteAuditLog(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}