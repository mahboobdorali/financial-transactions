package org.example.financial_transaction.service.impl;

import org.example.financial_transaction.dao.AccountRepository;
import org.example.financial_transaction.exception.AccountNotFoundException;
import org.example.financial_transaction.exception.EntityNotFoundException;
import org.example.financial_transaction.model.Account;
import org.example.financial_transaction.model.Admin;
import org.example.financial_transaction.model.dto.AccountUpdateRequest;
import org.example.financial_transaction.model.dto.CustomerSummary;
import org.example.financial_transaction.model.enumutation.AccountType;
import org.example.financial_transaction.model.enumutation.CustomerType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.time.LocalDate;
import java.util.Date;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
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
        newAccount = new AccountUpdateRequest(2, AccountType.BLOCKED.name());
    }

    @Test
    void shouldRegisterAccount() {
        when(repository.save(any(Account.class))).thenReturn(account);
        Account actual = underTest.createAccount();
        assertNotNull(actual);
        assertThat(actual)
                .hasFieldOrPropertyWithValue("balance", actual.getBalance())
                .hasFieldOrPropertyWithValue("accountNumber", actual.getAccountNumber())
                .hasFieldOrPropertyWithValue("accountType", actual.getAccountType());
        verify(repository, times(1)).save(any(Account.class));
    }

    @Test
    void shouldUpdated() {
        mockSecurityContext();
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

    private void mockSecurityContext() {
        Admin principal = mock(Admin.class);
        when(principal.getUsername()).thenReturn("adminUser");
        Authentication authentication = mock(Authentication.class);
        when(authentication.getPrincipal()).thenReturn(principal);
        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
    }

    @Test
    public void shouldFindAccountNumberByNationalCode() {
        when(repository.existsByNationalCode(anyString())).thenReturn(true);
        when(repository.findAccountNumberByNationalCode(anyString())).thenReturn(account.getAccountNumber());
        String accountNumber = underTest.findAccountNumberByNationalCode("1234567897");
        assertEquals(account.getAccountNumber(), accountNumber);
        verify(repository, times(1)).findAccountNumberByNationalCode(anyString());
    }

    @Test
    public void shouldGetBalanceByAccountNumber() {
        when(repository.existByAccountNumber(anyString())).thenReturn(true);
        when(repository.findBalanceByAccountNumber(anyString())).thenReturn(account.getBalance());
        Double balance = underTest.findBalanceByAccountNumber("1234567897");
        assertEquals(balance, account.getBalance());
        verify(repository, times(1)).findBalanceByAccountNumber(anyString());
    }

    @Test
    public void shouldThrowExceptionWhenCantFindByAccountNumber() {
        when(repository.getByAccountNumber(anyString())).thenReturn(Optional.empty());
        AccountNotFoundException exception = assertThrows(AccountNotFoundException.class, () -> underTest.getByAccountNumber(account.getAccountNumber()));
        assertEquals("Account with accountNumber 14785236978914 does not exist", exception.getMessage());
        verify(repository, times(1)).getByAccountNumber(account.getAccountNumber());
    }

    @Test
    public void shouldReturnCustomerDetailsByAccountNumber() {
        CustomerSummary summary = new CustomerSummary("name", "1478542101", LocalDate.now(), CustomerType.REAL, "09978452100", "address", "41526320", AccountType.ACTIVE, "20202020202002");
        when(repository.getByAccountNumber(anyString())).thenReturn(Optional.of(summary));
        CustomerSummary actual = underTest.getByAccountNumber("20202020202002");
        assertNotNull(actual);
        assertThat(actual)
                .hasFieldOrPropertyWithValue("name", summary.name())
                .hasFieldOrPropertyWithValue("nationalCode", summary.nationalCode())
                .hasFieldOrPropertyWithValue("customerType", summary.customerType())
                .hasFieldOrPropertyWithValue("phoneNumber", summary.phoneNumber())
                .hasFieldOrPropertyWithValue("address", summary.address())
                .hasFieldOrPropertyWithValue("postalCode", summary.postalCode())
                .hasFieldOrPropertyWithValue("accountNumber", summary.accountNumber())
                .hasFieldOrPropertyWithValue("accountType", summary.accountType());
    }

    @Test
    public void shouldFindAccountByAccountNumber() {
        when(repository.findByAccountNumber(anyString())).thenReturn(Optional.ofNullable(account));
        Account actual = underTest.findByAccountNumber(account.getAccountNumber());
        assertNotNull(actual);
        assertThat(actual)
                .hasFieldOrPropertyWithValue("balance", actual.getBalance())
                .hasFieldOrPropertyWithValue("accountNumber", actual.getAccountNumber())
                .hasFieldOrPropertyWithValue("accountType", actual.getAccountType());
    }

    @Test
    public void shouldThrowExceptionWhenCantFindAccountByAccountNumber() {
        when(repository.findByAccountNumber(anyString())).thenReturn(Optional.empty());
        AccountNotFoundException exception = assertThrows(AccountNotFoundException.class, () -> underTest.findByAccountNumber(account.getAccountNumber()));
        assertEquals("Account with accountNumber 14785236978914 does not exist", exception.getMessage());
    }
}