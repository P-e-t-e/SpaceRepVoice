package com.example.myapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.model.FlashCard;

import java.util.List;

public class CardSummaryAdapter extends RecyclerView.Adapter<FlashCardSummaryViewHolder> {
    private final List<FlashCard> cards;

    public CardSummaryAdapter(List<FlashCard> cards) {
        this.cards = cards;
    }

    @NonNull
    @Override
    public FlashCardSummaryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.flash_card_summary_view_holder, parent, false);
        return new FlashCardSummaryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FlashCardSummaryViewHolder holder, int position) {
        FlashCard flashCard = cards.get(position);
        holder.setSummaryTopicText(flashCard.getTopic());
        holder.setSummaryQuestionText(flashCard.getQuestion());
        holder.setCardId(flashCard.getUuid());
        holder.setCardAnswer(flashCard.getAnswer());
    }

    @Override
    public int getItemCount() {
        return cards.size();
    }
}
