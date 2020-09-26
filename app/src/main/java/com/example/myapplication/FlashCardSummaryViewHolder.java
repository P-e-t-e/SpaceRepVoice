package com.example.myapplication;

import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

public class FlashCardSummaryViewHolder extends RecyclerView.ViewHolder {
    private final TextView summaryTopic;
    private final TextView summaryQuestion;


    public FlashCardSummaryViewHolder(View view) {
        super(view);
        this.summaryTopic = view.findViewById(R.id.card_summary_topic);
        this.summaryQuestion = view.findViewById(R.id.card_summary_question);
    }

    public TextView getSummaryTopic() {
        return summaryTopic;
    }

    public TextView getSummaryQuestion() {
        return summaryQuestion;
    }
}
