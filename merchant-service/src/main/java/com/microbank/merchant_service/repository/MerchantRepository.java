package com.microbank.merchant_service.repository;

import com.microbank.merchant_service.entity.Merchant;
import com.microbank.merchant_service.entity.MerchantStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MerchantRepository extends JpaRepository<Merchant,Long> {

    Optional<Merchant> findByName(String merchantName);

    Page<Merchant> findAll(Pageable pageable);

    Page<Merchant> findByNameContainingIgnoreCaseOrStatus(String username, MerchantStatus status, Pageable pageable);

    boolean existsByName(String merchantName);

    Page<Merchant> findByNameContainingIgnoreCase(String merchantName, Pageable pageable);

}
