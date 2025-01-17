package org.example.financial_transaction.model.dto;

import java.util.Date;

public record TransactionSearchResponse(Integer id,
                                        Double amount,
                                        Date creationDate,
                                        Long trackingCode) {

}
