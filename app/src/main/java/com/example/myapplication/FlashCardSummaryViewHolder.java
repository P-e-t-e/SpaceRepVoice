package com.example.myapplication;

import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

public class FlashCardSummaryViewHolder extends RecyclerView.ViewHolder {
    private final TextView summaryTopic;
    private final TextView summaryQuestion;
    private final TextView cardId;
    private final TextView cardAnswer;


    public FlashCardSummaryViewHolder(View view) {
        super(view);
        this.summaryTopic = view.findViewById(R.id.card_summary_topic);
        this.summaryQuestion = view.findViewById(R.id.card_summary_question);
        this.cardId = view.findViewById(R.id.card_id);
        this.cardAnswer = view.findViewById(R.id.card_answer);
    }

    public void setSummaryTopicText(String string)
    {
        summaryTopic.setText(string);
    }

    public void setSummaryQuestionText(String string)
    {
        summaryQuestion.setText(string);
    }

    public void setCardId(String string)
    {
        cardId.setText(string);
    }

    public void setCardAnswer(String string)
    {
        cardAnswer.setText(string);
    }
}
