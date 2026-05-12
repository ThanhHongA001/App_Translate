package com.example.app_tt2.ui.translate;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.app_tt2.R;
import com.example.app_tt2.ui.evaluation.EvaluationActivity;
import com.example.app_tt2.ui.history.HistoryActivity;
import com.example.app_tt2.ui.suggestion.SuggestionPopupController;
import com.example.app_tt2.utils.Constants;
import com.example.app_tt2.utils.TextUtils;

import java.util.ArrayList;
import java.util.List;

public class TranslateActivity extends AppCompatActivity {

    private EditText edtInputText;
    private TextView tvTranslatedText;
    private TextView tvStatus;

    private Spinner spinnerSourceLanguage;
    private Spinner spinnerTargetLanguage;

    private Button btnTranslate;
    private Button btnClear;
    private Button btnCopy;

    private ImageButton btnSwapLanguage;
    private ImageButton btnMenu;
    private ImageButton btnHistory;

    private ProgressBar progressBar;

    private TranslateViewModel viewModel;
    private SuggestionPopupController suggestionPopupController;

    private final List<LanguageOption> languageOptions = new ArrayList<>();

    private long translationStartTime = 0L;
    private long lastTranslationDuration = 0L;
    private String lastSourceText = "";
    private String lastTranslatedText = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_translate);

        initViews();
        initSuggestions();
        initLanguages();
        initViewModel();
        initEvents();
        observeUiState();
    }

    private void initViews() {
        edtInputText = findViewById(R.id.translate_EdtInputText);

        tvTranslatedText = findViewById(R.id.translate_TvTranslatedText);
        tvStatus = findViewById(R.id.translate_TvResultLabel);

        spinnerSourceLanguage = findViewById(R.id.translate_TvSourceLanguage);
        spinnerTargetLanguage = findViewById(R.id.translate_TvTargetLanguage);

        btnTranslate = findViewById(R.id.translate_BtnTranslate);
        btnClear = findViewById(R.id.translate_BtnClear);
        btnCopy = findViewById(R.id.translate_BtnCopy);

        btnSwapLanguage = findViewById(R.id.translate_BtnSwapLanguage);
        btnMenu = findViewById(R.id.translate_BtnMenu);
        btnHistory = findViewById(R.id.translate_BtnHistory);

        progressBar = findViewById(R.id.translate_ProgressBar);
    }

    private void initSuggestions() {
        suggestionPopupController = new SuggestionPopupController(edtInputText);
    }

    private void initLanguages() {
        languageOptions.clear();

        languageOptions.add(LanguageOption.english());
        languageOptions.add(LanguageOption.vietnamese());

        ArrayAdapter<LanguageOption> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                languageOptions
        );

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinnerSourceLanguage.setAdapter(adapter);
        spinnerTargetLanguage.setAdapter(adapter);

        spinnerSourceLanguage.setSelection(0);
        spinnerTargetLanguage.setSelection(1);
    }

    private void initViewModel() {
        viewModel = new ViewModelProvider(this).get(TranslateViewModel.class);
    }

    private void initEvents() {
        btnTranslate.setOnClickListener(v -> translateText());

        btnSwapLanguage.setOnClickListener(v -> swapLanguageAndTranslateAgain());

        btnClear.setOnClickListener(v -> clearTranslateScreen());

        btnHistory.setOnClickListener(v -> openHistoryScreen());

        btnMenu.setOnClickListener(v -> showPopupMenu());

        btnCopy.setOnClickListener(v -> copyTranslatedText());
    }

    private void translateText() {
        String inputText = edtInputText.getText().toString().trim();

        if (TextUtils.isEmpty(inputText)) {
            Toast.makeText(
                    this,
                    "Vui lòng nhập nội dung cần dịch.",
                    Toast.LENGTH_SHORT
            ).show();
            return;
        }

        LanguageOption sourceLanguage =
                (LanguageOption) spinnerSourceLanguage.getSelectedItem();

        LanguageOption targetLanguage =
                (LanguageOption) spinnerTargetLanguage.getSelectedItem();

        if (sourceLanguage == null || targetLanguage == null) {
            Toast.makeText(
                    this,
                    "Vui lòng chọn ngôn ngữ dịch.",
                    Toast.LENGTH_SHORT
            ).show();
            return;
        }

        translationStartTime = System.currentTimeMillis();
        lastSourceText = inputText;
        lastTranslatedText = "";
        lastTranslationDuration = 0L;

        viewModel.translate(inputText, sourceLanguage, targetLanguage);
    }

    private void swapLanguageAndTranslateAgain() {
        int sourcePosition = spinnerSourceLanguage.getSelectedItemPosition();
        int targetPosition = spinnerTargetLanguage.getSelectedItemPosition();

        spinnerSourceLanguage.setSelection(targetPosition);
        spinnerTargetLanguage.setSelection(sourcePosition);

        if (!TextUtils.isEmpty(lastTranslatedText)) {
            edtInputText.setText(lastTranslatedText);
            edtInputText.setSelection(edtInputText.getText().length());

            if (suggestionPopupController != null) {
                suggestionPopupController.refresh();
            }

            tvTranslatedText.setText("");
            tvStatus.setText("Đang dịch lại...");

            translateText();
        }
    }

    private void clearTranslateScreen() {
        edtInputText.setText("");
        tvTranslatedText.setText("");
        tvStatus.setText("Đã xóa nội dung");

        translationStartTime = 0L;
        lastTranslationDuration = 0L;
        lastSourceText = "";
        lastTranslatedText = "";

        if (suggestionPopupController != null) {
            suggestionPopupController.refresh();
        }
    }

    private void copyTranslatedText() {
        String translatedText = tvTranslatedText.getText().toString().trim();

        if (TextUtils.isEmpty(translatedText)) {
            Toast.makeText(
                    this,
                    "Chưa có nội dung để sao chép.",
                    Toast.LENGTH_SHORT
            ).show();
            return;
        }

        ClipboardManager clipboardManager =
                (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);

        ClipData clipData =
                ClipData.newPlainText(
                        "translated_text",
                        translatedText
                );

        clipboardManager.setPrimaryClip(clipData);

        Toast.makeText(
                this,
                "Đã sao chép kết quả dịch.",
                Toast.LENGTH_SHORT
        ).show();
    }

    private void openHistoryScreen() {
        Intent intent = new Intent(
                TranslateActivity.this,
                HistoryActivity.class
        );

        startActivity(intent);
    }

    private void openEvaluationScreen() {
        if (TextUtils.isEmpty(lastSourceText) || TextUtils.isEmpty(lastTranslatedText)) {
            Toast.makeText(
                    this,
                    "Chưa có bản dịch để đánh giá.",
                    Toast.LENGTH_SHORT
            ).show();
            return;
        }

        Intent intent = new Intent(
                TranslateActivity.this,
                EvaluationActivity.class
        );

        intent.putExtra(Constants.EXTRA_SOURCE_TEXT, lastSourceText);
        intent.putExtra(Constants.EXTRA_TRANSLATED_TEXT, lastTranslatedText);
        intent.putExtra(Constants.EXTRA_TRANSLATION_DURATION, lastTranslationDuration);

        startActivity(intent);
    }

    private void showPopupMenu() {
        PopupMenu popupMenu = new PopupMenu(this, btnMenu);

        popupMenu.getMenu().add("Lịch sử dịch");
        popupMenu.getMenu().add("Đánh giá bản dịch");
        popupMenu.getMenu().add("Kiểm tra gợi ý từ");
        popupMenu.getMenu().add("Xóa nội dung");
        popupMenu.getMenu().add("Giới thiệu");

        popupMenu.setOnMenuItemClickListener(item -> {
            String title = item.getTitle().toString();

            if (title.equals("Lịch sử dịch")) {
                openHistoryScreen();
                return true;
            }

            if (title.equals("Đánh giá bản dịch")) {
                openEvaluationScreen();
                return true;
            }

            if (title.equals("Kiểm tra gợi ý từ")) {
                if (suggestionPopupController != null) {
                    suggestionPopupController.refresh();
                }

                Toast.makeText(
                        this,
                        "Đã kiểm tra từ nghi sai. Bấm vào từ gạch đỏ để xem gợi ý.",
                        Toast.LENGTH_SHORT
                ).show();
                return true;
            }

            if (title.equals("Xóa nội dung")) {
                clearTranslateScreen();
                return true;
            }

            if (title.equals("Giới thiệu")) {
                Toast.makeText(
                        this,
                        "Ứng dụng dịch Anh - Việt có lưu lịch sử, đánh giá bản dịch và gợi ý sửa từ nhập sai.",
                        Toast.LENGTH_SHORT
                ).show();
                return true;
            }

            return false;
        });

        popupMenu.show();
    }

    private void observeUiState() {
        viewModel.getUiState().observe(this, state -> {
            if (state == null) {
                return;
            }

            progressBar.setVisibility(
                    state.isLoading() ? View.VISIBLE : View.GONE
            );

            btnTranslate.setEnabled(!state.isLoading());
            btnClear.setEnabled(!state.isLoading());
            btnCopy.setEnabled(!state.isLoading());
            btnSwapLanguage.setEnabled(!state.isLoading());
            btnMenu.setEnabled(!state.isLoading());
            btnHistory.setEnabled(!state.isLoading());

            if (state.isLoading()) {
                tvStatus.setText("Đang dịch...");
                return;
            }

            if (state.hasError()) {
                tvStatus.setText("Lỗi");
                tvTranslatedText.setText("");

                lastTranslatedText = "";
                lastTranslationDuration = 0L;

                Toast.makeText(
                        this,
                        state.getErrorMessage(),
                        Toast.LENGTH_LONG
                ).show();

                return;
            }

            if (state.hasResult()) {
                tvStatus.setText("Dịch thành công");

                lastTranslationDuration =
                        System.currentTimeMillis() - translationStartTime;

                lastTranslatedText = state.getResult().getTranslatedText();

                tvTranslatedText.setText(lastTranslatedText);
            } else {
                tvStatus.setText("Sẵn sàng");
            }
        });
    }
}