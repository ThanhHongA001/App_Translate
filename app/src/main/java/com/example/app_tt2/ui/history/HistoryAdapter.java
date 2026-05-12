package com.example.app_tt2.ui.history;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.app_tt2.R;
import com.example.app_tt2.domain.model.TranslationHistory;
import com.example.app_tt2.utils.TimeUtils;

import java.util.ArrayList;
import java.util.List;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.HistoryViewHolder> {

    public interface OnHistoryClickListener {
        void onHistoryClick(TranslationHistory history);
    }

    private final List<TranslationHistory> histories = new ArrayList<>();
    private final OnHistoryClickListener listener;

    public HistoryAdapter(OnHistoryClickListener listener) {
        this.listener = listener;
    }

    public void submitList(List<TranslationHistory> newHistories) {
        histories.clear();

        if (newHistories != null) {
            histories.addAll(newHistories);
        }

        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public HistoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_history, parent, false);

        return new HistoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HistoryViewHolder holder, int position) {
        TranslationHistory history = histories.get(position);

        holder.tvSourceText.setText(history.getSourceText());
        holder.tvTranslatedText.setText(history.getTranslatedText());
        holder.tvCreatedAt.setText(
                history.getSourceLang()
                        + " → "
                        + history.getTargetLang()
                        + " | "
                        + TimeUtils.formatTime(history.getCreatedAt())
        );

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onHistoryClick(history);
            }
        });
    }

    @Override
    public int getItemCount() {
        return histories.size();
    }

    static class HistoryViewHolder extends RecyclerView.ViewHolder {

        TextView tvSourceText;
        TextView tvTranslatedText;
        TextView tvCreatedAt;

        public HistoryViewHolder(@NonNull View itemView) {
            super(itemView);

            tvSourceText = itemView.findViewById(R.id.item_history_TvHistorySourceText);
            tvTranslatedText = itemView.findViewById(R.id.item_history_TvText);
            tvCreatedAt = itemView.findViewById(R.id.item_history_TvHistoryCreatedAt);
        }
    }
}