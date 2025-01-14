package org.example.financial_transaction.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.financial_transaction.dao.AccountRepository.IAccountRepository;
import org.example.financial_transaction.model.Account;
import org.example.financial_transaction.model.enumutation.AccountType;
import org.example.financial_transaction.service.IAccountService;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

@Service
//@RequiredArgsConstructor
public class AccountServiceImpl implements IAccountService {

    private final IAccountRepository repository;

    private final JdbcTemplate jdbcTemplate;

    public AccountServiceImpl(IAccountRepository repository, JdbcTemplate jdbcTemplate) {
        this.repository = repository;
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Account createAccount() {
        Account account = new Account();
        String s = generateUniqueAccountNumber();
        account.setAccountNumber(s);
        account.setAccountType(AccountType.ACTIVE);
        account.setBalance(0D);
        return repository.save(account);
    }

    @Override
    public String findAccountNumberByNationalCode(String nationalCode) {
        return repository.findAccountNumberByNationalCode(nationalCode);
    }

    @Override
    public Double findBalanceByAccountNumber(String accountNumber) {
        return repository.findBalanceByAccountNumber(accountNumber);
    }

    private String generateUniqueAccountNumber() {
        return String.format("%014d", repository.generateAccountNumber());
    }
}
