package com.banking.app.service.impl;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.banking.app.model.Account;
import com.banking.app.model.dto.ApiResponse;
import com.banking.app.model.dto.CreateAccountDto;
import com.banking.app.model.dto.UpdateAccountDto;
import com.banking.app.repository.AccountRepository;
import com.banking.app.service.AccountService;

@Service
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;

    @Autowired
    public AccountServiceImpl(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @Override
    public ApiResponse<Account> create(CreateAccountDto request) {
        try {
            var account = Account.builder()
                .accountName(request.getAccountName())
                .accountNumber(generateAccountNumber())
                .bvn(request.getBvn())
                .accountType(request.getAccountType())
                .availableBalance(BigDecimal.ZERO)
                .ledgerBalance(BigDecimal.ZERO)
                .isDeleted(false)
                .build();

            account = accountRepository.save(account);
            return new ApiResponse<Account>().success(account, "Account created successfully");
        } catch (Exception e) {
            return new ApiResponse<Account>().failure("An error occurred when creating account: " + e.getMessage());
        }
    }

    @Override
    public ApiResponse<Object> update(Long accountId, UpdateAccountDto request) {
        try {
            var account = accountRepository.findById(accountId);

            if (!account.isPresent()) {
                return new ApiResponse<>().failure("Account not found");
            }

            account.get().setAccountName(request.getAccountName());
            account.get().setBvn(request.getBvn());

            accountRepository.save(account.get());

            return new ApiResponse<>().success(account, "Account updated successfully");
        } catch (Exception e) {
            return new ApiResponse<>().failure("An error occurred when updating account: " + e.getMessage());
        }
    }

    @Override
    public ApiResponse<Object> delete(Long accountId) {
        try {
            var account = accountRepository.findById(accountId);

            if (!account.isPresent()) {
                return new ApiResponse<>().failure("Account not found");
            }

            accountRepository.delete(account.get());

            return new ApiResponse<>().success(null, "Account updated successfully");
        } catch (Exception e) {
            return new ApiResponse<>().failure("An error occurred when updating account: " + e.getMessage());
        }
    }
    
    @Override
    public ApiResponse<Account> getById(Long accountId) {
        try {
            var account = accountRepository.findById(accountId);

            if (!account.isPresent()) {
                return new ApiResponse<Account>().failure("Account not found");
            }

            return new ApiResponse<Account>().success(account.get(), "Account updated successfully");
        } catch (Exception e) {
            return new ApiResponse<Account>().failure("An error occurred when fetching account: " + e.getMessage());
        }
    }

    @Override
    public ApiResponse<List<Account>> getAll() {
        try {
            var accounts = accountRepository.findAll();

            if (accounts.isEmpty()) {
                return new ApiResponse<List<Account>>().failure("No accounts found");
            }

            return new ApiResponse<List<Account>>().success(accounts, "Accounts fetched successfully");
        } catch (Exception e) {
            return new ApiResponse<List<Account>>().failure("An error occurred when fetching account: " + e.getMessage());
        }
    }

    private String generateAccountNumber() {
        //use some nibss algorithm here
        return String.valueOf(new Date().getTime()).substring(0, 10);
    }
}
