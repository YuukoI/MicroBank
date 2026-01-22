package com.microbank.wallet_service.dto;

import com.microbank.wallet_service.entity.WalletStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WalletResponseDTO {

    private Long id;

    private String username;

    private BigDecimal balance;

    private WalletStatus status;
}
