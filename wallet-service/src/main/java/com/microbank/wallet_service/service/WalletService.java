package com.microbank.wallet_service.service;

import com.microbank.wallet_service.dto.WalletPaymentDTO;
import com.microbank.wallet_service.dto.WalletUpdateDTO;
import com.microbank.wallet_service.entity.Wallet;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface WalletService {

    List<Wallet> findAll();

    Optional<Wallet> findById(Long id);

    Optional<Wallet> findByUsername(String username);

    Wallet save(String username);

    Optional<Wallet> update(Long id, WalletUpdateDTO dto);

    void deleteById(Long id);

    Page<Wallet> findAllPaged(Pageable pageable);

    public Page<Wallet> searchByUsernameOrStatus(String keyword, Pageable pageable);

    public boolean existsByUsername(String username);

    public Wallet recharge(Long walletId, BigDecimal amount);

    public Wallet payment(Long walletId, WalletPaymentDTO dto);
}
