package org.example.financial_transaction.service.impl;

import org.example.financial_transaction.dao.CustomerRepository;
import org.example.financial_transaction.exception.DuplicateException;
import org.example.financial_transaction.model.Account;
import org.example.financial_transaction.model.Customer;
import org.example.financial_transaction.model.dto.CustomerUpdateRequest;
import org.example.financial_transaction.model.enumutation.AccountType;
import org.example.financial_transaction.model.enumutation.CustomerType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Date;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CustomerServiceImplTest {
    @InjectMocks
    private CustomerServiceImpl underTest;

    @Mock
    private CustomerRepository repository;
    @Mock
    private AccountServiceImpl accountService;
    @Mock
    private HistoryServiceImpl historyService;

    private Account account;
    private Customer customer;
    private CustomerUpdateRequest newCustomer;

    @BeforeEach
    void setup() {
        account = new Account("12345678901234", new Date(), AccountType.ACTIVE, 10D, null, null);
        customer = new Customer("mahboob", "4610735131", LocalDate.MIN, "09919823955", "tehran street1", "8558214", CustomerType.REAL, null);
        newCustomer = new CustomerUpdateRequest(1, "mahi", "4610735171", LocalDate.now(), CustomerType.LEGAL, "02125478544", "isfahan", "45632101");
    }

    @Test
    void shouldRegisterCustomer() {
        when(repository.findByNationalCode(anyString())).thenReturn(Optional.empty());
        when(accountService.createAccount()).thenReturn(account);
        underTest.registerCustomer(customer);
        verify(repository, times(1)).findByNationalCode(anyString());
        assertEquals(account, customer.getAccount());
    }

    @Test
    void shouldThrowExceptionWhenCustomerExist() {
        when(repository.findByNationalCode(anyString())).thenReturn(Optional.of(customer));
        DuplicateException exception = assertThrows(DuplicateException.class, () -> underTest.registerCustomer(customer));
        assertEquals("More than this customers with national code 4610735131 are registered in the system", exception.getMessage());
        verify(repository, never()).save(any(Customer.class));
    }

    @Test
    void shouldReturnTruWhenAnotherCustomerExist() {
        Integer id = 2;
        when(repository.findDuplicateByNationalCodeAndId(anyString(), anyInt())).thenReturn(true);
        Boolean result = underTest.findDuplicateByNationalCodeAndId(customer.getNationalCode(), id);
        assertTrue(result);
        verify(repository, times(1)).findDuplicateByNationalCodeAndId(customer.getNationalCode(), id);
    }

    @Test
    void shouldUpdateCustomer() {
        when(underTest.findDuplicateByNationalCodeAndId(anyString(), anyInt())).thenReturn(false);
        when(repository.findById(anyInt())).thenReturn(Optional.of(customer));
        underTest.update(newCustomer);
        assertEquals(customer.getName(), "mahi");
        assertEquals(customer.getNationalCode(), "4610735171");
        assertEquals(customer.getPhoneNumber(), "02125478544");
        assertEquals(customer.getAddress(), "isfahan");
        assertEquals(customer.getCustomerType(), CustomerType.LEGAL);
    }

    @Test
    void shouldThrowExceptionWhenFindOtherCustomerByUpdatedNationalCode() {
        when(repository.findDuplicateByNationalCodeAndId(anyString(), anyInt())).thenReturn(true);
        DuplicateException exception = assertThrows(DuplicateException.class, () -> underTest.update(newCustomer));
        assertEquals("More than this customers with national code 4610735171 are registered in the system", exception.getMessage());
    }
}