package com.microbank.transaction_service.service;

import com.microbank.transaction_service.entity.Transaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface TransactionService {

    Transaction saveTransaction(Transaction transaction);

    Page<Transaction> getAllTransactions(Pageable pageable);

    Optional<Transaction> getTransactionById(Long id);

    Page<Transaction> searchByKeyword(String keyword, Pageable pageable);

}
