package com.banking.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.banking.app.model.Transaction;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    java.util.List<Transaction> findAllByDebitAccountNumber(String debitAccountNumber);
}
