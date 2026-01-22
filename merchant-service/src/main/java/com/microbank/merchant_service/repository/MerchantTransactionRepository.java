package com.microbank.merchant_service.repository;

import com.microbank.merchant_service.entity.MerchantTransaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MerchantTransactionRepository extends JpaRepository<MerchantTransaction, Long> {

    boolean existsByEventId(String eventId);

}
