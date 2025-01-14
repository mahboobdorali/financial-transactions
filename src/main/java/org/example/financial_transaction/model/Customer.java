package org.example.financial_transaction.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.financial_transaction.model.enumutation.CustomerType;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "customers")
public class Customer extends User {

    @Enumerated(value = EnumType.ORDINAL)
    @Column(name = "customer_type", nullable = false)
    private CustomerType customerType;

    @OneToOne(mappedBy = "customer")
    private Account account;
}
