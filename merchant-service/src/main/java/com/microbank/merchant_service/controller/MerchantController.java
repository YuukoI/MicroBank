package com.microbank.merchant_service.controller;

import com.microbank.merchant_service.dtos.MerchantCreateDTO;
import com.microbank.merchant_service.dtos.MerchantResponseDTO;
import com.microbank.merchant_service.dtos.MerchantUpdateDTO;
import com.microbank.merchant_service.entity.Merchant;
import com.microbank.merchant_service.entity.MerchantStatus;
import com.microbank.merchant_service.service.MerchantService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/microbank/merchant")
public class MerchantController {

    private final MerchantService merchantService;

    @GetMapping
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ResponseEntity<Page<MerchantResponseDTO>> findAllPaged(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "15") int size,
            @RequestParam(defaultValue = "id") String sortBy) {

        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));

        Page<MerchantResponseDTO> merchants = merchantService
                .findAllPaged(pageable)
                .map(this::toResponseDto);

        return ResponseEntity.ok(merchants);
    }

    @GetMapping("/search")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ResponseEntity<Page<MerchantResponseDTO>> search(
            @RequestParam String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "15") int size,
            @RequestParam(defaultValue = "id") String sortBy) {

        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));

        Page<MerchantResponseDTO> result = merchantService
                .searchByMerchantOrStatus(keyword, pageable)
                .map(this::toResponseDto);

        return ResponseEntity.ok(result);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ResponseEntity<MerchantResponseDTO> getMerchantById(@PathVariable Long id) {
        return merchantService.findById(id)
                .map(merchant -> ResponseEntity.ok(toResponseDto(merchant)))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<MerchantResponseDTO> createMerchant(
            @RequestBody MerchantCreateDTO dto) {

        Optional<Merchant> existing = merchantService.findByMerchantName(dto.getName());
        if (existing.isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }

        Merchant merchant = new Merchant();
        merchant.setName(dto.getName());
        merchant.setStatus(MerchantStatus.PENDING);
        merchant.setBalance(BigDecimal.ZERO);

        Merchant saved = merchantService.save(merchant);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(toResponseDto(saved));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<MerchantResponseDTO> updateMerchant(
            @PathVariable Long id,
            @RequestBody MerchantUpdateDTO dto) {

        Merchant merchant = merchantService.findById(id).orElse(null);
        if (merchant == null) {
            return ResponseEntity.notFound().build();
        }

        if (dto.getName() != null) {
            Optional<Merchant> existing = merchantService.findByMerchantName(dto.getName());
            if (existing.isPresent() && !existing.get().getId().equals(id)) {
                return ResponseEntity.status(HttpStatus.CONFLICT).build();
            }
            merchant.setName(dto.getName());
        }

        if (dto.getStatus() != null && dto.getStatus() != merchant.getStatus()) {
            merchant.setStatus(dto.getStatus());
        }

        Merchant saved = merchantService.save(merchant);
        return ResponseEntity.ok(toResponseDto(saved));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteMerchant(@PathVariable Long id) {
        if (merchantService.findById(id).isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        merchantService.deleteById(id);
        return ResponseEntity.ok().build();
    }

    private MerchantResponseDTO toResponseDto(Merchant merchant) {
        return new MerchantResponseDTO(
                merchant.getId(),
                merchant.getName(),
                merchant.getStatus(),
                merchant.getBalance()
        );
    }
}
