package com.microbank.wallet_service.interceptor;

import com.microbank.wallet_service.dto.AuditMessage;
import io.micrometer.tracing.Tracer;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class AuditInterceptor implements HandlerInterceptor {

    private final KafkaTemplate<String, Object> kafkaTemplate;
    private final Tracer tracer;

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        String method = request.getMethod();

        if (method.equalsIgnoreCase("GET")) {
            return;
        }

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = (auth != null && auth.isAuthenticated()) ? auth.getName() : "SYSTEM";

        String traceId = (tracer.currentSpan() != null)
                ? tracer.currentSpan().context().traceId()
                : "N/A";

        AuditMessage audit = AuditMessage.builder()
                .serviceName("wallet-service")
                .action(method + " " + request.getRequestURI())
                .username(username)
                .details("HTTP Status: " + response.getStatus())
                .traceId(traceId)
                .timestamp(LocalDateTime.now())
                .build();

        kafkaTemplate.send("audit-topic", audit);
    }
}
