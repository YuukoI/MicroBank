package com.microbank.merchant_service.dtos;

import com.microbank.merchant_service.entity.MerchantStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MerchantUpdateDTO {
        private String name;
        private MerchantStatus status;
}
