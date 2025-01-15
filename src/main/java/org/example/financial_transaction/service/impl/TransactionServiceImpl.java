package org.example.financial_transaction.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.financial_transaction.dao.TransactionRepository;
import org.example.financial_transaction.exception.InvalidAmountException;
import org.example.financial_transaction.model.dto.DepositRequest;
import org.example.financial_transaction.exception.DisabledAccountException;
import org.example.financial_transaction.exception.DuplicateAccountException;
import org.example.financial_transaction.exception.InsufficientFundsException;
import org.example.financial_transaction.model.Account;
import org.example.financial_transaction.model.Transaction;
import org.example.financial_transaction.model.TransferFeeConfig;
import org.example.financial_transaction.model.dto.TransferRequest;
import org.example.financial_transaction.model.dto.WithdrawRequest;
import org.example.financial_transaction.model.enumutation.AccountType;
import org.example.financial_transaction.model.enumutation.TransactionType;
import org.example.financial_transaction.service.IAccountService;
import org.example.financial_transaction.service.ITransactionService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class TransactionServiceImpl implements ITransactionService {
    private final TransactionRepository repository;
    private final IAccountService iAccountService;
    private final TransferFeeConfig transferFeeConfig;

    @Override
    @Transactional
    public long transferAmount(TransferRequest transferRequest) {
        Transaction transaction = new Transaction();
        if (transferRequest.amount() < 0)
            throw new InvalidAmountException();
        if (Objects.equals(transferRequest.sourceAccountNumber(), transferRequest.destinationAccountNumber()))
            throw new DuplicateAccountException();
        Account source = iAccountService.findByAccountNumber(transferRequest.sourceAccountNumber());
        Account destination = iAccountService.findByAccountNumber(transferRequest.destinationAccountNumber());
        checkAccountStatus(source);
        checkAccountStatus(destination);
        transferAmount(source, destination, transferRequest.amount());
        transaction.setSourceAccount(source);
        transaction.setDestinationAccount(destination);
        long trackingCode = generateTrackingCode();
        transaction.setTrackingCode(trackingCode);
        transaction.setAmount(transferRequest.amount());
        transaction.setTransactionType(TransactionType.TRANSFER);
        repository.save(transaction);
        return trackingCode;
    }

    @Override
    @Transactional
    public long depositAmount(DepositRequest depositRequest) {
        if (depositRequest.amount() < 0)
            throw new InvalidAmountException();
        Transaction transaction = new Transaction();
        Account destination = iAccountService.findByAccountNumber(depositRequest.destinationAccountNumber());
        checkAccountStatus(destination);
        increaseBalance(destination, depositRequest.amount());
        transaction.setDestinationAccount(destination);
        transaction.setTransactionType(TransactionType.DEPOSIT);
        long trackingCode = generateTrackingCode();
        transaction.setTrackingCode(trackingCode);
        transaction.setAmount(depositRequest.amount());
        repository.save(transaction);
        return trackingCode;
    }

    @Override
    @Transactional
    public long withdrawAmount(WithdrawRequest withdrawRequest) {
        if (withdrawRequest.amount() < 0)
            throw new InvalidAmountException();
        Transaction transaction = new Transaction();
        Account source = iAccountService.findByAccountNumber(withdrawRequest.sourceAccountNumber());
        checkAccountStatus(source);
        processTransfer(source, withdrawRequest.amount());
        transaction.setSourceAccount(source);
        transaction.setTransactionType(TransactionType.WITHDRAW);
        long trackingCode = generateTrackingCode();
        transaction.setTrackingCode(trackingCode);
        transaction.setAmount(withdrawRequest.amount());
        repository.save(transaction);
        return trackingCode;
    }

    private long generateTrackingCode() {
        Random random = new Random();
        return random.nextLong(100000000);

    }

    private void checkAccountStatus(Account account) {
        boolean accountType = account.getAccountType().equals(AccountType.ACTIVE);
        if (!accountType)
            throw new DisabledAccountException(account.getAccountNumber());
    }

    private void increaseBalance(Account account, Double amount) {
        Double balance = account.getBalance();
        balance += amount;
        account.setBalance(balance);
        iAccountService.pureSave(account);
    }

    private void processTransfer(Account account, Double amount) {
        double transferFee = calculateTransferFee(amount);
        if (amount + transferFee > account.getBalance())
            throw new InsufficientFundsException();
        double balance = account.getBalance();
        balance -= amount + transferFee;
        if (balance < 0)
            throw new InsufficientFundsException();
        account.setBalance(balance);
        iAccountService.pureSave(account);
    }

    private void transferAmount(Account sourceAccount, Account destinationAccount, Double amount) {
        processTransfer(sourceAccount, amount);
        Double destinationBalance = destinationAccount.getBalance();
        destinationBalance += amount;
        destinationAccount.setBalance(destinationBalance);
        iAccountService.pureSave(destinationAccount);
    }

    public double calculateTransferFee(Double amount) {
        double calculatedFee = amount * (transferFeeConfig.getPercentage() / 1000);
        if (calculatedFee < transferFeeConfig.getFloor()) {
            return transferFeeConfig.getFloor();
        } else if (calculatedFee > transferFeeConfig.getCeiling()) {
            return transferFeeConfig.getCeiling();
        } else {
            return calculatedFee;
        }
    }
}
