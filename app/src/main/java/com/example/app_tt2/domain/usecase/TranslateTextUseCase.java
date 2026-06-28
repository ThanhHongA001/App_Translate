package com.example.app_tt2.domain.usecase;

import com.example.app_tt2.data.remote.TranslationResultSelector;
import com.example.app_tt2.data.repository.TranslateRepository;
import com.example.app_tt2.data.repository.TranslateRepositoryImpl;
import com.example.app_tt2.domain.model.TranslationResult;

public class TranslateTextUseCase {

    private final TranslateRepository translateRepository;

    public TranslateTextUseCase() {
        this(new TranslateRepositoryImpl());
    }

    public TranslateTextUseCase(TranslateRepository translateRepository) {
        this.translateRepository = translateRepository;
    }

    public TranslationResult execute(String sourceText, Object sourceLanguage, Object targetLanguage) {
        String sourceCode = TranslationResultSelector.resolveLanguageCode(sourceLanguage, "en");
        String targetCode = TranslationResultSelector.resolveLanguageCode(targetLanguage, "vi");
        return translateRepository.translate(sourceText, sourceCode, targetCode);
    }

    public TranslationResult translate(String sourceText, Object sourceLanguage, Object targetLanguage) {
        return execute(sourceText, sourceLanguage, targetLanguage);
    }
}
