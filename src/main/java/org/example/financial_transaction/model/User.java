package org.example.financial_transaction.model;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@MappedSuperclass
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class User extends BaseEntity {

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "national_code", nullable = false, unique = true)
    private String nationalCode;

    @NotNull
    @Column(name = "establishment_date")
    private LocalDate establishmentDate;

    @NotBlank
    @Column(name = "phone_number", nullable = false)
    private String phoneNumber;

    @NotBlank
    @Column(name = "address", nullable = false)
    private String address;

    @NotBlank
    @Column(name = "postal_code", nullable = false)
    private String postalCode;
}
