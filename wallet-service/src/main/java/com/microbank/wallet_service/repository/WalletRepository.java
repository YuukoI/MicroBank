package com.microbank.wallet_service.repository;

import com.microbank.wallet_service.entity.Wallet;
import com.microbank.wallet_service.entity.WalletStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface WalletRepository extends JpaRepository<Wallet, Long> {

    Optional<Wallet> findByUsername(String username);

    Page<Wallet> findAll(Pageable pageable);

    Page<Wallet> findByUsernameContainingIgnoreCaseOrStatus(String username, WalletStatus walletStatus, Pageable pageable);

    boolean existsByUsername(String username);

    Page<Wallet> findByUsernameContainingIgnoreCase(String merchantName, Pageable pageable);

}
