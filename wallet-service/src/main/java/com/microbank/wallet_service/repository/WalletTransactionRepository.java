package com.microbank.wallet_service.repository;

import com.microbank.wallet_service.entity.TransactionType;
import com.microbank.wallet_service.entity.WalletTransaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WalletTransactionRepository extends JpaRepository<WalletTransaction,Long> {

    Page<WalletTransaction> findByWalletId(Long walletId, Pageable pageable);

    Page<WalletTransaction> findByType(TransactionType type, Pageable pageable);
}