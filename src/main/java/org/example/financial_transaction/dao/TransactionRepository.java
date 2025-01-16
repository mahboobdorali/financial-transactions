package org.example.financial_transaction.dao;

import org.example.financial_transaction.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

public interface TransactionRepository extends JpaRepository<Transaction, Integer>,
        JpaSpecificationExecutor<Transaction> {

    Optional<Transaction> findByTrackingCode(Long trackingCOde);
}
