package com.example.app_tt2.domain.usecase;

import com.example.app_tt2.data.repository.HistoryRepository;

public class DeleteHistoryUseCase {

    private final HistoryRepository repository;

    public DeleteHistoryUseCase(HistoryRepository repository) {
        this.repository = repository;
    }

    public void execute(int id) {
        repository.deleteById(id);
    }
}