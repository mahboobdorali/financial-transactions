package org.example.financial_transaction.exception;

public class InvalidAmountException extends RuntimeException {
    public InvalidAmountException() {
        super("amount should not be negative");
    }
}
