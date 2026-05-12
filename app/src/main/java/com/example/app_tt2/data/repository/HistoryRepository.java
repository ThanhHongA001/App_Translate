package com.example.app_tt2.data.repository;

import com.example.app_tt2.domain.model.TranslationHistory;

import java.util.List;

public interface HistoryRepository {

    void save(TranslationHistory history);

    List<TranslationHistory> getAll();

    TranslationHistory getById(int id);

    void deleteById(int id);

    void clearAll();

    void update(TranslationHistory history);
}