package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Adapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.model.FlashCard;
import com.example.myapplication.tools.CardResourceStore;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Intent intent = getIntent();
        setUpRecyclerView();
        if(intent.hasExtra("toast"))
        {
            getIntent().getStringExtra("toast");
            Toast.makeText(this, R.string.card_saved_toast, Toast.LENGTH_SHORT).show();
        }
        Log.d("test", "in on start main");
    }

    private void setUpRecyclerView() {
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.card_summary_recycler);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        recyclerView.setHasFixedSize(true);

        // use a linear layout manager
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        // specify an adapter (see also next example)
        List<FlashCard> cards = getAllFlashCardSummaries();
        RecyclerView.Adapter mAdapter = new CardSummaryAdapter(cards);
        recyclerView.setAdapter(mAdapter);
    }

    private List<FlashCard> getAllFlashCardSummaries() {
        String flashcardFilePath = CardResourceStore.getCardFileName();
        SharedPreferences sharedPreferences = getSharedPreferences(flashcardFilePath, Context.MODE_PRIVATE);
        return CardResourceStore.getFlashCards(sharedPreferences);
    }

    @Override
    protected void onResume() {
        Log.d("test", "in on resume main");
        super.onResume();
    }

    public void onClickAddCardButton(View view)
    {
        Intent intent = new Intent(this, ViewEditCard.class);
        startActivity(intent);
    }

    public void onClickEditCardButton(View view)
    {
        Intent intent = new Intent(this, ViewEditCard.class);
        TextView summaryTopic = findViewById(R.id.card_summary_topic);
        TextView summaryQuestion = findViewById(R.id.card_summary_question);
        TextView cardId = findViewById(R.id.card_id);
        TextView cardAnswer = findViewById(R.id.card_answer);
        intent.putExtra("edit", true);
        intent.putExtra("topic", summaryTopic.getText());
        intent.putExtra("question", summaryQuestion.getText());
        intent.putExtra("card_id", cardId.getText());
        intent.putExtra("card_answer", cardAnswer.getText());
        startActivity(intent);
    }
}