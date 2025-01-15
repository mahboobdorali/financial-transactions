package org.example.financial_transaction.dao;

import org.example.financial_transaction.model.History;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HistoryRepository extends JpaRepository<History, Integer> {
}
