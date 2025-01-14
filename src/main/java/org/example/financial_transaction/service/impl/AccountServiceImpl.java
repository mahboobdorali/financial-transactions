package org.example.financial_transaction.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.financial_transaction.dao.AccountRepository.IAccountRepository;
import org.example.financial_transaction.exception.AccountNotFoundException;
import org.example.financial_transaction.exception.EntityNotFoundException;
import org.example.financial_transaction.model.Account;
import org.example.financial_transaction.model.dto.CustomerSummary;
import org.example.financial_transaction.model.enumutation.AccountType;
import org.example.financial_transaction.service.IAccountService;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements IAccountService {

    private final IAccountRepository repository;

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
        if (!existByNationalCode(nationalCode))
            throw new EntityNotFoundException("nationalCode" + nationalCode);
        return repository.findAccountNumberByNationalCode(nationalCode);
    }

    @Override
    public Double findBalanceByAccountNumber(String accountNumber) {
        if (!existsByAccountNumber(accountNumber))
            throw new AccountNotFoundException(accountNumber);
        return repository.findBalanceByAccountNumber(accountNumber);
    }

    @Override
    public Boolean existByNationalCode(String nationalCode) {
        return repository.existsByNationalCode(nationalCode);
    }

    @Override
    public Boolean existsByAccountNumber(String accountNumber) {
        return repository.existByAccountNumber(accountNumber);
    }

    @Override
    public CustomerSummary getByAccountNumber(String accountNumber) {
        Optional<CustomerSummary> byAccountNumber = repository.getByAccountNumber(accountNumber);
        if (byAccountNumber.isEmpty())
            throw new AccountNotFoundException(accountNumber);
        return byAccountNumber.get();
    }

    @Override
    public Account findById(Integer id) {
        return repository.findById(id).orElseThrow();
    }

    @Override
    public Account pureSave(Account account) {
        return repository.save(account);
    }

    private String generateUniqueAccountNumber() {
        return String.format("%014d", repository.generateAccountNumber());
    }
}
