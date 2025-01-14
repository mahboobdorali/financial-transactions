package org.example.financial_transaction.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.example.financial_transaction.model.enumutation.AccountType;
import org.hibernate.annotations.CreationTimestamp;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "accounts")
@ToString
public class Account extends BaseEntity {

    @NotBlank
    @Column(name = "account_number", nullable = false, length = 14)
    private String accountNumber;

    @CreationTimestamp
    @Temporal(value = TemporalType.TIMESTAMP)
    @Column(name = "creation_date")
    private Date creationDate;

    @NotNull
    @Enumerated(value = EnumType.ORDINAL)
    @Column(name = "account_type", nullable = false)
    private AccountType accountType;

    @NotNull
    @Column(name = "balance", nullable = false)
    private Double balance;

    @OneToOne(fetch = FetchType.LAZY, mappedBy = "account")
    private Customer customer;

}
