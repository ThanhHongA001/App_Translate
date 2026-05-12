package com.example.app_tt2.ui.translate;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.app_tt2.data.repository.HistoryRepository;
import com.example.app_tt2.data.repository.HistoryRepositoryImpl;
import com.example.app_tt2.data.repository.TranslateRepository;
import com.example.app_tt2.data.repository.TranslateRepositoryImpl;
import com.example.app_tt2.domain.model.TranslationHistory;
import com.example.app_tt2.domain.model.TranslationResult;
import com.example.app_tt2.domain.usecase.SaveTranslationUseCase;
import com.example.app_tt2.domain.usecase.TranslateTextUseCase;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TranslateViewModel extends AndroidViewModel {

    private final MutableLiveData<TranslateUiState> uiState =
            new MutableLiveData<>(TranslateUiState.idle());

    private final TranslateTextUseCase translateTextUseCase;
    private final SaveTranslationUseCase saveTranslationUseCase;

    private final ExecutorService executorService = Executors.newSingleThreadExecutor();

    public TranslateViewModel(@NonNull Application application) {
        super(application);

        TranslateRepository translateRepository = new TranslateRepositoryImpl();

        HistoryRepository historyRepository =
                new HistoryRepositoryImpl(application.getApplicationContext());

        translateTextUseCase = new TranslateTextUseCase(translateRepository);
        saveTranslationUseCase = new SaveTranslationUseCase(historyRepository);
    }

    public LiveData<TranslateUiState> getUiState() {
        return uiState;
    }

    public void translate(String text,
                          LanguageOption sourceLanguage,
                          LanguageOption targetLanguage) {
        String inputText = text == null ? "" : text.trim();

        if (inputText.isEmpty()) {
            uiState.setValue(TranslateUiState.error("Vui lòng nhập nội dung cần dịch."));
            return;
        }

        if (sourceLanguage == null || targetLanguage == null) {
            uiState.setValue(TranslateUiState.error("Vui lòng chọn ngôn ngữ dịch."));
            return;
        }

        if (sourceLanguage.getCode().equals(targetLanguage.getCode())) {
            uiState.setValue(TranslateUiState.error("Ngôn ngữ nguồn và ngôn ngữ đích không được trùng nhau."));
            return;
        }

        uiState.setValue(TranslateUiState.loading());

        executorService.execute(() -> {
            try {
                TranslationResult result = translateTextUseCase.execute(
                        inputText,
                        sourceLanguage.getCode(),
                        targetLanguage.getCode()
                );

                TranslationHistory history = new TranslationHistory(
                        0,
                        inputText,
                        result.getTranslatedText(),
                        sourceLanguage.getCode(),
                        targetLanguage.getCode(),
                        System.currentTimeMillis(),
                        false
                );

                saveTranslationUseCase.execute(history);

                uiState.postValue(TranslateUiState.success(result));
            } catch (Exception e) {
                String message = e.getMessage();

                if (message == null || message.trim().isEmpty()) {
                    message = "Đã xảy ra lỗi khi dịch văn bản.";
                }

                uiState.postValue(TranslateUiState.error(message));
            }
        });
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        executorService.shutdown();
    }
}