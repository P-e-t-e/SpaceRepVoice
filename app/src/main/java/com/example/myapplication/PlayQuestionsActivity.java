package com.example.myapplication;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.model.CardStatus;
import com.example.myapplication.model.FlashCard;
import com.example.myapplication.tools.CardResourceStore;

import java.util.List;
import java.util.Locale;

public class PlayQuestionsActivity extends AppCompatActivity {

    TextToSpeech tts;
    private static final int SPEECH_REQUEST_CODE = 0;
    private static final int secondsWaitForAnswer = 20;
    List<FlashCard> flashCards;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_questions);
        tts = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int i) {
                if (i != TextToSpeech.ERROR) {
                    tts.setLanguage(Locale.UK);
                }
            }
        });

        List<FlashCard> flashCards = CardResourceStore.getFlashCards(getSharedPreferences(CardResourceStore.getCardFileName(), Context.MODE_PRIVATE));
    }

    /**
     * Get the most urgent flashcard based on time til next due, if no card due within next 5 mins returns null
     *
     * @return most urgent card
     */
    private FlashCard getMostUrgentFlashcard() {
        long currentTimeMillis = System.currentTimeMillis();
        long currentLowestTimeDue = 0l;
        FlashCard currentMostUrgentCard = null;
        for (FlashCard card : flashCards) {
            CardStatus cardStatus = card.getCardStatus();
            long timeNextDue = cardStatus.getUnixTimeLastAsked() + (cardStatus.getMinutesUntilNextAskDue() * 60 * 1000);
            if (currentLowestTimeDue == 0l || timeNextDue < currentLowestTimeDue) {
                currentLowestTimeDue = timeNextDue;
                currentMostUrgentCard = card;
            }
        }
        return (currentTimeMillis + (5 * 60 * 1000)) >= currentLowestTimeDue ? currentMostUrgentCard : null;
    }

    public void onClickStartButton(View view) {
        Toast.makeText(this, "Speaking!", Toast.LENGTH_SHORT).show();
        tts.speak("testing tts", TextToSpeech.QUEUE_FLUSH, null);
        displaySpeechRecognizer();
    }

    private void askMostUrgentQuestion() {

    }

    // Create an intent that can start the Speech Recognizer activity
    private void displaySpeechRecognizer() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        // Start the activity, the intent will be populated with the speech text
        startActivityForResult(intent, SPEECH_REQUEST_CODE);
    }

    // This callback is invoked when the Speech Recognizer returns.
    // This is where you process the intent and extract the speech text from the intent.
    @Override
    protected void onActivityResult(int requestCode, int resultCode,
                                    Intent data) {
        if (requestCode == SPEECH_REQUEST_CODE && resultCode == RESULT_OK) {
            List<String> results = data.getStringArrayListExtra(
                    RecognizerIntent.EXTRA_RESULTS);
            String spokenText = results.get(0);
            tts.speak("Did you say " + spokenText, TextToSpeech.QUEUE_FLUSH, null);
            // Do something with spokenText
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}