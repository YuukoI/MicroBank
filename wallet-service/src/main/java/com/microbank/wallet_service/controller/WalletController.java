package com.microbank.wallet_service.controller;

import com.microbank.wallet_service.dto.*;
import com.microbank.wallet_service.entity.Wallet;
import com.microbank.wallet_service.entity.WalletTransaction;
import com.microbank.wallet_service.service.WalletService;
import com.microbank.wallet_service.service.WalletTransactionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/microbank/wallets")
public class WalletController {

    private final WalletService walletService;
    private final WalletTransactionService walletTransactionService;

    @GetMapping
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ResponseEntity<Page<WalletResponseDTO>> findAllPaged(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "15") int size,
            @RequestParam(defaultValue = "id") String sortBy) {

        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));

        Page<WalletResponseDTO> wallets = walletService
                .findAllPaged(pageable)
                .map(this::toResponseDto);

        return ResponseEntity.ok(wallets);
    }

    @GetMapping("/search")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ResponseEntity<Page<WalletResponseDTO>> search(
            @RequestParam String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "15") int size,
            @RequestParam(defaultValue = "id") String sortBy) {

        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));

        Page<WalletResponseDTO> result = walletService
                .searchByUsernameOrStatus(keyword, pageable)
                .map(this::toResponseDto);

        return ResponseEntity.ok(result);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ResponseEntity<WalletResponseDTO> getWalletById(@PathVariable Long id) {
        return walletService.findById(id)
                .map(wallet -> ResponseEntity.ok(toResponseDto(wallet)))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<WalletResponseDTO> createWallet(@Valid @RequestBody WalletCreateDTO dto) {

        Wallet wallet = walletService.save(dto.getUsername());

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(toResponseDto(wallet));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<WalletResponseDTO> updateWallet(@PathVariable Long id, @RequestBody WalletUpdateDTO dto) {

        return walletService.update(id, dto)
                .map(wallet -> ResponseEntity.ok(toResponseDto(wallet)))
                .orElse(ResponseEntity.notFound().build());
    }

    private WalletResponseDTO toResponseDto(Wallet wallet) {
        return new WalletResponseDTO(
                wallet.getId(),
                wallet.getUsername(),
                wallet.getBalance(),
                wallet.getStatus()
        );
    }

    @GetMapping("/{walletId}/transactions")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ResponseEntity<Page<WalletTransaction>> getTransactions(
            @PathVariable Long walletId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "15") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy
    ) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy).descending());

        Page<WalletTransaction> transactions =
                walletTransactionService.findByWalletId(walletId, pageable);

        return ResponseEntity.ok(transactions);
    }

    @GetMapping("/{walletId}/transactions/{transactionId}")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ResponseEntity<WalletTransaction> getTransaction(@PathVariable Long walletId, @PathVariable Long transactionId) {
        return walletTransactionService.findById(transactionId)
                .filter(tx -> tx.getWalletId().equals(walletId))
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/{walletId}/recharge")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ResponseEntity<WalletResponseDTO> recharge(@PathVariable Long walletId, @Valid @RequestBody WalletOperationDTO dto) {
        Wallet wallet = walletService.recharge(walletId, dto.getAmount());
        return ResponseEntity.ok(toResponseDto(wallet));
    }

    @PostMapping("/{walletId}/payment")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<WalletResponseDTO> payment(@PathVariable Long walletId, @Valid @RequestBody WalletPaymentDTO dto) {
        Wallet wallet = walletService.payment(walletId, dto);
        return ResponseEntity.ok(toResponseDto(wallet));
    }

}
