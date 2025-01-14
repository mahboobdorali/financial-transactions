package org.example.financial_transaction.exception;

public class AccountNotFoundException extends RuntimeException {
    public AccountNotFoundException(String accountNumber) {
        super("Account with accountNumber " + accountNumber + " does not exist");
    }
}
