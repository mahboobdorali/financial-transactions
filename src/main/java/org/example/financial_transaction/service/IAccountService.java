package org.example.financial_transaction.service;

import org.example.financial_transaction.model.Account;
import org.example.financial_transaction.model.dto.CustomerSummary;

public interface IAccountService {
    Account createAccount();

    String findAccountNumberByNationalCode(String nationalCode);

    Double findBalanceByAccountNumber(String accountNumber);

    Boolean existByNationalCode(String nationalCode);

    Boolean existsByAccountNumber(String accountNumber);

    CustomerSummary getByAccountNumber(String accountNumber);

    Account findById(Integer id);

    Account pureSave(Account account);
}
