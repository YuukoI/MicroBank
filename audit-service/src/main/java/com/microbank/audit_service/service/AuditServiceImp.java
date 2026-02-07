package com.microbank.audit_service.service;

import com.microbank.audit_service.entity.AuditLog;
import com.microbank.audit_service.repository.AuditRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuditServiceImp implements AuditService {

    private final AuditRepository auditRepository;


    @Override
    public AuditLog saveAudit(AuditLog audit) {
        return auditRepository.save(audit);
    }

    @Override
    public Page<AuditLog> getAllAudits(Pageable pageable) {
        return auditRepository.findAll(pageable);
    }

    @Override
    public Optional<AuditLog> getAuditById(Long id) {
        return  auditRepository.findById(id);
    }

    @Override
    public Page<AuditLog> searchAuditByKeyword(String keyword, Pageable pageable) {
        return auditRepository.findByKeyword(keyword, pageable);
    }

}
