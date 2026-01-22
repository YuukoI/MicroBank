package com.microbank.wallet_service.service;

import com.microbank.wallet_service.entity.WalletTransaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface WalletTransactionService {

    Page<WalletTransaction> findAllPaged(Pageable pageable);

    Optional<WalletTransaction> findById(Long id);

    Page<WalletTransaction> findByWalletId(Long walletId, Pageable pageable);

    Page<WalletTransaction> searchByWalletIdOrTransactionType(String keyword,Pageable pageable);

    WalletTransaction save(WalletTransaction transaction);

}
