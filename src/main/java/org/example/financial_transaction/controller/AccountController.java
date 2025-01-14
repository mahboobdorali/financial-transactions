package org.example.financial_transaction.controller;

import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.example.financial_transaction.model.dto.CustomerSummary;
import org.example.financial_transaction.service.IAccountService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/account")
@RequiredArgsConstructor
public class AccountController {
    private final IAccountService accountService;

    @GetMapping("/find-account-number/{nationalCode}")
    public JsonMessage<String> findAccountNumber(@PathVariable @NotBlank String nationalCode) {
        return new JsonMessage<>(accountService.findAccountNumberByNationalCode(nationalCode));
    }

    @GetMapping("/get-balance/{accountNumber}")
    public JsonMessage<String> getBalance(@PathVariable String accountNumber) {
        return new JsonMessage<>("balance is:" + accountService.findBalanceByAccountNumber(accountNumber));
    }

    @GetMapping("/get-summary-customer/{accountNumber}")
    public CustomerSummary getSummaryCustomer(@PathVariable String accountNumber) {
        return accountService.getByAccountNumber(accountNumber);
    }
}
