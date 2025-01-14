package org.example.financial_transaction.service;

import org.example.financial_transaction.model.Account;

public interface IAccountService {
    Account createAccount();

    String findAccountNumberByNationalCode(String nationalCode);

    Double findBalanceByAccountNumber(String accountNumber);
}
