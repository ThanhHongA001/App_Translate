package com.example.app_tt2.ui.history;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.app_tt2.R;

public class HistoryActivity extends AppCompatActivity {

    private ImageButton btnBack;
    private ImageButton btnClearAll;
    private TextView tvEmpty;
    private RecyclerView recyclerView;

    private HistoryViewModel viewModel;
    private HistoryAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_history);

        initViews();
        initRecyclerView();
        initViewModel();
        initEvents();
        observeData();

        viewModel.loadHistory();
    }

    private void initViews() {
        btnBack = findViewById(R.id.history_BtnBack);
        btnClearAll = findViewById(R.id.history_BtnClearAll);
        tvEmpty = findViewById(R.id.history_TvEmpty);
        recyclerView = findViewById(R.id.history_RclList);
    }

    private void initRecyclerView() {
        adapter = new HistoryAdapter(history -> {
            Intent intent = new Intent(
                    HistoryActivity.this,
                    HistoryDetailActivity.class
            );

            intent.putExtra(HistoryDetailActivity.EXTRA_HISTORY_ID, history.getId());
            startActivity(intent);
        });

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }

    private void initViewModel() {
        viewModel = new ViewModelProvider(this).get(HistoryViewModel.class);
    }

    private void initEvents() {
        btnBack.setOnClickListener(v -> finish());

        btnClearAll.setOnClickListener(v -> {
            new AlertDialog.Builder(this)
                    .setTitle("Xóa toàn bộ lịch sử")
                    .setMessage("Bạn có chắc muốn xóa toàn bộ lịch sử dịch không?")
                    .setNegativeButton("Hủy", null)
                    .setPositiveButton("Xóa", (dialog, which) -> {
                        viewModel.clearAllHistory();
                    })
                    .show();
        });
    }

    private void observeData() {
        viewModel.getUiState().observe(this, state -> {
            if (state == null) return;

            if (state.hasError()) {
                Toast.makeText(
                        this,
                        state.getErrorMessage(),
                        Toast.LENGTH_SHORT
                ).show();
                return;
            }

            adapter.submitList(state.getHistories());

            boolean empty = state.isEmpty();
            tvEmpty.setVisibility(empty ? View.VISIBLE : View.GONE);
            recyclerView.setVisibility(empty ? View.GONE : View.VISIBLE);
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (viewModel != null) {
            viewModel.loadHistory();
        }
    }
}