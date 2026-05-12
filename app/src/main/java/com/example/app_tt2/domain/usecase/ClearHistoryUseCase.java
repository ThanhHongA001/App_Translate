package com.example.app_tt2.domain.usecase;

import com.example.app_tt2.data.repository.HistoryRepository;

public class ClearHistoryUseCase {

    private final HistoryRepository repository;

    public ClearHistoryUseCase(HistoryRepository repository) {
        this.repository = repository;
    }

    public void execute() {
        repository.clearAll();
    }
}