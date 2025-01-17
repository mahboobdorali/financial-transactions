package org.example.financial_transaction.model.dto;

import org.example.financial_transaction.validation.AccountType.AccountTypeValid;

public record AccountUpdateRequest(Integer id,
                                   @AccountTypeValid String accountType) {
}
