package org.example.financial_transaction.exception;

public class IdNotFoundException extends RuntimeException {
    public IdNotFoundException(Integer id) {
        super("entity with id " + id + " not found");
    }
}
