package org.example.financial_transaction.model.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import org.example.financial_transaction.model.enumutation.AccountType;
import org.example.financial_transaction.model.enumutation.CustomerType;

import java.time.LocalDate;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CustomerSummary {
    String name;
    String nationalCode;
    LocalDate establishmentDate;
    CustomerType customerType;
    String phoneNumber;
    String address;
    String postalCode;
    AccountType accountType;
    String accountNumber;
    //todo:last updated


    public CustomerSummary(String name, String nationalCode, LocalDate establishmentDate, CustomerType customerType, String phoneNumber, String address, String postalCode, AccountType accountType, String accountNumber) {
        this.name = name;
        this.nationalCode = nationalCode;
        this.establishmentDate = establishmentDate;
        this.customerType = customerType;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.postalCode = postalCode;
        this.accountType = accountType;
        this.accountNumber = accountNumber;
    }
}
