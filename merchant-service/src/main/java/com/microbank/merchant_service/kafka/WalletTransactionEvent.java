package com.microbank.merchant_service.kafka;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WalletTransactionEvent {

    private String eventId;
    private String type;
    private Long walletId;
    private Long merchantId;
    private BigDecimal amount;
    private String reference;
    private LocalDateTime createdAt;
}
