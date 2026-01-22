package com.microbank.wallet_service.kafka;

import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class WalletTransactionProducer {

    private static final String TOPIC = "wallet.transactions";

    private final KafkaTemplate<String, WalletTransactionEvent> kafkaTemplate;

    public void send(WalletTransactionEvent event) {
        kafkaTemplate.send(TOPIC, event.getEventId(), event);
    }
}
