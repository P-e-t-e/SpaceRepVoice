package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.myapplication.model.FlashCard;
import com.example.myapplication.tools.CardResourceStore;

public class ViewEditCard extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_edit_card);
        Intent intent = getIntent();
        if(intent.getBooleanExtra("edit", false))
        {
            presetValuesFromIntent(intent);
        }
    }

    private void presetValuesFromIntent(Intent intent) {
        String uuid = intent.getStringExtra("card_id");
        String topic = intent.getStringExtra("topic");
        String question = intent.getStringExtra("question");
        String answer = intent.getStringExtra("card_answer");
        TextView id_view = findViewById(R.id.card_id);
        TextView topic_view = findViewById(R.id.card_topic_text);
        TextView question_view = findViewById(R.id.card_question_text);
        TextView answer_view = findViewById(R.id.card_answer_text);
        id_view.setText(uuid);
        topic_view.setText(topic);
        question_view.setText(question);
        answer_view.setText(answer);
    }

    public void onClickSaveCard(View view) {
        String topic = ((TextView) findViewById(R.id.card_topic_text)).getText().toString();
        String quesiton = ((TextView) findViewById(R.id.card_question_text)).getText().toString();
        String answer = ((TextView) findViewById(R.id.card_answer_text)).getText().toString();
        String id = ((TextView) findViewById(R.id.card_id)).getText().toString();
        String resourceFilePath = CardResourceStore.getCardFileName();
        SharedPreferences sharedPreferences = getSharedPreferences(resourceFilePath, Context.MODE_PRIVATE);
        FlashCard flashCard;
        if(id.isEmpty())
        {
             flashCard = new FlashCard(quesiton, answer, topic);
        }
        else
        {
            flashCard = new FlashCard(quesiton, answer, topic, id);
        }
        CardResourceStore.saveFlashCard(flashCard, sharedPreferences);
        launchMain("New card has been saved");
    }

    public void launchMain(String toast)
    {
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("toast", toast);
        startActivity(intent);
    }
}