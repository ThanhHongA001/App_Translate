package com.example.app_tt2.ui.evaluation;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.app_tt2.domain.model.TranslationEvaluation;
import com.example.app_tt2.domain.usecase.EvaluateTranslationUseCase;

import java.util.ArrayList;
import java.util.List;

public class EvaluationViewModel extends ViewModel {

    private final EvaluateTranslationUseCase evaluateTranslationUseCase;

    private final MutableLiveData<List<TranslationEvaluation>> evaluationsLiveData =
            new MutableLiveData<>(new ArrayList<>());

    public EvaluationViewModel() {
        this.evaluateTranslationUseCase = new EvaluateTranslationUseCase();
    }

    public LiveData<List<TranslationEvaluation>> getEvaluationsLiveData() {
        return evaluationsLiveData;
    }

    public void evaluate(
            String sourceText,
            String translatedText,
            long durationMillis
    ) {
        TranslationEvaluation evaluation =
                evaluateTranslationUseCase.execute(sourceText, translatedText, durationMillis);

        List<TranslationEvaluation> currentList = evaluationsLiveData.getValue();
        if (currentList == null) {
            currentList = new ArrayList<>();
        }

        List<TranslationEvaluation> newList = new ArrayList<>(currentList);
        newList.add(0, evaluation);

        evaluationsLiveData.setValue(newList);
    }
}