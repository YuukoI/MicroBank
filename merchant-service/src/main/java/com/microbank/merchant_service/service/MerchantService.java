package com.microbank.merchant_service.service;

import com.microbank.merchant_service.entity.Merchant;
import com.microbank.merchant_service.kafka.WalletTransactionEvent;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface MerchantService {

    List<Merchant> findAll();

    Optional<Merchant> findById(Long id);

    Optional<Merchant> findByMerchantName(String merchantName);

    Merchant save(Merchant merchant);

    void deleteById(Long id);

    Page<Merchant> findAllPaged(Pageable pageable);

    public Page<Merchant> searchByMerchantOrStatus(String keyword, Pageable pageable);

    public boolean existsByMerchantName(String merchantName);

    void processPayment(WalletTransactionEvent event);
}
