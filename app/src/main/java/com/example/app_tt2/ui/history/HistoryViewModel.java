package com.example.app_tt2.ui.history;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.app_tt2.data.repository.HistoryRepository;
import com.example.app_tt2.data.repository.HistoryRepositoryImpl;
import com.example.app_tt2.domain.model.TranslationHistory;
import com.example.app_tt2.domain.usecase.ClearHistoryUseCase;
import com.example.app_tt2.domain.usecase.DeleteHistoryUseCase;
import com.example.app_tt2.domain.usecase.GetHistoryUseCase;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class HistoryViewModel extends AndroidViewModel {

    private final MutableLiveData<HistoryUiState> uiState =
            new MutableLiveData<>(HistoryUiState.loading());

    private final MutableLiveData<TranslationHistory> selectedHistory =
            new MutableLiveData<>();

    private final GetHistoryUseCase getHistoryUseCase;
    private final DeleteHistoryUseCase deleteHistoryUseCase;
    private final ClearHistoryUseCase clearHistoryUseCase;

    private final ExecutorService executorService = Executors.newSingleThreadExecutor();

    public HistoryViewModel(@NonNull Application application) {
        super(application);

        HistoryRepository repository =
                new HistoryRepositoryImpl(application.getApplicationContext());

        getHistoryUseCase = new GetHistoryUseCase(repository);
        deleteHistoryUseCase = new DeleteHistoryUseCase(repository);
        clearHistoryUseCase = new ClearHistoryUseCase(repository);
    }

    public LiveData<HistoryUiState> getUiState() {
        return uiState;
    }

    public LiveData<TranslationHistory> getSelectedHistory() {
        return selectedHistory;
    }

    public void loadHistory() {
        uiState.setValue(HistoryUiState.loading());

        executorService.execute(() -> {
            try {
                uiState.postValue(
                        HistoryUiState.success(getHistoryUseCase.execute())
                );
            } catch (Exception e) {
                uiState.postValue(
                        HistoryUiState.error("Không thể tải lịch sử dịch.")
                );
            }
        });
    }

    public void loadHistoryById(int id) {
        executorService.execute(() -> {
            TranslationHistory history = getHistoryUseCase.getById(id);
            selectedHistory.postValue(history);
        });
    }

    public void deleteHistory(int id) {
        executorService.execute(() -> {
            deleteHistoryUseCase.execute(id);
            loadHistory();
        });
    }

    public void clearAllHistory() {
        executorService.execute(() -> {
            clearHistoryUseCase.execute();
            loadHistory();
        });
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        executorService.shutdown();
    }
}