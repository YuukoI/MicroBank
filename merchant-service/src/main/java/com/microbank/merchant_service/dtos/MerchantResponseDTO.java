package com.microbank.merchant_service.dtos;

import com.microbank.merchant_service.entity.MerchantStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MerchantResponseDTO {
    private Long id;
    private String name;
    private MerchantStatus status;
    private BigDecimal balance;
}