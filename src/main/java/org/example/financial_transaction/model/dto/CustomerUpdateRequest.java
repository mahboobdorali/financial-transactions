package org.example.financial_transaction.model.dto;

import org.example.financial_transaction.model.enumutation.AccountType;
import org.example.financial_transaction.model.enumutation.CustomerType;

import java.time.LocalDate;

public record CustomerUpdateRequest(Integer accountId,
                                    Integer customerId,
                                    String name,
                                    String nationalCode,
                                    LocalDate establishmentDate,
                                    CustomerType customerType,
                                    String phoneNumber,
                                    String address,
                                    String postalCode,
                                    AccountType accountType) {
}
