package com.example.app_tt2.data.repository;

import com.example.app_tt2.domain.model.TranslationResult;

public interface TranslateRepository {

    TranslationResult translate(String sourceText, String sourceLanguage, String targetLanguage);
}
