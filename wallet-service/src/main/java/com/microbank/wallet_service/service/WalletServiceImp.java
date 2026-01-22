package com.microbank.wallet_service.service;

import com.microbank.wallet_service.dto.WalletPaymentDTO;
import com.microbank.wallet_service.dto.WalletUpdateDTO;
import com.microbank.wallet_service.entity.TransactionType;
import com.microbank.wallet_service.entity.Wallet;
import com.microbank.wallet_service.entity.WalletStatus;
import com.microbank.wallet_service.entity.WalletTransaction;
import com.microbank.wallet_service.kafka.WalletTransactionEvent;
import com.microbank.wallet_service.kafka.WalletTransactionProducer;
import com.microbank.wallet_service.repository.WalletRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class WalletServiceImp implements WalletService {

    private final WalletRepository walletRepository;
    private final WalletTransactionService  walletTransactionService;
    private final WalletTransactionProducer walletTransactionProducer;

    @Override
    public List<Wallet> findAll() {
        return walletRepository.findAll();
    }

    @Override
    public Optional<Wallet> findById(Long id) {
        return walletRepository.findById(id);
    }

    @Override
    public Optional<Wallet> findByUsername(String username) {
        return walletRepository.findByUsername(username);
    }

    @Override
    public Wallet save(String username) {
        walletRepository.findByUsername(username)
                .ifPresent(w -> {
                    throw new ResponseStatusException(
                            HttpStatus.CONFLICT, "Wallet already exists");
                });

        Wallet wallet = new Wallet();
        wallet.setUsername(username);

        return walletRepository.save(wallet);
    }

    @Override
    public Optional<Wallet> update(Long id, WalletUpdateDTO dto) {

        Wallet wallet = walletRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        if (dto.getUsername() != null &&
                !dto.getUsername().equals(wallet.getUsername())) {

            walletRepository.findByUsername(dto.getUsername())
                    .ifPresent(w -> {
                        throw new ResponseStatusException(HttpStatus.CONFLICT);
                    });

            wallet.setUsername(dto.getUsername());
        }

        if (dto.getStatus() != null) {
            wallet.setStatus(dto.getStatus());
        }

        return Optional.of(walletRepository.save(wallet));
    }

    @Override
    public void deleteById(Long id) {
        walletRepository.deleteById(id);
    }

    @Override
    public Page<Wallet> findAllPaged(Pageable pageable) {
        return walletRepository.findAll(pageable);
    }

    @Override
    public Page<Wallet> searchByUsernameOrStatus(String keyword, Pageable pageable) {
        return walletRepository.findByUsernameContainingIgnoreCase(keyword, pageable);
    }

    @Override
    public boolean existsByUsername(String username) {
        return walletRepository.existsByUsername(username);
    }

    @Override
    public Wallet recharge(Long walletId, BigDecimal amount) {
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Amount must be positive");
        }

        Wallet wallet = walletRepository.findById(walletId)
                .orElseThrow(() -> new RuntimeException("Wallet not found"));

        if (wallet.getStatus() != WalletStatus.ACTIVE) {
            throw new RuntimeException("Wallet not active");
        }

        BigDecimal newBalance = wallet.getBalance().add(amount);
        wallet.setBalance(newBalance);

        WalletTransaction tx = new WalletTransaction();
        tx.setWalletId(walletId);
        tx.setType(TransactionType.CREDIT);
        tx.setAmount(amount);
        tx.setBalanceAfter(newBalance);
        tx.setCreatedAt(LocalDateTime.now());

        walletTransactionService.save(tx);

        return walletRepository.save(wallet);
    }

    @Override
    @Transactional
    public Wallet payment(Long walletId, WalletPaymentDTO dto) {

        BigDecimal amount = dto.getAmount();

        Wallet wallet = walletRepository.findById(walletId)
                .orElseThrow(() -> new RuntimeException("Wallet not found"));

        if (wallet.getBalance().compareTo(amount) < 0) {
            throw new RuntimeException("Insufficient balance");
        }

        BigDecimal newBalance = wallet.getBalance().subtract(amount);
        wallet.setBalance(newBalance);

        String eventId = UUID.randomUUID().toString();

        WalletTransaction tx = new WalletTransaction();
        tx.setWalletId(walletId);
        tx.setType(TransactionType.DEBIT);
        tx.setAmount(amount);
        tx.setBalanceAfter(newBalance);
        tx.setReference("MERCHANT:" + dto.getMerchantId());
        tx.setCreatedAt(LocalDateTime.now());

        walletRepository.save(wallet);
        walletTransactionService.save(tx);

        WalletTransactionEvent event = new WalletTransactionEvent(
                eventId,
                "DEBIT",
                walletId,
                dto.getMerchantId(),
                amount,
                tx.getReference(),
                LocalDateTime.now()
        );

        walletTransactionProducer.send(event);

        return wallet;
    }

}
