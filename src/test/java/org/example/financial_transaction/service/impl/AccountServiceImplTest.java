package org.example.financial_transaction.service.impl;

import org.example.financial_transaction.dao.AccountRepository;
import org.example.financial_transaction.exception.AccountNotFoundException;
import org.example.financial_transaction.exception.EntityNotFoundException;
import org.example.financial_transaction.model.Account;
import org.example.financial_transaction.model.dto.AccountUpdateRequest;
import org.example.financial_transaction.model.enumutation.AccountType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Date;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AccountServiceImplTest {

    @InjectMocks
    private AccountServiceImpl underTest;

    @Mock
    private AccountRepository repository;

    @Mock
    HistoryServiceImpl historyService;

    private Account account;
    private AccountUpdateRequest newAccount;

    @BeforeEach
    void setUp() {
        account = new Account("14785236978914", new Date(), AccountType.ACTIVE, 0D, null, null);
        newAccount = new AccountUpdateRequest(2, AccountType.BLOCKED);
    }

    @Test
    void shouldRegisterAccount() {
//        when(repository.save(any(Account.class))).thenReturn(account);
//        AccountServiceImpl spyAccountService = spy(underTest);
//        doReturn("14785236978914").when(spyAccountService).generateUniqueAccountNumber();
//        spyAccountService.createAccount();
//        assertNotNull(account);
//        verify(repository,times(1)).save(any(Account.class));
    }

    @Test
    void shouldUpdated() {
        when(repository.findById(anyInt())).thenReturn(Optional.of(account));
        underTest.update(newAccount);
        assertEquals(account.getAccountType(), AccountType.BLOCKED);
        verify(repository, times(1)).save(any(Account.class));
    }

    @Test
    void shouldThrowExceptionWhenAccountNotFoundByNationalCode() {
        String nationalCode = "4610735131";
        when(repository.existsByNationalCode(nationalCode)).thenReturn(false);
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> underTest.findAccountNumberByNationalCode(nationalCode));
        assertEquals("Account with NationalCode 4610735131 does not exist", exception.getMessage());
        verify(repository, never()).findAccountNumberByNationalCode(anyString());
    }

    @Test
    void shouldThrowExceptionWhenAccountNotFoundByAccountNumber() {
        String accountNumber = "4610735131";
        when(repository.existByAccountNumber(accountNumber)).thenReturn(false);
        AccountNotFoundException exception = assertThrows(AccountNotFoundException.class, () -> underTest.findBalanceByAccountNumber(accountNumber));
        assertEquals("Account with accountNumber 4610735131 does not exist", exception.getMessage());
        verify(repository, never()).findBalanceByAccountNumber(accountNumber);
    }


}