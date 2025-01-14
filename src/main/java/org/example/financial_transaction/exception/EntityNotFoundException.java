package org.example.financial_transaction.exception;

public class EntityNotFoundException extends RuntimeException {

    public EntityNotFoundException(String nationalCode) {
        super("Account with NationalCode " + nationalCode + " does not exist");
    }
}
