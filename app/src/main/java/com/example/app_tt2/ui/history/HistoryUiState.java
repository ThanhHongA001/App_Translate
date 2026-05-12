package com.example.app_tt2.ui.history;

import com.example.app_tt2.domain.model.TranslationHistory;

import java.util.ArrayList;
import java.util.List;

public class HistoryUiState {

    private final boolean loading;
    private final List<TranslationHistory> histories;
    private final String errorMessage;

    private HistoryUiState(boolean loading,
                           List<TranslationHistory> histories,
                           String errorMessage) {
        this.loading = loading;
        this.histories = histories;
        this.errorMessage = errorMessage;
    }

    public static HistoryUiState loading() {
        return new HistoryUiState(true, new ArrayList<>(), null);
    }

    public static HistoryUiState success(List<TranslationHistory> histories) {
        return new HistoryUiState(false, histories, null);
    }

    public static HistoryUiState error(String message) {
        return new HistoryUiState(false, new ArrayList<>(), message);
    }

    public boolean isLoading() { return loading; }

    public List<TranslationHistory> getHistories() {
        return histories;
    }

    public String getErrorMessage() { return errorMessage; }

    public boolean hasError() {
        return errorMessage != null && !errorMessage.trim().isEmpty();
    }

    public boolean isEmpty() {
        return histories == null || histories.isEmpty();
    }
}