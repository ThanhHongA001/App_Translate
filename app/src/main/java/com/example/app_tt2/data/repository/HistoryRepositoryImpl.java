package com.example.app_tt2.data.repository;

import android.content.Context;

import com.example.app_tt2.data.local.AppDatabase;
import com.example.app_tt2.data.local.dao.TranslationHistoryDao;
import com.example.app_tt2.data.local.entity.TranslationHistoryEntity;
import com.example.app_tt2.domain.model.TranslationHistory;

import java.util.ArrayList;
import java.util.List;

public class HistoryRepositoryImpl implements HistoryRepository {

    private final TranslationHistoryDao dao;

    public HistoryRepositoryImpl(Context context) {
        dao = AppDatabase.getInstance(context).translationHistoryDao();
    }

    @Override
    public void save(TranslationHistory history) {
        dao.insert(toEntity(history));
    }

    @Override
    public List<TranslationHistory> getAll() {
        List<TranslationHistoryEntity> entities = dao.getAll();
        List<TranslationHistory> histories = new ArrayList<>();

        for (TranslationHistoryEntity entity : entities) {
            histories.add(toModel(entity));
        }

        return histories;
    }

    @Override
    public TranslationHistory getById(int id) {
        TranslationHistoryEntity entity = dao.getById(id);
        if (entity == null) return null;
        return toModel(entity);
    }

    @Override
    public void deleteById(int id) {
        dao.deleteById(id);
    }

    @Override
    public void clearAll() {
        dao.clearAll();
    }

    @Override
    public void update(TranslationHistory history) {
        dao.update(toEntity(history));
    }

    private TranslationHistoryEntity toEntity(TranslationHistory history) {
        TranslationHistoryEntity entity = new TranslationHistoryEntity(
                history.getSourceText(),
                history.getTranslatedText(),
                history.getSourceLang(),
                history.getTargetLang(),
                history.getCreatedAt(),
                history.isFavorite()
        );

        entity.setId(history.getId());
        return entity;
    }

    private TranslationHistory toModel(TranslationHistoryEntity entity) {
        return new TranslationHistory(
                entity.getId(),
                entity.getSourceText(),
                entity.getTranslatedText(),
                entity.getSourceLang(),
                entity.getTargetLang(),
                entity.getCreatedAt(),
                entity.isFavorite()
        );
    }
}