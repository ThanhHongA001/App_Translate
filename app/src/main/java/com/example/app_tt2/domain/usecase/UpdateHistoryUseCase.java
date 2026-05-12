package com.example.app_tt2.domain.usecase;

import com.example.app_tt2.data.repository.HistoryRepository;
import com.example.app_tt2.domain.model.TranslationHistory;

public class UpdateHistoryUseCase {

    private final HistoryRepository repository;

    public UpdateHistoryUseCase(HistoryRepository repository) {
        this.repository = repository;
    }

    public void execute(TranslationHistory history) {
        repository.update(history);
    }
}