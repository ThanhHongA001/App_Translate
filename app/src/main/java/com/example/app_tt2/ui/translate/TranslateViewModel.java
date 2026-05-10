package com.example.app_tt2.ui.translate;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.app_tt2.data.repository.TranslateRepository;
import com.example.app_tt2.data.repository.TranslateRepositoryImpl;
import com.example.app_tt2.domain.model.TranslationResult;
import com.example.app_tt2.domain.usecase.TranslateTextUseCase;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TranslateViewModel extends ViewModel {

    private final MutableLiveData<TranslateUiState> uiState =
            new MutableLiveData<>(TranslateUiState.idle());

    private final TranslateTextUseCase translateTextUseCase;
    private final ExecutorService executorService = Executors.newSingleThreadExecutor();

    public TranslateViewModel() {
        TranslateRepository repository = new TranslateRepositoryImpl();
        this.translateTextUseCase = new TranslateTextUseCase(repository);
    }

    public LiveData<TranslateUiState> getUiState() {
        return uiState;
    }

    public void translate(
            String text,
            LanguageOption sourceLanguage,
            LanguageOption targetLanguage
    ) {
        uiState.setValue(TranslateUiState.loading());

        executorService.execute(() -> {
            try {
                TranslationResult result = translateTextUseCase.execute(
                        text,
                        sourceLanguage.getCode(),
                        targetLanguage.getCode()
                );

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