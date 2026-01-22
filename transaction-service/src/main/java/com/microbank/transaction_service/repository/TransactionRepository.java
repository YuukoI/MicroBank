package com.microbank.transaction_service.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.microbank.transaction_service.entity.Transaction;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    @Query(value = "SELECT * FROM transactions t WHERE " +
            "t.event_type LIKE %:keyword% OR " +
            "t.trace_id LIKE %:keyword% OR " +
            "CAST(t.payload AS CHAR) LIKE %:keyword%",
            nativeQuery = true)
    Page<Transaction> findByKeyword(@Param("keyword") String keyword, Pageable pageable);

}
