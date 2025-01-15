package org.example.financial_transaction.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.financial_transaction.dao.AccountRepository;
import org.example.financial_transaction.exception.AccountNotFoundException;
import org.example.financial_transaction.exception.EntityNotFoundException;
import org.example.financial_transaction.model.Account;
import org.example.financial_transaction.model.History;
import org.example.financial_transaction.model.dto.AccountUpdateRequest;
import org.example.financial_transaction.model.dto.CustomerSummary;
import org.example.financial_transaction.model.enumutation.AccountType;
import org.example.financial_transaction.service.IAccountService;
import org.example.financial_transaction.service.IHistoryService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;


@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements IAccountService {

    private final AccountRepository repository;
    private final IHistoryService iHistoryService;

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
    @Transactional
    public void update(AccountUpdateRequest accountUpdateRequest) {
        Account prevAccount = findById(accountUpdateRequest.id());
        StringBuilder description = new StringBuilder();
        Optional.ofNullable(accountUpdateRequest.accountType()).ifPresent(newType -> {
            if (!newType.equals(prevAccount.getAccountType())) {
                description.append("account type: ").append(newType);
                prevAccount.setAccountType(newType);
            }
        });
        if (!description.isEmpty()) {
            description.append(" successfully updated");
            saveHistory(String.valueOf(description), prevAccount);
        }
        repository.save(prevAccount);
    }

    @Override
    public Account findByAccountNumber(String accountNumber) {
        return repository.findByAccountNumber(accountNumber).orElseThrow(() -> new AccountNotFoundException(accountNumber));
    }

    @Override
    public void pureSave(Account account) {
        repository.save(account);
    }

    public void saveHistory(String description, Account account) {
        String username = "username1";
        History history = new History(username, LocalDateTime.now(), description, account, null);
        iHistoryService.save(history);
    }

    private String generateUniqueAccountNumber() {
        return String.format("%014d", repository.generateAccountNumber());
    }
}
