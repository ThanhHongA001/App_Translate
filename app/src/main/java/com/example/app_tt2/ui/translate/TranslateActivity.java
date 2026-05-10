package com.example.app_tt2.ui.translate;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.app_tt2.R;

import java.util.ArrayList;
import java.util.List;

public class TranslateActivity extends AppCompatActivity {

    private EditText edtInputText;
    private TextView tvTranslatedText,tvStatus;
    private Spinner spinnerSourceLanguage,spinnerTargetLanguage;
    private Button btnTranslate;
    private ImageButton btnSwapLanguage;
    private ProgressBar progressBar;
    private TranslateViewModel viewModel;

    private final List<LanguageOption> languageOptions = new ArrayList<>();

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
        btnSwapLanguage = findViewById(R.id.translate_BtnSwapLanguage);
        progressBar = findViewById(R.id.translate_ProgressBar);
    }

    private void initLanguages() {
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

        btnSwapLanguage.setOnClickListener(v -> {
            int sourcePosition = spinnerSourceLanguage.getSelectedItemPosition();
            int targetPosition = spinnerTargetLanguage.getSelectedItemPosition();

            spinnerSourceLanguage.setSelection(targetPosition);
            spinnerTargetLanguage.setSelection(sourcePosition);
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

    private void observeUiState() {
        viewModel.getUiState().observe(this, state -> {
            if (state == null) {
                return;
            }

            progressBar.setVisibility(state.isLoading() ? View.VISIBLE : View.GONE);
            btnTranslate.setEnabled(!state.isLoading());

            if (state.isLoading()) {
                tvStatus.setText("Đang dịch...");
                return;
            }

            if (state.hasError()) {
                tvStatus.setText("Lỗi");
                tvTranslatedText.setText("");
                Toast.makeText(this, state.getErrorMessage(), Toast.LENGTH_LONG).show();
                return;
            }

            if (state.hasResult()) {
                tvStatus.setText("Dịch thành công");
                tvTranslatedText.setText(state.getResult().getTranslatedText());
            } else {
                tvStatus.setText("Sẵn sàng");
            }
        });
    }
}