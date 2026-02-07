package com.microbank.audit_service.repository;

import com.microbank.audit_service.entity.AuditLog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface AuditRepository extends JpaRepository<AuditLog,Long> {

    @Query("SELECT a FROM AuditLog a WHERE " +
            "LOWER(a.serviceName) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(a.action) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(a.username) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(a.entityId) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(a.traceId) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    Page<AuditLog> findByKeyword(@Param("keyword") String keyword, Pageable pageable);
}
