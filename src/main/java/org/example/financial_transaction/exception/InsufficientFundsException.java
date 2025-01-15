package org.example.financial_transaction.exception;

public class InsufficientFundsException extends RuntimeException{
    public InsufficientFundsException() {
        super("Insufficient funds for withdrawal");
    }
}
