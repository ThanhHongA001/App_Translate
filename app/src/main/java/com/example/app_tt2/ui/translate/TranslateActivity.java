package com.example.app_tt2.ui.translate;

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
import com.example.app_tt2.ui.history.HistoryActivity;

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

    private final List<LanguageOption> languageOptions = new ArrayList<>();

    private String lastTranslatedText = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_translate);

        initViews();
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

        btnCopy.setOnClickListener(v -> {
            String translatedText = tvTranslatedText.getText().toString().trim();

            if (translatedText.isEmpty()) {
                Toast.makeText(
                        this,
                        "Chưa có nội dung để sao chép.",
                        Toast.LENGTH_SHORT
                ).show();
                return;
            }

            android.content.ClipboardManager clipboardManager =
                    (android.content.ClipboardManager) getSystemService(CLIPBOARD_SERVICE);

            android.content.ClipData clipData =
                    android.content.ClipData.newPlainText(
                            "translated_text",
                            translatedText
                    );

            clipboardManager.setPrimaryClip(clipData);

            Toast.makeText(
                    this,
                    "Đã sao chép kết quả dịch.",
                    Toast.LENGTH_SHORT
            ).show();
        });
    }

    private void translateText() {
        String inputText = edtInputText.getText().toString();

        LanguageOption sourceLanguage =
                (LanguageOption) spinnerSourceLanguage.getSelectedItem();

        LanguageOption targetLanguage =
                (LanguageOption) spinnerTargetLanguage.getSelectedItem();

        viewModel.translate(inputText, sourceLanguage, targetLanguage);
    }

    private void swapLanguageAndTranslateAgain() {
        int sourcePosition = spinnerSourceLanguage.getSelectedItemPosition();
        int targetPosition = spinnerTargetLanguage.getSelectedItemPosition();

        spinnerSourceLanguage.setSelection(targetPosition);
        spinnerTargetLanguage.setSelection(sourcePosition);

        if (lastTranslatedText != null && !lastTranslatedText.trim().isEmpty()) {
            edtInputText.setText(lastTranslatedText);
            edtInputText.setSelection(edtInputText.getText().length());

            tvTranslatedText.setText("");
            tvStatus.setText("Đang dịch lại...");

            translateText();
        }
    }

    private void clearTranslateScreen() {
        edtInputText.setText("");
        tvTranslatedText.setText("");
        tvStatus.setText("Đã xóa nội dung");
        lastTranslatedText = "";
    }

    private void openHistoryScreen() {
        Intent intent = new Intent(
                TranslateActivity.this,
                HistoryActivity.class
        );

        startActivity(intent);
    }

    private void showPopupMenu() {
        PopupMenu popupMenu = new PopupMenu(this, btnMenu);

        popupMenu.getMenu().add("Lịch sử dịch");
        popupMenu.getMenu().add("Xóa nội dung");
        popupMenu.getMenu().add("Giới thiệu");

        popupMenu.setOnMenuItemClickListener(item -> {
            String title = item.getTitle().toString();

            if (title.equals("Lịch sử dịch")) {
                openHistoryScreen();
                return true;
            }

            if (title.equals("Xóa nội dung")) {
                clearTranslateScreen();
                return true;
            }

            if (title.equals("Giới thiệu")) {
                Toast.makeText(
                        this,
                        "Ứng dụng dịch Anh - Việt có lưu lịch sử dịch.",
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

                Toast.makeText(
                        this,
                        state.getErrorMessage(),
                        Toast.LENGTH_LONG
                ).show();

                return;
            }

            if (state.hasResult()) {
                tvStatus.setText("Dịch thành công");

                lastTranslatedText = state.getResult().getTranslatedText();

                tvTranslatedText.setText(lastTranslatedText);
            } else {
                tvStatus.setText("Sẵn sàng");
            }
        });
    }
}