package org.example.financial_transaction.exception;

public class TransactionNotFoundException extends RuntimeException {
    public TransactionNotFoundException(Long trackingCode) {
        super("transaction not found for trackingCode: " + trackingCode);
    }
}
