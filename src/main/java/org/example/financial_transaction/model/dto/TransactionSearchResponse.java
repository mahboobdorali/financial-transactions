package org.example.financial_transaction.model.dto;

import org.example.financial_transaction.model.enumutation.TransactionType;

import java.util.Date;

public record TransactionSearchResponse(Integer id,
                                        Double amount,
                                        Date creationDate,
                                        Long trackingCode,
                                        TransactionType transactionType) {

}
