package com.example.app_tt2.domain.usecase;

import com.example.app_tt2.domain.evaluator.TranslationEvaluator;
import com.example.app_tt2.domain.model.TranslationEvaluation;

public class EvaluateTranslationUseCase {

    private final TranslationEvaluator evaluator;

    public EvaluateTranslationUseCase() {
        this.evaluator = new TranslationEvaluator();
    }

    public TranslationEvaluation execute(
            String sourceText,
            String translatedText,
            long durationMillis
    ) {
        return evaluator.evaluate(sourceText, translatedText, durationMillis);
    }
}