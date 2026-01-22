package com.microbank.wallet_service.service;

import com.microbank.wallet_service.entity.TransactionType;
import com.microbank.wallet_service.entity.WalletTransaction;
import com.microbank.wallet_service.repository.WalletTransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class WalletTransactionServiceImp implements WalletTransactionService {

    private final WalletTransactionRepository walletTransactionRepository;


    @Override
    public Page<WalletTransaction> findAllPaged(Pageable pageable) {
        return walletTransactionRepository.findAll(pageable);
    }

    @Override
    public Optional<WalletTransaction> findById(Long id) {
        return walletTransactionRepository.findById(id);
    }

    @Override
    public Page<WalletTransaction> findByWalletId(Long walletId, Pageable pageable) {
        return walletTransactionRepository.findByWalletId(walletId, pageable);
    }

    @Override
    public Page<WalletTransaction> searchByWalletIdOrTransactionType(String keyword, Pageable pageable) {
        if (keyword.matches("\\d+")) {
            return walletTransactionRepository.findByWalletId(Long.parseLong(keyword), pageable);
        }

        try {
            TransactionType type = TransactionType.valueOf(keyword.toUpperCase());
            return walletTransactionRepository.findByType(type, pageable);
        } catch (IllegalArgumentException e) {
            return Page.empty(pageable);
        }
    }

    @Override
    public WalletTransaction save(WalletTransaction transaction) {
        return walletTransactionRepository.save(transaction);
    }
}
