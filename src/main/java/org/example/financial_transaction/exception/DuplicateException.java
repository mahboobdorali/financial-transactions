package org.example.financial_transaction.exception;

public class DuplicateException extends RuntimeException {
    public DuplicateException(String nationalCode) {
        super("بیش از این مشتری با کدملی" + nationalCode + "در سامانه ثبت شده است");
    }
}
