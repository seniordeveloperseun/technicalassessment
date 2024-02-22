package com.banking.app.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.banking.app.model.Transaction;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    @Query(value = "SELECT * FROM transaction WHERE credit_account_number = ?1 OR debit_account_number = ?1", nativeQuery = true)
    List<Transaction> findAllByAccountNumber(String accountNumber);
}
