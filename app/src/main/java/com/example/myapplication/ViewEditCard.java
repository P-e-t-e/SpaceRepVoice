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
    }

    public void onClickSaveCard(View view) {
        String topic = ((TextView) findViewById(R.id.card_topic_text)).getText().toString();
        String quesiton = ((TextView) findViewById(R.id.card_question_text)).getText().toString();
        String answer = ((TextView) findViewById(R.id.card_answer_text)).getText().toString();
        String resourceFilePath = CardResourceStore.getCardFileName();
        SharedPreferences sharedPreferences = getSharedPreferences(resourceFilePath, Context.MODE_PRIVATE);
        FlashCard flashCard = new FlashCard(quesiton, answer, topic);
        CardResourceStore.saveFlashCard(flashCard, sharedPreferences);
        launchMain();
    }

    public void launchMain()
    {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}