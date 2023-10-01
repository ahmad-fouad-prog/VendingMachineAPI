package com.flapkap.vendingmachine.controller;

import com.flapkap.vendingmachine.dto.AccountDTO;
import com.flapkap.vendingmachine.exception.ErrorCode;
import com.flapkap.vendingmachine.exception.CustomException;
import com.flapkap.vendingmachine.service.AccountService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/accounts")
public class AccountController {

    @Autowired
    private AccountService accountService;

    @PostMapping("/deposit")
    public ResponseEntity<AccountDTO> depositMoneyToAccount(Authentication authentication, @RequestBody Double amount) {
        AccountDTO updatedAccount = accountService.addDepositForAuthenticatedUser(authentication.getName(), amount);
        return new ResponseEntity<>(updatedAccount, HttpStatus.OK);
    }

    @PostMapping("/buy")
    public ResponseEntity<AccountDTO> buyProduct(Authentication authentication, @RequestParam Long productId, @RequestParam Integer amount) {
        AccountDTO updatedAccount = accountService.buyProductForAuthenticatedUser(authentication.getName(), productId, amount);
        return new ResponseEntity<>(updatedAccount, HttpStatus.OK);
    }

    @PostMapping("/reset")
    public ResponseEntity<AccountDTO> resetDeposit(Authentication authentication) {
        AccountDTO updatedAccount = accountService.resetDepositForAuthenticatedUser(authentication.getName());
        return new ResponseEntity<>(updatedAccount, HttpStatus.OK);
    }
}
