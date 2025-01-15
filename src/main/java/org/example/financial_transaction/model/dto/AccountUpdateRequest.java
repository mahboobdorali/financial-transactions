package org.example.financial_transaction.model.dto;

import org.example.financial_transaction.model.enumutation.AccountType;

public record AccountUpdateRequest(Integer id,
                                   AccountType accountType) {
}
