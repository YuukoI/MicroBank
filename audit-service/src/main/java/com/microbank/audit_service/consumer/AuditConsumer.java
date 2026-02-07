package com.microbank.audit_service.consumer;

import com.microbank.audit_service.dto.AuditMessage;
import com.microbank.audit_service.entity.AuditLog;
import com.microbank.audit_service.repository.AuditRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@Slf4j
@RequiredArgsConstructor
public class AuditConsumer {

    private final AuditRepository auditRepository;

    @KafkaListener(topics = "audit-topic", groupId = "audit-service-group")
    public void consume(AuditMessage message) {
        log.info("Audit event received: {} from {}", message.getAction(), message.getServiceName());

        try {
            AuditLog auditLog = AuditLog.builder()
                    .serviceName(message.getServiceName())
                    .action(message.getAction())
                    .username(message.getUsername())
                    .entityId(message.getEntityId())
                    .details(message.getDetails())
                    .traceId(message.getTraceId())
                    .timestamp(message.getTimestamp() != null ? message.getTimestamp() : LocalDateTime.now())
                    .build();

            auditRepository.save(auditLog);
        } catch (Exception e) {
            log.error("Error processing audt message: {}", e.getMessage());
        }
    }
}
