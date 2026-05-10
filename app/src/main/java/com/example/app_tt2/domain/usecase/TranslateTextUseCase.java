package com.example.app_tt2.domain.usecase;

import com.example.app_tt2.data.repository.TranslateRepository;
import com.example.app_tt2.domain.model.TranslationResult;

public class TranslateTextUseCase {

    private final TranslateRepository repository;

    public TranslateTextUseCase(TranslateRepository repository) {
        this.repository = repository;
    }

    public TranslationResult execute(
            String text,
            String sourceLang,
            String targetLang
    ) throws Exception {

        if (text == null || text.trim().isEmpty()) {
            throw new IllegalArgumentException("Vui lòng nhập văn bản cần dịch.");
        }

        if (sourceLang == null || sourceLang.trim().isEmpty()) {
            throw new IllegalArgumentException("Ngôn ngữ nguồn không hợp lệ.");
        }

        if (targetLang == null || targetLang.trim().isEmpty()) {
            throw new IllegalArgumentException("Ngôn ngữ đích không hợp lệ.");
        }

        if (sourceLang.equals(targetLang)) {
            throw new IllegalArgumentException("Ngôn ngữ nguồn và ngôn ngữ đích không được trùng nhau.");
        }

        return repository.translateText(text.trim(), sourceLang, targetLang);
    }
}