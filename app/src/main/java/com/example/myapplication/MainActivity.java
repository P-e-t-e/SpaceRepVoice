package com.example.myapplication;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.model.FlashCard;
import com.example.myapplication.tools.CardResourceStore;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_EDIT_CARD = 991;
    private static final int REQUEST_PLAY_CARDS = 992;
    private RecyclerView summaryRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Intent intent = getIntent();
        summaryRecyclerView = (RecyclerView) findViewById(R.id.card_summary_recycler);
        setUpRecyclerView(summaryRecyclerView);
        if (intent.hasExtra("toast")) {
            getIntent().getStringExtra("toast");
            Toast.makeText(this, R.string.card_saved_toast, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        Log.d("temp", "MainActivity onActivityResult");
        super.onActivityResult(requestCode, resultCode, data);
        setNewRecyclerViewCardAdapter(summaryRecyclerView);
        if (requestCode == RESULT_OK) {
            if (data != null && data.hasExtra("toast")) {
                Log.d("temp", "MainActivity toastTriggered");
                Toast.makeText(this, data.getStringExtra("toast"), Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void setUpRecyclerView(RecyclerView recyclerView) {
        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        recyclerView.setHasFixedSize(true);

        // use a linear layout manager
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        setNewRecyclerViewCardAdapter(recyclerView);

    }

    private void setNewRecyclerViewCardAdapter(RecyclerView recyclerView) {
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

    public void onClickAddCardButton(View view) {
        Intent intent = new Intent(this, ViewEditCardActivity.class);
        startActivity(intent);
    }

    public void onClickEditCardButton(View view) {
        Intent intent = new Intent(this, ViewEditCardActivity.class);
        TextView summaryTopic = findViewById(R.id.card_summary_topic);
        TextView summaryQuestion = findViewById(R.id.card_summary_question);
        TextView cardId = findViewById(R.id.card_id);
        TextView cardAnswer = findViewById(R.id.card_answer);
        intent.putExtra("edit", true);
        intent.putExtra("topic", summaryTopic.getText());
        intent.putExtra("question", summaryQuestion.getText());
        intent.putExtra("card_id", cardId.getText());
        intent.putExtra("card_answer", cardAnswer.getText());
        startActivityForResult(intent, REQUEST_EDIT_CARD);
    }

    public void onClickDeleteCardButton(View view)
    {
        String flashcardFilePath = CardResourceStore.getCardFileName();
        SharedPreferences sharedPreferences = getSharedPreferences(flashcardFilePath, Context.MODE_PRIVATE);
        TextView cardId = findViewById(R.id.card_id);
        CardResourceStore.deleteCard(cardId.getText().toString(), sharedPreferences);
        Toast.makeText(this, "Card deleted", Toast.LENGTH_SHORT).show();
        setNewRecyclerViewCardAdapter(summaryRecyclerView);
    }

    public void onClickStartPlayingButton(View view) {
        Intent intent = new Intent(this, PlayQuestionsActivity.class);
        startActivityForResult(intent, REQUEST_PLAY_CARDS);
    }
}