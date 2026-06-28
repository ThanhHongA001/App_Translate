package com.example.app_tt2.data.repository;

import com.example.app_tt2.data.local.dictionary.LocalWordDictionary;
import com.example.app_tt2.data.remote.RetrofitClient;
import com.example.app_tt2.data.remote.TranslationResultSelector;
import com.example.app_tt2.data.remote.api.TranslateApiService;
import com.example.app_tt2.data.remote.dto.TranslateResponseDto;
import com.example.app_tt2.domain.model.TranslationResult;

import java.io.IOException;

import retrofit2.Response;

public class TranslateRepositoryImpl implements TranslateRepository {

    private final TranslateApiService translateApiService;

    public TranslateRepositoryImpl() {
        this(RetrofitClient.getTranslateApiService());
    }

    public TranslateRepositoryImpl(TranslateApiService translateApiService) {
        this.translateApiService = translateApiService;
    }

    @Override
    public TranslationResult translate(String sourceText, String sourceLanguage, String targetLanguage) {
        String input = sourceText == null ? "" : sourceText.trim();

        if (input.isEmpty()) {
            throw new IllegalArgumentException("Vui lòng nhập nội dung cần dịch.");
        }

        String sourceCode = TranslationResultSelector.toMyMemoryCode(sourceLanguage);
        String targetCode = TranslationResultSelector.toMyMemoryCode(targetLanguage);

        if (sourceCode.isEmpty() || targetCode.isEmpty()) {
            throw new IllegalArgumentException("Ngôn ngữ dịch không hợp lệ.");
        }

        if (sourceCode.equals(targetCode)) {
            throw new IllegalArgumentException("Ngôn ngữ nguồn và ngôn ngữ đích không được trùng nhau.");
        }

        String localTranslation = LocalWordDictionary.translateSingleWord(input, sourceCode, targetCode);
        if (localTranslation != null && !localTranslation.trim().isEmpty()) {
            return new TranslationResult(input, localTranslation, sourceCode, targetCode);
        }

        String langPair = sourceCode + "|" + targetCode;

        try {
            Response<TranslateResponseDto> response = translateApiService
                    .translate(input, langPair, 1)
                    .execute();

            if (!response.isSuccessful()) {
                throw new RuntimeException("Không thể dịch văn bản. Mã lỗi HTTP: " + response.code());
            }

            TranslateResponseDto body = response.body();
            if (body == null) {
                throw new RuntimeException("Không nhận được dữ liệu dịch từ máy chủ.");
            }

            String translatedText = TranslationResultSelector.selectBest(
                    body,
                    input,
                    sourceCode,
                    targetCode
            );

            if (translatedText == null || translatedText.trim().isEmpty()) {
                throw new RuntimeException("Không tìm được kết quả dịch phù hợp.");
            }

            return new TranslationResult(input, translatedText, sourceCode, targetCode);
        } catch (IOException exception) {
            throw new RuntimeException("Lỗi kết nối khi dịch: " + exception.getMessage(), exception);
        }
    }
}
