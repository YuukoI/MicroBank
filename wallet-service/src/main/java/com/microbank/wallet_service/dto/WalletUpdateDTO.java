package com.microbank.wallet_service.dto;

import com.microbank.wallet_service.entity.WalletStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WalletUpdateDTO {

    private String username;

    private WalletStatus status;

}
