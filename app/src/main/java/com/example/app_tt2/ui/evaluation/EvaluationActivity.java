package com.example.app_tt2.ui.evaluation;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.app_tt2.R;
import com.example.app_tt2.utils.Constants;

public class EvaluationActivity extends AppCompatActivity {

    private TextView tvSourceText;
    private TextView tvTranslatedText;
    private RecyclerView recyclerView;

    private EvaluationViewModel viewModel;
    private EvaluationResultAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_evaluation);

        initViews();
        initRecyclerView();
        initViewModel();
        receiveDataAndEvaluate();
    }

    private void initViews() {
        tvSourceText = findViewById(R.id.evaluation_TvSourceText);
        tvTranslatedText = findViewById(R.id.evaluation_TvTranslatedText);
        recyclerView = findViewById(R.id.evaluation_RvResults);
    }

    private void initRecyclerView() {
        adapter = new EvaluationResultAdapter();

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }

    private void initViewModel() {
        viewModel = new ViewModelProvider(this).get(EvaluationViewModel.class);

        viewModel.getEvaluationsLiveData().observe(this, evaluations -> {
            adapter.submitList(evaluations);
        });
    }

    private void receiveDataAndEvaluate() {
        String sourceText = getIntent().getStringExtra(Constants.EXTRA_SOURCE_TEXT);
        String translatedText = getIntent().getStringExtra(Constants.EXTRA_TRANSLATED_TEXT);
        long durationMillis = getIntent().getLongExtra(
                Constants.EXTRA_TRANSLATION_DURATION,
                0L
        );

        if (sourceText == null) sourceText = "";
        if (translatedText == null) translatedText = "";

        tvSourceText.setText(sourceText);
        tvTranslatedText.setText(translatedText);

        viewModel.evaluate(sourceText, translatedText, durationMillis);
    }
}