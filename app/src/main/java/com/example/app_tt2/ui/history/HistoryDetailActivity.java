package com.example.app_tt2.ui.history;

import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.app_tt2.R;
import com.example.app_tt2.domain.model.TranslationHistory;
import com.example.app_tt2.utils.TimeUtils;

public class HistoryDetailActivity extends AppCompatActivity {

    public static final String EXTRA_HISTORY_ID = "extra_history_id";

    private ImageButton btnBack;
    private ImageButton btnDelete;

    private TextView tvSourceText;
    private TextView tvTranslatedText;
    private TextView tvCreatedAt;

    private HistoryViewModel viewModel;

    private int historyId = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_history_detail);

        historyId = getIntent().getIntExtra(EXTRA_HISTORY_ID, -1);

        if (historyId == -1) {
            Toast.makeText(this, "Không tìm thấy lịch sử dịch.", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        initViews();
        initViewModel();
        initEvents();
        observeData();

        viewModel.loadHistoryById(historyId);
    }

    private void initViews() {
        btnBack = findViewById(R.id.history_detail_BtnBack);
        btnDelete = findViewById(R.id.history_detail_BtnDelete);

        tvSourceText = findViewById(R.id.history_detail_TvSourceText);
        tvTranslatedText = findViewById(R.id.history_detail_TvTranslatedText);
        tvCreatedAt = findViewById(R.id.history_detail_TvCreatedAt);
    }

    private void initViewModel() {
        viewModel = new ViewModelProvider(this).get(HistoryViewModel.class);
    }

    private void initEvents() {
        btnBack.setOnClickListener(v -> finish());

        btnDelete.setOnClickListener(v -> {
            new AlertDialog.Builder(this)
                    .setTitle("Xóa lịch sử")
                    .setMessage("Bạn có chắc muốn xóa bản dịch này không?")
                    .setNegativeButton("Hủy", null)
                    .setPositiveButton("Xóa", (dialog, which) -> {
                        viewModel.deleteHistory(historyId);
                        Toast.makeText(this, "Đã xóa lịch sử dịch.", Toast.LENGTH_SHORT).show();
                        finish();
                    })
                    .show();
        });
    }

    private void observeData() {
        viewModel.getSelectedHistory().observe(this, history -> {
            if (history == null) {
                Toast.makeText(this, "Không tìm thấy dữ liệu.", Toast.LENGTH_SHORT).show();
                finish();
                return;
            }

            showHistory(history);
        });
    }

    private void showHistory(TranslationHistory history) {
        tvSourceText.setText(history.getSourceText());
        tvTranslatedText.setText(history.getTranslatedText());

        tvCreatedAt.setText(
                "Ngôn ngữ: "
                        + history.getSourceLang()
                        + " → "
                        + history.getTargetLang()
                        + "\nThời gian: "
                        + TimeUtils.formatTime(history.getCreatedAt())
        );
    }
}