package com.example.app_tt2.data.repository;

import com.example.app_tt2.domain.model.TranslationResult;

public interface TranslateRepository {

    TranslationResult translateText(
            String text,
            String sourceLang,
            String targetLang
    ) throws Exception;
}