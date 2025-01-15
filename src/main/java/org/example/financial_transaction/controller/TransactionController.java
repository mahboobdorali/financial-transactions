package org.example.financial_transaction.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.financial_transaction.model.dto.DepositRequest;
import org.example.financial_transaction.model.dto.TransferRequest;
import org.example.financial_transaction.model.dto.WithdrawRequest;
import org.example.financial_transaction.service.ITransactionService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/transaction")
@RequiredArgsConstructor
public class TransactionController {

    private final ITransactionService iTransactionService;

    @PostMapping("/deposit")
    public JsonMessage<Long> increaseBalance(@Valid @RequestBody DepositRequest depositRequest) {
        return new JsonMessage<>(iTransactionService.depositAmount(depositRequest));
    }

    @PostMapping("/withdraw")
    public JsonMessage<Long> withdrawBalance(@Valid @RequestBody WithdrawRequest withdrawRequest) {
        return new JsonMessage<>(iTransactionService.withdrawAmount(withdrawRequest));
    }

    @PostMapping("/transfer")
    public JsonMessage<Long> transferBalance(@Valid @RequestBody TransferRequest transferRequest) {
        return new JsonMessage<>(iTransactionService.transferAmount(transferRequest));
    }
}
