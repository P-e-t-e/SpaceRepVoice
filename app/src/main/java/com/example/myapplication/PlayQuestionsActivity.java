package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.view.View;
import android.widget.Toast;

import java.util.Locale;

public class PlayQuestionsActivity extends AppCompatActivity {

    TextToSpeech tts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_questions);
        tts = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int i) {
                if(i != TextToSpeech.ERROR)
                {
                    tts.setLanguage(Locale.UK);
                }
            }
        });
    }

    public void onClickStartButton(View view)
    {
        Toast.makeText(this, "Speaking!", Toast.LENGTH_SHORT).show();
        tts.speak("testing tts", TextToSpeech.QUEUE_FLUSH, null);
    }
}