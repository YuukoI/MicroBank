package com.microbank.wallet_service.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WalletPaymentDTO {

    @NotNull
    private Long merchantId;

    @NotNull
    @Positive
    private BigDecimal amount;
}
