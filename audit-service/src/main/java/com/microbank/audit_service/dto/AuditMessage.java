package com.microbank.audit_service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AuditMessage {
    private String serviceName;
    private String action;
    private String username;
    private String details;
    private String entityId;
    private String traceId;
    private LocalDateTime timestamp;
}
