package org.example.financial_transaction.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.financial_transaction.dao.HistoryRepository;
import org.example.financial_transaction.model.History;
import org.example.financial_transaction.service.IHistoryService;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class HistoryServiceImpl implements IHistoryService {

    private final HistoryRepository repository;

    @Override
    public void save(History history) {
        repository.save(history);
    }
}
