package com.banking.app.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.banking.app.model.Account.AccountType;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class CreateAccountDto {
    private String accountName;
    private String bvn;
    @Enumerated(EnumType.STRING)
    private AccountType accountType;
}
