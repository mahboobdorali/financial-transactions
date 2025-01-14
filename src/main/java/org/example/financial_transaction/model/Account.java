package org.example.financial_transaction.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.financial_transaction.model.enumutation.AccountType;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "accounts")
public class Account extends BaseEntity {

    @NotBlank
    @Column(name = "account_number", nullable = false, length = 14)
    private String accountNumber;

    @NotNull
    @CreationTimestamp
    @Column(name = "creation_date", nullable = false, updatable = false)
    private LocalDateTime creationDate;

    @NotNull
    @Enumerated(value = EnumType.ORDINAL)
    @Column(name = "account_type", nullable = false)
    private AccountType accountType;

    @NotNull
    @Column(name = "balance", nullable = false)
    private Double balance;

    @NotNull
    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;

}
