package com.microbank.transaction_service.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.microbank.transaction_service.entity.Transaction;
import com.microbank.transaction_service.service.TransactionService;
import io.micrometer.tracing.Tracer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
@Slf4j
public class TransactionListener {

    private final TransactionService transactionService;
    private final Tracer tracer;
    private final ObjectMapper objectMapper;

    @KafkaListener(topics = "user-created-topic", groupId = "transaction-service")
    public void handleUserCreatedEvent(com.microbank.transaction_service.kafka.UserCreatedEvent event) {
        log.info("Received UserCreatedEvent with traceId: {}", tracer.currentSpan().context().traceId());

        Transaction tx = new Transaction();
        tx.setTraceId(tracer.currentSpan().context().traceId());
        tx.setEventType("UserCreatedEvent");
        tx.setSourceService("auth-service");
        tx.setPayload(objectMapper.valueToTree(event));
        tx.setCreatedAt(LocalDateTime.now());

        transactionService.saveTransaction(tx);
    }

    @KafkaListener(topics = "wallet.transactions", groupId = "transaction-service")
    public void handleWalletTransactionEvent(com.microbank.transaction_service.kafka.WalletTransactionEvent event) {
        Transaction tx = new Transaction();
        tx.setTraceId(tracer.currentSpan().context().traceId());
        tx.setEventType("WalletTransactionEvent");
        tx.setSourceService("wallet-service");
        tx.setPayload(objectMapper.valueToTree(event));
        tx.setCreatedAt(LocalDateTime.now());

        transactionService.saveTransaction(tx);
    }
}