package com.microbank.audit_service.service;

import com.microbank.audit_service.entity.AuditLog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface AuditService {

    AuditLog saveAudit(AuditLog audit);

    Page<AuditLog> getAllAudits(Pageable pageable);

    Optional<AuditLog> getAuditById(Long id);

    Page<AuditLog> searchAuditByKeyword(String keyword, Pageable pageable);
}
