package com.example.myapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;

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
        holder.setSummaryTopicText(cards.get(position).getTopic());
        holder.setSummaryQuestionText(cards.get(position).getQuestion());
    }

    @Override
    public int getItemCount() {
        return cards.size();
    }
}