package org.example.financial_transaction.model;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@MappedSuperclass
@Getter
@Setter
// @AllArgsConstructor
@NoArgsConstructor
public class User extends BaseEntity {

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "national_code", nullable = false, unique = true)
    private String nationalCode;


    @Column(name = "establishment_date")
    private LocalDate establishmentDate;


    @Column(name = "phone_number", nullable = false)
    private String phoneNumber;


    @Column(name = "address", nullable = false)
    private String address;


    @Column(name = "postal_code", nullable = false)
    private String postalCode;


    public User(String name, String nationalCode, LocalDate establishmentDate, String phoneNumber, String address, String postalCode) {
        this.name = name;
        this.nationalCode = nationalCode;
        this.establishmentDate = establishmentDate;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.postalCode = postalCode;
    }
}
