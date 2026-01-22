package com.microbank.merchant_service.kafka;

import com.microbank.merchant_service.service.MerchantService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class WalletTransactionConsumer {

    private final MerchantService merchantService;

    @KafkaListener(
            topics = "wallet.transactions",
            groupId = "merchant-service"
    )
    public void consume(WalletTransactionEvent event) {

        log.info("Received event: {}", event);

        try {
            merchantService.processPayment(event);
        } catch (Exception e) {
            log.error(
                    "Error processing payment eventId={}, merchantId={}",
                    event.getEventId(),
                    event.getMerchantId(),
                    e
            );
        }
    }
}
