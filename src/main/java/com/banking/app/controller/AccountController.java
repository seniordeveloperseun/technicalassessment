package com.banking.app.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.banking.app.model.dto.CreateAccountDto;
import com.banking.app.model.dto.UpdateAccountDto;
import com.banking.app.service.AccountService;

import jakarta.websocket.server.PathParam;

@RestController
@RequestMapping("api/v1/account")
public class AccountController {
    
    private final AccountService accountService;

    @Autowired
    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @PostMapping
    public ResponseEntity<Object> createAccount(@RequestBody CreateAccountDto accountDto) {
        try {
            var resp = accountService.create(accountDto);
            return new ResponseEntity<>(resp, HttpStatus.OK);

        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping
    public ResponseEntity<Object> getAllAccounts() {
        try {
            var resp = accountService.getAll();
            return new ResponseEntity<>(resp, HttpStatus.OK);

        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping(path = "/:id")
    public ResponseEntity<Object> getById(@PathParam(value = "id") Long accountId) {
        try {
            var resp = accountService.getById(accountId);
            return new ResponseEntity<>(resp, HttpStatus.OK);

        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PatchMapping(path = "/:id")
    public ResponseEntity<Object> update(@PathParam(value = "id") Long accountId, @RequestBody UpdateAccountDto accountDto) {
        try {
            var resp = accountService.update(accountId, accountDto);
            return new ResponseEntity<>(resp, HttpStatus.OK);

        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping(path = "/:id")
    public ResponseEntity<Object> delete(@PathParam(value = "id") Long accountId) {
        try {
            var resp = accountService.delete(accountId);
            return new ResponseEntity<>(resp, HttpStatus.OK);

        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}
