package com.banking.app.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.banking.app.model.Account;

public interface AccountRepository extends JpaRepository<Account, Long> {
}
