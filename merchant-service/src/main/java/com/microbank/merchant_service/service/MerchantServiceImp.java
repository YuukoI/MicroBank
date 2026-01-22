package com.microbank.merchant_service.service;

import com.microbank.merchant_service.entity.Merchant;
import com.microbank.merchant_service.entity.MerchantStatus;
import com.microbank.merchant_service.entity.MerchantTransaction;
import com.microbank.merchant_service.entity.TransactionStatus;
import com.microbank.merchant_service.kafka.WalletTransactionEvent;
import com.microbank.merchant_service.repository.MerchantRepository;
import com.microbank.merchant_service.repository.MerchantTransactionRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MerchantServiceImp implements MerchantService {

    private final MerchantRepository merchantRepository;
    private final MerchantTransactionRepository merchantTransactionRepository;

    @Override
    public List<Merchant> findAll() {
        return merchantRepository.findAll();
    }

    @Override
    public Optional<Merchant> findById(Long id) {
        return merchantRepository.findById(id);
    }

    @Override
    public Optional<Merchant> findByMerchantName(String merchantName) {
        return merchantRepository.findByName(merchantName);
    }

    @Override
    public Merchant save(Merchant merchant) {
        return merchantRepository.save(merchant);
    }

    @Override
    public void deleteById(Long id) {
        merchantRepository.deleteById(id);
    }

    @Override
    public Page<Merchant> findAllPaged(Pageable pageable) {
        return merchantRepository.findAll(pageable);
    }

    @Override
    public Page<Merchant> searchByMerchantOrStatus(String keyword, Pageable pageable) {
        MerchantStatus status = null;

        try {
            status = MerchantStatus.valueOf(keyword.toUpperCase());
        } catch (IllegalArgumentException ignored) {
        }

        return merchantRepository
                .findByNameContainingIgnoreCaseOrStatus(keyword, status, pageable);
    }

    @Override
    public boolean existsByMerchantName(String merchantName) {
        return merchantRepository.existsByName(merchantName);
    }

    @Override
    @Transactional
    public void processPayment(WalletTransactionEvent event) {

        if (merchantTransactionRepository.existsByEventId(event.getEventId())) {
            return;
        }

        if (!"DEBIT".equals(event.getType())) {
            return;
        }

        Merchant merchant = merchantRepository.findById(event.getMerchantId())
                .orElseThrow(() ->
                        new RuntimeException("Merchant not found: " + event.getMerchantId())
                );

        if (merchant.getStatus() != MerchantStatus.ACTIVE) {
            throw new RuntimeException("Merchant inactive");
        }

        merchant.setBalance(
                merchant.getBalance().add(event.getAmount())
        );

        MerchantTransaction tx = new MerchantTransaction();
        tx.setMerchantId(merchant.getId());
        tx.setWalletId(event.getWalletId());
        tx.setAmount(event.getAmount());
        tx.setEventId(event.getEventId());
        tx.setStatus(TransactionStatus.SUCCESS);
        tx.setReference(event.getReference());
        tx.setCreatedAt(event.getCreatedAt());

        merchantRepository.save(merchant);
        merchantTransactionRepository.save(tx);
    }

}