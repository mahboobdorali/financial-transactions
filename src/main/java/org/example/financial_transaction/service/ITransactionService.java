package org.example.financial_transaction.service;

import org.example.financial_transaction.model.Transaction;
import org.example.financial_transaction.model.dto.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ITransactionService {
    long transferAmount(TransferRequest transferRequest);

    long depositAmount(DepositRequest depositRequest);

    long withdrawAmount(WithdrawRequest depositRequest);

    StatusInquiryResponse getStatusInquiry(Long trackingCode);

    Transaction findByTrackingCode(Long trackingCode);

    Page<TransactionSearchResponse> getFilteredTransactions(TransactionSearch transactionSearch,
                                              Pageable pageable);
}
