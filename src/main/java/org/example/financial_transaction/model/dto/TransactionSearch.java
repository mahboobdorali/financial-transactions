package org.example.financial_transaction.model.dto;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.example.financial_transaction.model.Transaction;
import org.example.financial_transaction.model.enumutation.TransactionType;
import org.springframework.data.jpa.domain.Specification;

import java.util.Date;

public record TransactionSearch(TransactionType transactionType,
                                String sourceAccountNumber,
                                String destinationAccountNumber,
                                Double minAmount,
                                Double maxAmount,
                                Date startDate,
                                Date endDate) implements Specification<Transaction> {
    @Override
    public Predicate toPredicate(Root<Transaction> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
        Predicate predicate = criteriaBuilder.conjunction();
        if (transactionType != null) {
            criteriaBuilder.and(predicate, criteriaBuilder.equal(root.get("transactionType"), transactionType));
        }
        if (sourceAccountNumber != null) {
            criteriaBuilder.and(predicate, criteriaBuilder.equal(root.get("sourceAccount").get("accountNumber"), sourceAccountNumber));
        }
        if (destinationAccountNumber != null) {
            criteriaBuilder.and(predicate, criteriaBuilder.equal(root.get("destinationAccount").get("accountNumber"), destinationAccountNumber));
        }
        if (minAmount != null) {
            predicate = criteriaBuilder.greaterThanOrEqualTo(root.get("amount"), minAmount);
        }
        if (maxAmount != null) {
            predicate = criteriaBuilder.lessThanOrEqualTo(root.get("amount"), maxAmount);
        }
        if (startDate != null) {
            predicate = criteriaBuilder.greaterThanOrEqualTo(root.get("creationDate"), startDate);
        }
        if (endDate != null) {
            predicate = criteriaBuilder.lessThanOrEqualTo(root.get("creationDate"), endDate);
        }
        return predicate;
    }
}
