package com.banking.app.service.impl;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.banking.app.model.Account;
import com.banking.app.model.Transaction;
import com.banking.app.model.Transaction.TransactionStatus;
import com.banking.app.model.Transaction.TransactionType;
import com.banking.app.model.dto.ApiResponse;
import com.banking.app.model.dto.CreateAccountDto;
import com.banking.app.model.dto.FundDto;
import com.banking.app.model.dto.UpdateAccountDto;
import com.banking.app.repository.AccountRepository;
import com.banking.app.repository.TransactionRepository;
import com.banking.app.service.AccountService;

@Service
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;

    @Autowired
    public AccountServiceImpl(AccountRepository accountRepository, TransactionRepository transactionRepository) {
        this.accountRepository = accountRepository;
        this.transactionRepository = transactionRepository;
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

    @Override
    public ApiResponse<Transaction> makeWithdrawal(Long accountId, FundDto withdawDto) {
        try {
            var account = accountRepository.findById(accountId);
            if (!account.isPresent()) {
                return new ApiResponse<Transaction>().failure("Account not found");
            }
            // call Core Banking Application to withdraw, then update the balance

            account.get().setAvailableBalance(account.get().getAvailableBalance().subtract(withdawDto.getAmount()));
            accountRepository.save(account.get());

            var transaction = Transaction.builder()
            .amount(withdawDto.getAmount())
            .debitAccountNumber(account.get().getAccountNumber())
            .debitAccountName(account.get().getAccountName())
            .paymentReference("REF" + generateAccountNumber())
            .type(TransactionType.DEBIT)
            .status(TransactionStatus.SUCCESS)
            .isDeleted(false)
            .build();

            transaction = transactionRepository.save(transaction);

            return new ApiResponse<Transaction>().success(null, "Withdrawal successful");
        } catch (Exception e) {
            return new ApiResponse<Transaction>().failure("An error occurred when withdrawing from account: " + e.getMessage());
        }
    }

    @Override
    public ApiResponse<Transaction> makeDeposit(Long accountId, FundDto fundDto) {
        try {
            var account = accountRepository.findById(accountId);
            if (!account.isPresent()) {
                return new ApiResponse<Transaction>().failure("Account not found");
            }
            // call Core Banking Application to make deposit, then update the balance

            account.get().setAvailableBalance(account.get().getAvailableBalance().add(fundDto.getAmount()));
            accountRepository.save(account.get());

            var transaction = Transaction.builder()
                .amount(fundDto.getAmount())
                .creditAccountNumber(account.get().getAccountNumber())
                .creditAccountName(account.get().getAccountName())
                .paymentReference("REF" + generateAccountNumber())
                .type(TransactionType.CREDIT)
                .status(TransactionStatus.SUCCESS)
                .isDeleted(false)
                .build();

            transaction = transactionRepository.save(transaction);

            return new ApiResponse<Transaction>().success(transaction, "Funding successful");
        } catch (Exception e) {
            return new ApiResponse<Transaction>().failure("An error occurred when funding account: " + e.getMessage());
        }
    }

    @Override
    public ApiResponse<List<Transaction>> getAccountTransactions(Long accountId) {
        try {

            var account = accountRepository.findById(accountId);
            if (!account.isPresent()) {
                return new ApiResponse<List<Transaction>>().failure("Account not found");
            }

            var transactions = transactionRepository.findAllByDebitAccountNumber(account.get().getAccountNumber());

            if (transactions.isEmpty()) {
                return new ApiResponse<List<Transaction>>().failure("No transactions found");
            }

            return new ApiResponse<List<Transaction>>().success(transactions, "Transactions fetched successfully");
        } catch (Exception e) {
            return new ApiResponse<List<Transaction>>().failure("An error occurred when fetching account transactions: " + e.getMessage());
        }
    }

    private String generateAccountNumber() {
        //use some nibss algorithm here
        return String.valueOf(new Date().getTime()).substring(0, 10);
    }
}
