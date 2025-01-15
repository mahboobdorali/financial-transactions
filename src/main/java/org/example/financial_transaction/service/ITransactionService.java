package org.example.financial_transaction.service;

import org.example.financial_transaction.model.dto.DepositRequest;
import org.example.financial_transaction.model.dto.TransferRequest;
import org.example.financial_transaction.model.dto.WithdrawRequest;

public interface ITransactionService {
    long transferAmount(TransferRequest transferRequest);

    long depositAmount(DepositRequest depositRequest);

    long withdrawAmount(WithdrawRequest depositRequest);
}
