package org.example.financial_transaction.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import org.example.financial_transaction.model.enumutation.CustomerType;

import java.time.LocalDate;

public record CustomerRequest(
        @NotBlank @Pattern(regexp = "^[A-Za-z]{3,29}$", message = "Name must be between 3 and 29 letters") String name,
        @NotBlank @Pattern(regexp = "^\\d{10}$", message = "National code must be exactly 10 digits") String nationalCode,
        LocalDate establishmentDate,
        CustomerType customerType,
        @NotBlank @Pattern(regexp = "^\\+?[0-9]{10,15}$", message = "Phone number must be between 10 to 15 digits, optionally prefixed with '+'") String phoneNumber,
        @NotBlank @Pattern(regexp = "^.+$", message = "Address cannot be empty") String address,
        @NotBlank @Pattern(regexp = "^\\d{5}(-\\d{4})?$", message = "Postal code must be 5 digits, optionally followed by a hyphen and 4 digits") String postalCode) {
}
