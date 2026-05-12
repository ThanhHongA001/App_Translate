package com.example.app_tt2.ui.evaluation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.app_tt2.R;
import com.example.app_tt2.domain.model.TranslationEvaluation;
import com.example.app_tt2.utils.TimeUtils;

import java.util.ArrayList;
import java.util.List;

public class EvaluationResultAdapter
        extends RecyclerView.Adapter<EvaluationResultAdapter.EvaluationViewHolder> {

    private final List<TranslationEvaluation> evaluations = new ArrayList<>();

    public void submitList(List<TranslationEvaluation> newList) {
        evaluations.clear();

        if (newList != null) {
            evaluations.addAll(newList);
        }

        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public EvaluationViewHolder onCreateViewHolder(
            @NonNull ViewGroup parent,
            int viewType
    ) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_evaluation_result, parent, false);

        return new EvaluationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(
            @NonNull EvaluationViewHolder holder,
            int position
    ) {
        TranslationEvaluation item = evaluations.get(position);

        holder.tvComment.setText(item.getComment());
        holder.tvCreatedAt.setText(TimeUtils.formatTime(item.getCreatedAt()));
    }

    @Override
    public int getItemCount() {
        return evaluations.size();
    }

    static class EvaluationViewHolder extends RecyclerView.ViewHolder {

        TextView tvComment;
        TextView tvCreatedAt;

        public EvaluationViewHolder(@NonNull View itemView) {
            super(itemView);

            tvComment = itemView.findViewById(
                    R.id.item_evaluation_result_TvEvaluationComment
            );

            tvCreatedAt = itemView.findViewById(
                    R.id.item_evaluation_result_TvEvaluationCreatedAt
            );
        }
    }
}