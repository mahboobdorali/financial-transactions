package org.example.financial_transaction.service.impl;

import org.example.financial_transaction.dao.TransactionRepository;
import org.example.financial_transaction.exception.TransactionNotFoundException;
import org.example.financial_transaction.model.Account;
import org.example.financial_transaction.model.Transaction;
import org.example.financial_transaction.model.TransferFeeConfig;
import org.example.financial_transaction.model.dto.*;
import org.example.financial_transaction.model.enumutation.AccountType;
import org.example.financial_transaction.model.enumutation.TransactionStatus;
import org.example.financial_transaction.model.enumutation.TransactionType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Arrays;
import java.util.Date;
import java.util.Objects;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TransactionServiceImplTest {
    @InjectMocks
    private TransactionServiceImpl underTest;

    @Mock
    private TransactionRepository repository;

    @Mock
    private AccountServiceImpl accountService;

    @Mock
    private TransferFeeConfig transferFeeConfig;

    private Account destinationAccount;
    private Account sourceAccount;
    private Account bankAccount;
    private static final String DESTINATION_ACCOUNT_NUMBER = "78787878454545";
    private static final String SOURCE_ACCOUNT_NUMBER = "12124545124512";
    private static final String BANK_ACCOUNT_NUMBER = "11111111111111";

    @BeforeEach
    public void setUp() {
        destinationAccount = new Account(DESTINATION_ACCOUNT_NUMBER, new Date(), AccountType.ACTIVE, 0D, null, null);
        sourceAccount = new Account(SOURCE_ACCOUNT_NUMBER, new Date(), AccountType.ACTIVE, 80000000D, null, null);
        bankAccount = new Account(BANK_ACCOUNT_NUMBER, new Date(), AccountType.ACTIVE, 0D, null, null);
    }

    @Test
    public void shouldDepositAmount() {
        when(accountService.findByAccountNumber(anyString()))
                .thenReturn(destinationAccount);
        long trackingCode = underTest.depositAmount(new DepositRequest(DESTINATION_ACCOUNT_NUMBER, 12000D));
        assertNotNull(trackingCode);
        verify(repository, times(1)).save(any(Transaction.class));
    }

    @Test
    public void shouldWithdrawAmount() {
        when(accountService.findByAccountNumber(SOURCE_ACCOUNT_NUMBER))
                .thenReturn(sourceAccount);
        when(accountService.findByAccountNumber(BANK_ACCOUNT_NUMBER))
                .thenReturn(bankAccount);
        long result = underTest.withdrawAmount(new WithdrawRequest(SOURCE_ACCOUNT_NUMBER, 1000D));
        verify(repository, times(1)).save(any(Transaction.class));
        assertNotNull(result);
    }

    @Test
    public void shouldTransferAmount() {
        when(accountService.findByAccountNumber(SOURCE_ACCOUNT_NUMBER))
                .thenReturn(sourceAccount);

        when(accountService.findByAccountNumber(DESTINATION_ACCOUNT_NUMBER))
                .thenReturn(destinationAccount);
        when(accountService.findByAccountNumber(BANK_ACCOUNT_NUMBER))
                .thenReturn(bankAccount);
        long result = underTest.transferAmount(new TransferRequest(SOURCE_ACCOUNT_NUMBER, DESTINATION_ACCOUNT_NUMBER, 1500D));
        verify(repository, times(1)).save(any(Transaction.class));
        assertNotNull(result);
    }

    @Test
    public void shouldThrowExceptionWhenNotFoundByTrackingCode() {
        Long trackingCode = 67890L;
        when(repository.findByTrackingCode(trackingCode)).thenReturn(Optional.empty());
        TransactionNotFoundException exception = assertThrows(TransactionNotFoundException.class, () -> underTest.findByTrackingCode(trackingCode));
        assertEquals("transaction not found for trackingCode: " + trackingCode, exception.getMessage());
    }

    @Test
    public void shouldFindByTrackingCode() {
        Long trackingCode = 12345L;
        when(repository.findByTrackingCode(trackingCode)).thenReturn(Optional.of(new Transaction(sourceAccount, destinationAccount, 1234D, TransactionType.TRANSFER, new Date(), trackingCode, TransactionStatus.CONFIRM)));
        Transaction result = underTest.findByTrackingCode(trackingCode);
        assertNotNull(result);
        assertEquals(trackingCode, result.getTrackingCode());
        assertThat(result)
                .hasFieldOrPropertyWithValue("sourceAccount", result.getSourceAccount())
                .hasFieldOrPropertyWithValue("destinationAccount", result.getDestinationAccount())
                .hasFieldOrPropertyWithValue("amount", result.getAmount())
                .hasFieldOrPropertyWithValue("trackingCode", result.getTrackingCode());
    }

    @Test
    public void shouldFilter() {
        TransactionSearch transactionSearch = new TransactionSearch(null, null, null,100.00D, null, null,null
        );
        Pageable pageable = PageRequest.of(0, 10);
        Transaction transaction1 = new Transaction();
        transaction1.setId(1);
        transaction1.setAmount(100.00);
        transaction1.setTransactionType(TransactionType.DEPOSIT);
        transaction1.setCreationDate(new Date());
        transaction1.setTrackingCode(12345L);
        Transaction transaction2 = new Transaction();
        transaction2.setId(2);
        transaction2.setAmount(200.00);
        transaction2.setTransactionType(TransactionType.WITHDRAW);
        transaction2.setCreationDate(new Date());
        transaction2.setTrackingCode(67890L);
        Page<Transaction> page = new PageImpl<>(Arrays.asList(transaction1, transaction2), pageable, 2);
        when(repository.findAll(transactionSearch, pageable)).thenReturn(page);
        Page<TransactionSearchResponse> result = underTest.getFilteredTransactions(transactionSearch, pageable);
        assertNotNull(result);
        assertEquals(2, result.getTotalElements());
        assertEquals(10, result.getSize());
        assertEquals(0, result.getNumber());
        assertTrue(result.getContent().stream().allMatch(response ->
                Objects.equals(response.id(), transaction1.getId()) || Objects.equals(response.id(), transaction2.getId())
        ));
    }

}