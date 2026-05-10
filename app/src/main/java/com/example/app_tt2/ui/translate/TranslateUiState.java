package com.example.app_tt2.ui.translate;

import com.example.app_tt2.domain.model.TranslationResult;

public class TranslateUiState {

    private final boolean loading;
    private final TranslationResult result;
    private final String errorMessage;

    private TranslateUiState(
            boolean loading,
            TranslationResult result,
            String errorMessage
    ) {
        this.loading = loading;
        this.result = result;
        this.errorMessage = errorMessage;
    }

    public static TranslateUiState idle() {
        return new TranslateUiState(false, null, null);
    }

    public static TranslateUiState loading() {
        return new TranslateUiState(true, null, null);
    }

    public static TranslateUiState success(TranslationResult result) {
        return new TranslateUiState(false, result, null);
    }

    public static TranslateUiState error(String message) {
        return new TranslateUiState(false, null, message);
    }

    public boolean isLoading() {
        return loading;
    }

    public TranslationResult getResult() {
        return result;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public boolean hasError() {
        return errorMessage != null && !errorMessage.trim().isEmpty();
    }

    public boolean hasResult() {
        return result != null;
    }
}