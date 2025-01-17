package org.example.financial_transaction.validation.AccountType;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.Arrays;
import java.util.List;

public class AccountTypeValidator implements ConstraintValidator<AccountTypeValid, String> {

    private static final List<String> VALID_COLUMN_SEARCH = Arrays.asList("ACTIVE","INACTIVE","BLOCKED");
    @Override
    public boolean isValid(String name, ConstraintValidatorContext context) {
        return name == null || VALID_COLUMN_SEARCH.contains(name.toLowerCase());
    }
}