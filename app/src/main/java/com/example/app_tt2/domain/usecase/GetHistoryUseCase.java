package com.example.app_tt2.domain.usecase;

import com.example.app_tt2.data.repository.HistoryRepository;
import com.example.app_tt2.domain.model.TranslationHistory;

import java.util.List;

public class GetHistoryUseCase {

    private final HistoryRepository repository;

    public GetHistoryUseCase(HistoryRepository repository) {
        this.repository = repository;
    }

    public List<TranslationHistory> execute() {
        return repository.getAll();
    }

    public TranslationHistory getById(int id) {
        return repository.getById(id);
    }
}