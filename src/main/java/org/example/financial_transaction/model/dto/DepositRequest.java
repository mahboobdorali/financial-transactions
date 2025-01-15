package org.example.financial_transaction.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public record DepositRequest(
        @NotBlank(message = "destination account must not be null") @Pattern(regexp = "^[0-9]{14}$", message = "accountNumber must be 14 digits") String destinationAccountNumber,
        @NotNull(message = "amount must not be null") Double amount) {
}
