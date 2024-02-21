package com.banking.app.service;

import java.util.List;

import com.banking.app.model.Account;
import com.banking.app.model.dto.ApiResponse;
import com.banking.app.model.dto.CreateAccountDto;
import com.banking.app.model.dto.UpdateAccountDto;

public interface AccountService {
    ApiResponse<Account> create(CreateAccountDto request);
    ApiResponse<Object> update(Long accountId, UpdateAccountDto request);
    ApiResponse<Account> getById(Long accountId);
    ApiResponse<List<Account>> getAll();    
    ApiResponse<Object> delete(Long accountId);
}