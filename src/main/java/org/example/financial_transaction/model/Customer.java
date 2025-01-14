package org.example.financial_transaction.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.example.financial_transaction.model.enumutation.CustomerType;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "customers")
@ToString
public class Customer extends User {

    @Enumerated(value = EnumType.ORDINAL)
    @Column(name = "customer_type", nullable = false)
    private CustomerType customerType;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id")
    private Account account;

    public Customer(String name, String nationalCode, LocalDate establishmentDate, String phoneNumber, String address, String postalCode, CustomerType customerType, Account account) {
        super(name, nationalCode, establishmentDate, phoneNumber, address, postalCode);
        this.customerType = customerType;
        this.account = account;
    }

    public Customer(String name, String nationalCode, LocalDate establishmentDate, String phoneNumber, String address, String postalCode) {
        super(name, nationalCode, establishmentDate, phoneNumber, address, postalCode);
    }

}
