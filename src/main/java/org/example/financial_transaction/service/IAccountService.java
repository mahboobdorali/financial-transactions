package org.example.financial_transaction.service;

import org.example.financial_transaction.model.Account;
import org.example.financial_transaction.model.dto.AccountUpdateRequest;
import org.example.financial_transaction.model.dto.CustomerSummary;

import java.util.Optional;

public interface IAccountService {
    Account createAccount();

    String findAccountNumberByNationalCode(String nationalCode);

    Double findBalanceByAccountNumber(String accountNumber);

    Boolean existByNationalCode(String nationalCode);

    Boolean existsByAccountNumber(String accountNumber);

    CustomerSummary getByAccountNumber(String accountNumber);

    Account findById(Integer id);

    void update(AccountUpdateRequest accountUpdateRequest);

    Account findByAccountNumber(String accountNumber);

    void pureSave(Account account);
}
